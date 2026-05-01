package com.example.tvapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.R
import com.example.tvapp.data.Channel
import com.example.tvapp.data.ChannelList
import com.example.tvapp.data.EPGRepository
import com.example.tvapp.data.AppPreferences
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class EPGActivity : AppCompatActivity() {
    
    private lateinit var epgRecyclerView: RecyclerView
    private lateinit var currentTimeText: TextView
    private val epgRepository = EPGRepository()
    private val preferences by lazy { AppPreferences(this) }
    
    private var currentTimeZoneOffset = 0
    private var selectedDate = Date()
    private var timeOffsetHours = 0 // Смещение по времени в часах
    private var channels: List<Channel> = emptyList() // Список каналов
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epg)
        
        currentTimeZoneOffset = preferences.timezoneOffset
        currentTimeText = findViewById(R.id.currentTimeText)
        
        epgRecyclerView = findViewById(R.id.epgRecyclerView)
        
        setupEPGGrid()
        loadEPGForDate()
        updateCurrentTimeDisplay()
    }
    
    private fun setupEPGGrid() {
        channels = ChannelList.channels // Сохраняем список каналов
        val epgAdapter = EPGAdapter(
            channels = channels,
            programs = emptyMap(),
            timezoneOffset = currentTimeZoneOffset
        )
        
        epgRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@EPGActivity)
            adapter = epgAdapter
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
            val programList = holder.itemView.findViewById<RecyclerView>(R.id.programList)
            val layoutManager = programList.layoutManager as? LinearLayoutManager
            
            // Находим индекс текущей программы
            val channelId = ChannelList.channels.firstOrNull()?.id
            val programs = adapter.getPrograms()[channelId]
            val currentProgramIndex = programs?.indexOfFirst { 
                it.startTime <= currentTime && it.endTime > currentTime 
            } ?: 0
            
            if (currentProgramIndex >= 0 && layoutManager != null) {
                // Прокручиваем так, чтобы текущая программа была видна
                layoutManager.scrollToPositionWithOffset(currentProgramIndex, programList.width / 4)
            }
        }
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
                val currentPos = (epgRecyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0
                if (currentPos > 0) {
                    epgRecyclerView.scrollToPosition(currentPos - 1)
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                // Прокрутка списка каналов вниз
                val currentPos = (epgRecyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0
                if (currentPos < channels.size - 1) {
                    epgRecyclerView.scrollToPosition(currentPos + 1)
                }
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
    
    private fun changeDate(days: Int) {
        val calendar = Calendar.getInstance().apply {
            time = selectedDate
            add(Calendar.DAY_OF_YEAR, days)
        }
        selectedDate = calendar.time
        timeOffsetHours = 0 // Сбрасываем смещение по времени при смене даты
        
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        Toast.makeText(
            this,
            "Дата: ${dateFormat.format(selectedDate)}",
            Toast.LENGTH_SHORT
        ).show()
        
        loadEPGForDate()
        updateCurrentTimeDisplay()
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
