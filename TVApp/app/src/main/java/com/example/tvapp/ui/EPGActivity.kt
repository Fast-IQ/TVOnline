package com.example.tvapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import android.widget.FrameLayout
import android.view.View
import android.graphics.drawable.GradientDrawable
import com.example.tvapp.R
import com.example.tvapp.data.Channel
import com.example.tvapp.data.ChannelList
import com.example.tvapp.data.EPGRepository
import com.example.tvapp.data.Program
import com.example.tvapp.data.AppPreferences
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class EPGActivity : AppCompatActivity() {

    private lateinit var currentTimeText: TextView
    private lateinit var timeOffsetText: TextView
    private lateinit var timeScaleContainer: LinearLayout
    private lateinit var channelLabelsContainer: LinearLayout
    private lateinit var epgGridContainer: FrameLayout
    private lateinit var timeScaleScroll: HorizontalScrollView
    private lateinit var epgHorizontalScroll: HorizontalScrollView
    private lateinit var epgVerticalScroll: NestedScrollView
    private lateinit var channelLabelsScroll: NestedScrollView

    private val epgRepository = EPGRepository()
    private val preferences by lazy { AppPreferences(this) }

    private var currentTimeZoneOffset = 0
    private var selectedDate = Date()
    private var timeOffsetHours = 0 // Смещение по времени в часах (-12 до +12)
    private var channels: List<Channel> = emptyList()

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    // Настройки временной шкалы
    private val pixelsPerHour = 150f // Пикселей на час
    private val pixelsPerMinute = pixelsPerHour / 60f
    private val rowHeight = 70 // Высота строки канала в пикселях
    private val totalHours = 24 // Показываем 24 часа (от -12 до +12 от текущего времени)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epg)

        currentTimeZoneOffset = preferences.timezoneOffset
        currentTimeText = findViewById(R.id.currentTimeText)
        timeOffsetText = findViewById(R.id.timeOffsetText)
        timeScaleContainer = findViewById(R.id.timeScaleContainer)
        channelLabelsContainer = findViewById(R.id.channelLabelsContainer)
        epgGridContainer = findViewById(R.id.epgGridContainer)
        timeScaleScroll = findViewById(R.id.timeScaleScroll)
        epgHorizontalScroll = findViewById(R.id.epgHorizontalScroll)
        epgVerticalScroll = findViewById(R.id.epgVerticalScroll)
        channelLabelsScroll = findViewById(R.id.channelLabelsScroll)

        setupEPGGrid()
        loadEPGForDate()
        updateCurrentTimeDisplay()

        // Синхронизация горизонтальной прокрутки
        setupScrollSync()
    }

    private fun setupScrollSync() {
        // Синхронизируем прокрутку временной шкалы и сетки программ
        timeScaleScroll.viewTreeObserver.addOnScrollChangedListener {
            if (!isChangingConfigurations) {
                epgHorizontalScroll.scrollX = timeScaleScroll.scrollX
            }
        }

        epgHorizontalScroll.viewTreeObserver.addOnScrollChangedListener {
            if (!isChangingConfigurations) {
                timeScaleScroll.scrollX = epgHorizontalScroll.scrollX
            }
        }

        // Синхронизируем вертикальную прокрутку
        channelLabelsScroll.viewTreeObserver.addOnScrollChangedListener {
            if (!isChangingConfigurations) {
                epgVerticalScroll.scrollY = channelLabelsScroll.scrollY
            }
        }

        epgVerticalScroll.viewTreeObserver.addOnScrollChangedListener {
            if (!isChangingConfigurations) {
                channelLabelsScroll.scrollY = epgVerticalScroll.scrollY
            }
        }
    }

    private fun setupEPGGrid() {
        channels = ChannelList.channels

        // Создаем временную шкалу
        createTimeScale()

        // Создаем названия каналов
        createChannelLabels()

        // Устанавливаем размеры контейнера программ
        val totalWidth = (totalHours * pixelsPerHour).toInt()
        val totalHeight = channels.size * rowHeight
        epgGridContainer.layoutParams = FrameLayout.LayoutParams(totalWidth, totalHeight)
    }

    private fun createTimeScale() {
        timeScaleContainer.removeAllViews()

        // Показываем временную шкалу от -12 до +12 часов от текущего времени
        for (hourOffset in -12..12) {
            val calendar = Calendar.getInstance().apply {
                add(Calendar.HOUR_OF_DAY, hourOffset)
            }
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            
            val timeText = TextView(this).apply {
                text = String.format("%02d:00", hour)
                textSize = 14f
                setTextColor(getColor(R.color.text_secondary))
                width = pixelsPerHour.toInt()
                gravity = android.view.Gravity.CENTER
            }
            timeScaleContainer.addView(timeText)
        }
    }

    private fun createChannelLabels() {
        channelLabelsContainer.removeAllViews()

        for (channel in channels) {
            val channelText = TextView(this).apply {
                text = channel.name
                textSize = 14f
                setTextColor(getColor(R.color.white))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    rowHeight
                ).apply {
                    gravity = android.view.Gravity.CENTER_VERTICAL
                    setPadding(16, 0, 16, 0)
                }
            }
            channelLabelsContainer.addView(channelText)
        }
    }

    private fun renderPrograms(programs: Map<String, List<Program>>) {
        epgGridContainer.removeAllViews()

        // Базовое время - текущее время минус 12 часов
        val baseTime = getStartTimeMillis()

        channels.forEachIndexed { index, channel ->
            val channelPrograms = programs[channel.id] ?: return@forEachIndexed

            channelPrograms.forEach { program ->
                // Вычисляем позицию и размер программы относительно базового времени
                val startTimeOffset = (program.startTime - baseTime) / 1000f / 60f // минуты от начала
                val endTimeOffset = (program.endTime - baseTime) / 1000f / 60f
                val duration = (program.endTime - program.startTime) / 1000f / 60f // длительность в минутах

                // Пропускаем программы вне видимого диапазона (24 часа)
                val visibleStartMinutes = 0f
                val visibleEndMinutes = totalHours * 60f
                
                if (endTimeOffset < visibleStartMinutes || startTimeOffset > visibleEndMinutes) {
                    return@forEach
                }

                // Вычисляем левый отступ и ширину
                val leftMargin = (startTimeOffset * pixelsPerMinute).toInt().coerceAtLeast(0)
                val width = (duration * pixelsPerMinute).toInt().coerceAtLeast(40)

                // Создаем блок программы
                val programView = createProgramBlock(program, width)

                val params = FrameLayout.LayoutParams(width, rowHeight - 8).apply {
                    marginStart = leftMargin
                    topMargin = index * rowHeight + 4
                }

                epgGridContainer.addView(programView, params)
            }
        }
    }

    private fun createProgramBlock(program: Program, width: Int): View {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val container = FrameLayout(this).apply {
            setPadding(8, 8, 8, 8)
            setBackgroundResource(R.drawable.program_item_background)
            isClickable = true
            isFocusable = true

            // Обработка фокуса
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.scaleX = 1.05f
                    view.scaleY = 1.05f
                    view.setBackgroundResource(R.drawable.focus_highlight)
                } else {
                    view.scaleX = 1.0f
                    view.scaleY = 1.0f
                    view.setBackgroundResource(R.drawable.program_item_background)
                }
            }
        }

        // Текст времени начала-конца и названия передачи в одной строке
        val programText = TextView(this).apply {
            text = "${dateFormat.format(Date(program.startTime))} - ${dateFormat.format(Date(program.endTime))} ${program.title}"
            textSize = 12f
            setTextColor(getColor(R.color.white))
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
        }

        container.addView(programText)

        // Проверка на текущую программу
        val currentTime = System.currentTimeMillis() + (currentTimeZoneOffset * 60 * 60 * 1000L)
        if (program.isLive(currentTime)) {
            container.setBackgroundResource(R.drawable.live_indicator)
        }

        return container
    }

    private fun getStartTimeMillis(): Long {
        // Базовое время - текущее время минус 12 часов с учетом смещения
        val calendar = Calendar.getInstance().apply {
            time = selectedDate
            add(Calendar.HOUR_OF_DAY, -12 + timeOffsetHours)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis - (currentTimeZoneOffset * 60 * 60 * 1000L)
    }

    private fun loadEPGForDate() {
        scope.launch {
            try {
                // Учитываем смещение времени из настроек и текущее смещение пользователя
                val totalOffset = currentTimeZoneOffset + timeOffsetHours
                val adjustedDate = Date(selectedDate.time + (totalOffset * 60 * 60 * 1000L))

                val programs = withContext(Dispatchers.IO) {
                    epgRepository.getProgramsForAllChannels(adjustedDate)
                }

                renderPrograms(programs)

                // Прокрутка к текущему времени после обновления данных
                epgGridContainer.post {
                    scrollToCurrentTime()
                }

            } catch (e: Exception) {
                Toast.makeText(this@EPGActivity, "Ошибка загрузки программы передач", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scrollToCurrentTime() {
        // Текущее время находится посередине (через 12 часов от начала)
        val currentPos = (12 * pixelsPerHour).toInt() - (epgHorizontalScroll.width / 2)
        epgHorizontalScroll.scrollTo(currentPos.coerceAtLeast(0), 0)
    }

    private fun updateCurrentTimeDisplay() {
        val currentTime = System.currentTimeMillis() + ((currentTimeZoneOffset + timeOffsetHours) * 60 * 60 * 1000L)
        val dateFormat = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
        currentTimeText.text = "Время: ${dateFormat.format(Date(currentTime))}"
        
        val offsetText = if (timeOffsetHours >= 0) "+$timeOffsetHours" else "$timeOffsetHours"
        timeOffsetText.text = "Смещение: ${offsetText}ч"
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                val currentPos = channelLabelsScroll.scrollY
                if (currentPos > 0) {
                    channelLabelsScroll.smoothScrollBy(0, -rowHeight)
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                val maxScroll = channelLabelsContainer.height - channelLabelsScroll.height
                val currentPos = channelLabelsScroll.scrollY
                if (currentPos < maxScroll) {
                    channelLabelsScroll.smoothScrollBy(0, rowHeight)
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                changeTimeOffset(-1)
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                changeTimeOffset(1)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем часовой пояс при возврате из настроек
        currentTimeZoneOffset = preferences.timezoneOffset
        loadEPGForDate()
    }

    private fun changeTimeOffset(hours: Int) {
        timeOffsetHours += hours

        // Ограничиваем смещение от -12 до +12
        if (timeOffsetHours > 12) {
            timeOffsetHours = 12
            Toast.makeText(this, "Максимальное смещение: +12 часов", Toast.LENGTH_SHORT).show()
        }
        if (timeOffsetHours < -12) {
            timeOffsetHours = -12
            Toast.makeText(this, "Минимальное смещение: -12 часов", Toast.LENGTH_SHORT).show()
        }

        // Перерисовываем временную шкалу
        createTimeScale()
        
        // Загружаем программы для нового временного диапазона
        loadEPGForDate()
        updateCurrentTimeDisplay()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
