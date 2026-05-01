package com.example.tvapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.R
import com.example.tvapp.data.Channel
import com.example.tvapp.data.Program
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

class EPGAdapter(
    private var channels: List<Channel>,
    private var programs: Map<String, List<Program>>,
    private val timezoneOffset: Int
) : RecyclerView.Adapter<EPGAdapter.EPGViewHolder>() {

    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    // Константы для отображения
    private val pixelsPerMinute = 2f // 2 пикселя на минуту
    private val startHour = 0 // Начало отображения с 00:00
    private val endHour = 24 // Конец отображения 24:00
    private val totalMinutes = (endHour - startHour) * 60 // 1440 минут
    
    fun getPrograms(): Map<String, List<Program>> = programs

    inner class EPGViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val channelName: TextView = itemView.findViewById(R.id.channelName)
        private val programContainer: LinearLayout = itemView.findViewById(R.id.programContainer)

        fun bind(channel: Channel, channelPrograms: List<Program>?) {
            channelName.text = channel.name
            
            // Очищаем контейнер
            programContainer.removeAllViews()
            
            val sortedPrograms = channelPrograms?.sortedBy { it.startTime } ?: emptyList()
            
            // Добавляем передачи в контейнер
            sortedPrograms.forEach { program ->
                val programView = createProgramView(program)
                programContainer.addView(programView)
            }
            
            // Фокус для навигации
            itemView.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.setBackgroundResource(R.drawable.focus_highlight)
                } else {
                    view.setBackgroundResource(android.R.color.transparent)
                }
            }
        }
        
        private fun createProgramView(program: Program): View {
            val view = LayoutInflater.from(itemView.context)
                .inflate(R.layout.item_program, programContainer, false)
            
            val titleText: TextView = view.findViewById(R.id.programTitle)
            val timeText: TextView = view.findViewById(R.id.programTime)
            
            titleText.text = program.title
            timeText.text = "${dateFormat.format(Date(program.startTime))} - ${dateFormat.format(Date(program.endTime))}"
            
            // Вычисляем позицию и ширину программы
            val startTimeMinutes = getMinutesFromStartOfDay(program.startTime)
            val endTimeMinutes = getMinutesFromStartOfDay(program.endTime)
            val durationMinutes = max(0, endTimeMinutes - startTimeMinutes)
            
            // Позиционирование через LayoutParams
            val params = LinearLayout.LayoutParams(
                (durationMinutes * pixelsPerMinute).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.marginStart = (startTimeMinutes * pixelsPerMinute).toInt()
            params.topMargin = 4
            params.bottomMargin = 4
            view.layoutParams = params
            
            // Подсветка текущей программы
            val currentTime = System.currentTimeMillis() + (timezoneOffset * 60 * 60 * 1000L)
            if (program.isLive(currentTime)) {
                view.setBackgroundResource(R.drawable.live_indicator)
            } else {
                view.setBackgroundResource(R.drawable.program_item_background)
            }
            
            // Обработка выбора программы
            view.setOnClickListener {
                // Можно реализовать перемотку к этой программе если это архив
            }
            
            // Фокус
            view.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.scaleX = 1.1f
                    v.scaleY = 1.1f
                } else {
                    v.scaleX = 1.0f
                    v.scaleY = 1.0f
                }
            }
            
            return view
        }
        
        private fun getMinutesFromStartOfDay(timestamp: Long): Int {
            val calendar = Calendar.getInstance().apply {
                time = Date(timestamp)
            }
            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            return hours * 60 + minutes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EPGViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel_epg, parent, false)
        return EPGViewHolder(view)
    }

    override fun onBindViewHolder(holder: EPGViewHolder, position: Int) {
        val channel = channels[position]
        val channelPrograms = programs[channel.id]
        holder.bind(channel, channelPrograms)
    }

    override fun getItemCount(): Int = channels.size

    fun updatePrograms(newPrograms: Map<String, List<Program>>) {
        programs = newPrograms
        notifyDataSetChanged()
    }
    
    fun getPixelsPerMinute(): Float {
        return pixelsPerMinute
    }
    
    fun getTotalWidth(): Int {
        return (totalMinutes * pixelsPerMinute).toInt()
    }
    
    fun getTimeScaleMarks(): List<Pair<Int, String>> {
        val marks = mutableListOf<Pair<Int, String>>()
        for (hour in startHour..endHour) {
            val minutePosition = hour * 60 * pixelsPerMinute
            val timeLabel = if (hour < 24) String.format("%02d:00", hour) else "24:00"
            marks.add(Pair(minutePosition.toInt(), timeLabel))
        }
        return marks
    }
}
