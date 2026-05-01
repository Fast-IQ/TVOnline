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
import android.graphics.drawable.GradientDrawable
import android.view.View
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
    private var timeOffsetHours = 0 // Смещение по времени в часах
    private var channels: List<Channel> = emptyList()

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    // Настройки временной шкалы
    private val pixelsPerHour = 200f // Пикселей на час
    private val pixelsPerMinute = pixelsPerHour / 60f
    private val rowHeight = 80 // Высота строки канала в пикселях
    private val startHour = 6 // Начало отображения (6:00)
    private val endHour = 30 // Конец отображения (30 часов = 6:00 следующего дня)
    private val totalHours = endHour - startHour

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epg)

        currentTimeZoneOffset = preferences.timezoneOffset
        currentTimeText = findViewById(R.id.currentTimeText)
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

        for (hour in startHour until endHour) {
            val timeText = TextView(this).apply {
                text = String.format("%02d:00", hour % 24)
                textSize = 14f
                setTextColor(getColor(R.color.text_secondary))
                width = pixelsPerHour.toInt()
                gravity = android.view.Gravity.CENTER
            }
            timeScaleContainer.addView(timeText)

            // Добавляем разделительные линии
            if (hour % 2 == 0 && hour != startHour) {
                val separator = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(2, 40)
                    setBackgroundColor(getColor(R.color.text_secondary))
                }
                // Смещаем назад на половину ширины
            }
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

        val baseTime = getStartTimeMillis()

        channels.forEachIndexed { index, channel ->
            val channelPrograms = programs[channel.id] ?: return@forEachIndexed

            channelPrograms.forEach { program ->
                // Вычисляем позицию и размер программы
                val startTimeOffset = (program.startTime - baseTime) / 1000f / 60f // минуты от начала
                val endTimeOffset = (program.endTime - baseTime) / 1000f / 60f
                val duration = (program.endTime - program.startTime) / 1000f / 60f // длительность в минутах

                // Пропускаем программы вне видимого диапазона
                if (endTimeOffset < startHour * 60 || startTimeOffset > endHour * 60) return@forEach

                val leftMargin = (startTimeOffset * pixelsPerMinute).toInt().coerceAtLeast(0)
                val width = (duration * pixelsPerMinute).toInt().coerceAtLeast(60)

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

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
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

        val timeText = TextView(this).apply {
            text = "${dateFormat.format(Date(program.startTime))} - ${dateFormat.format(Date(program.endTime))}"
            textSize = 11f
            setTextColor(getColor(R.color.text_secondary))
        }

        val titleText = TextView(this).apply {
            text = program.title
            textSize = 13f
            setTextColor(getColor(R.color.white))
            maxLines = 2
            ellipsize = android.text.TextUtils.TruncateAt.END
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 4
            }
        }

        container.addView(timeText)
        container.addView(titleText)

        // Проверка на текущую программу
        val currentTime = System.currentTimeMillis() + (currentTimeZoneOffset * 60 * 60 * 1000L)
        if (program.isLive(currentTime)) {
            container.setBackgroundResource(R.drawable.live_indicator)
        }

        return container
    }

    private fun getStartTimeMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            time = selectedDate
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis - (currentTimeZoneOffset * 60 * 60 * 1000L)
    }

    private fun loadEPGForDate() {
        scope.launch {
            try {
                val adjustedDate = Date(selectedDate.time + (currentTimeZoneOffset * 60 * 60 * 1000L))

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
        val currentTime = System.currentTimeMillis() + ((currentTimeZoneOffset + timeOffsetHours) * 60 * 60 * 1000L)
        val baseTime = getStartTimeMillis()

        val minutesFromStart = (currentTime - baseTime) / 1000f / 60f
        val scrollPosition = (minutesFromStart * pixelsPerMinute).toInt() - (epgHorizontalScroll.width / 4)

        epgHorizontalScroll.scrollTo(scrollPosition.coerceAtLeast(0), 0)
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

    private fun changeDate(days: Int) {
        val calendar = Calendar.getInstance().apply {
            time = selectedDate
            add(Calendar.DAY_OF_YEAR, days)
        }
        selectedDate = calendar.time
        timeOffsetHours = 0

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

        if (timeOffsetHours > 12) timeOffsetHours = 12
        if (timeOffsetHours < -12) timeOffsetHours = -12

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = System.currentTimeMillis() + ((currentTimeZoneOffset + timeOffsetHours) * 60 * 60 * 1000L)

        Toast.makeText(
            this,
            "Смещение: ${if (hours > 0) "+" else ""}${hours}ч (${timeFormat.format(Date(currentTime))})",
            Toast.LENGTH_SHORT
        ).show()

        loadEPGForDate()
        updateCurrentTimeDisplay()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
