package com.example.tvapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.R
import com.example.tvapp.data.ChannelList
import com.example.tvapp.data.EPGRepository
import com.example.tvapp.data.AppPreferences
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class EPGActivity : AppCompatActivity() {
    
    private lateinit var epgRecyclerView: RecyclerView
    private lateinit var currentTimeText: TextView
    private lateinit var timeScaleContainer: LinearLayout
    private val epgRepository = EPGRepository()
    private val preferences by lazy { AppPreferences(this) }
    
    private var currentTimeZoneOffset = 0
    private var selectedDate = Date()
    private var timeOffsetHours = 0 // Смещение по времени в часах
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epg)
        
        currentTimeZoneOffset = preferences.timezoneOffset
        currentTimeText = findViewById(R.id.currentTimeText)
        timeScaleContainer = findViewById(R.id.timeScaleContainer)
        
        epgRecyclerView = findViewById(R.id.epgRecyclerView)
        
        setupEPGGrid()
        setupTimeScale()
        loadEPGForDate()
        updateCurrentTimeDisplay()
    }
    
    private fun setupEPGGrid() {
        val epgAdapter = EPGAdapter(
            channels = ChannelList.channels,
            programs = emptyMap(),
            timezoneOffset = currentTimeZoneOffset
        )
        
        epgRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@EPGActivity)
            adapter = epgAdapter
        }
    }
    
    private fun setupTimeScale() {
        // Создаем временную шкалу с метками каждый час
        val adapter = epgRecyclerView.adapter as? EPGAdapter ?: return
        val marks = adapter.getTimeScaleMarks()
        
        timeScaleContainer.removeAllViews()
        
        marks.forEach { (position, timeLabel) ->
            val textView = TextView(this).apply {
                text = timeLabel
                textSize = 12f
                setTextColor(getColor(R.color.text_secondary))
                minWidth = 60 // Минимальная ширина для каждой метки
                gravity = android.view.Gravity.CENTER
            }
            
            // Устанавливаем отступ слева для позиционирования
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.marginStart = if (position == 0) 0 else ((position - marks[marks.size - 2].first) / 24)
            textView.layoutParams = params
            
            timeScaleContainer.addView(textView)
        }
    }
    
    private fun loadEPGForDate() {
        scope.launch {
            try {
                val adjustedDate = Date(selectedDate.time + (currentTimeZoneOffset * 60 * 60 * 1000L))
                
                val programs = withContext(Dispatchers.IO) {
                    epgRepository.getProgramsForAllChannels(adjustedDate)
                }
                
                // Обновляем адаптер
                val adapter = epgRecyclerView.adapter as? EPGAdapter
                adapter?.updatePrograms(programs)
                
                // Прокрутка к текущему времени после обновления данных
                epgRecyclerView.post {
                    scrollToCurrentTime()
                }
                
            } catch (e: Exception) {
                Toast.makeText(this@EPGActivity, "Ошибка загрузки программы передач", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun scrollToCurrentTime() {
        val adapter = epgRecyclerView.adapter as? EPGAdapter ?: return
        
        // Находим первую видимую программу для текущего времени
        val currentTime = System.currentTimeMillis() + ((currentTimeZoneOffset + timeOffsetHours) * 60 * 60 * 1000L)
        
        // Прокручиваем к первой строке (первый канал)
        epgRecyclerView.scrollToPosition(0)
        
        // Для горизонтального списка программ первого канала находим текущую программу
        val firstViewHolder = epgRecyclerView.findViewHolderForAdapterPosition(0) as? EPGAdapter.EPGViewHolder
        firstViewHolder?.let { holder ->
            val programContainer = holder.itemView.findViewById<LinearLayout>(R.id.programContainer)
            val scrollView = programContainer.parent as? HorizontalScrollView
            
            // Находим индекс текущей программы
            val channelId = ChannelList.channels.firstOrNull()?.id
            val programs = adapter.getPrograms()[channelId]
            val currentProgramIndex = programs?.indexOfFirst { 
                it.startTime <= currentTime && it.endTime > currentTime 
            } ?: 0
            
            if (currentProgramIndex >= 0 && scrollView != null) {
                // Вычисляем позицию прокрутки на основе времени начала программы
                val startMinutes = getMinutesFromStartOfDay(programs[currentProgramIndex].startTime)
                val scrollPosition = (startMinutes * adapter.getPixelsPerMinute()).toInt()
                scrollView.scrollTo(scrollPosition, 0)
            }
        }
    }
    
    private fun getMinutesFromStartOfDay(timestamp: Long): Int {
        val calendar = Calendar.getInstance().apply {
            time = Date(timestamp)
        }
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        return hours * 60 + minutes
    }
    
    private fun updateCurrentTimeDisplay() {
        val currentTime = System.currentTimeMillis() + ((currentTimeZoneOffset + timeOffsetHours) * 60 * 60 * 1000L)
        val dateFormat = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
        currentTimeText.text = "Время: ${dateFormat.format(Date(currentTime))}"
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                // Прокрутка списка каналов вверх
                scrollChannelList(-1)
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                // Прокрутка списка каналов вниз
                scrollChannelList(1)
                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                // Сдвиг по времени влево (назад) на 1 час для всех каналов
                changeTimeOffset(-1)
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                // Сдвиг по времени вправо (вперед) на 1 час для всех каналов
                changeTimeOffset(1)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
    
    private fun scrollChannelList(direction: Int) {
        val currentPosition = (epgRecyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0
        val newPosition = (currentPosition + direction).coerceIn(0, ChannelList.channels.size - 1)
        epgRecyclerView.scrollToPosition(newPosition)
        
        val channelName = ChannelList.channels.getOrNull(newPosition)?.name
        Toast.makeText(this, "Канал: $channelName", Toast.LENGTH_SHORT).show()
    }
    
    private fun changeTimeOffset(hours: Int) {
        timeOffsetHours += hours
        
        // Ограничиваем смещение +/- 12 часов
        if (timeOffsetHours > 12) timeOffsetHours = 12
        if (timeOffsetHours < -12) timeOffsetHours = -12
        
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = System.currentTimeMillis() + ((currentTimeZoneOffset + timeOffsetHours) * 60 * 60 * 1000L)
        
        Toast.makeText(
            this,
            "Смещение: ${if (hours > 0) "+" else ""}${hours}ч (${timeFormat.format(Date(currentTime))})",
            Toast.LENGTH_SHORT
        ).show()
        
        // Перезагружаем EPG с учетом нового смещения
        loadEPGForDate()
        updateCurrentTimeDisplay()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
