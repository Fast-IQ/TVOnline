package com.example.tvapp.ui

import android.os.Bundle
import android.view.KeyEvent
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
    private val epgRepository = EPGRepository()
    private val preferences by lazy { AppPreferences(this) }
    
    private var currentTimeZoneOffset = 0
    private var selectedDate = Date()
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epg)
        
        currentTimeZoneOffset = preferences.timezoneOffset
        
        epgRecyclerView = findViewById(R.id.epgRecyclerView)
        
        setupEPGGrid()
        loadEPGForDate()
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
                
            } catch (e: Exception) {
                Toast.makeText(this@EPGActivity, "Ошибка загрузки программы передач", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                // Переключение на предыдущий день
                changeDate(-1)
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                // Переключение на следующий день
                changeDate(1)
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
        
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        Toast.makeText(
            this,
            "Дата: ${dateFormat.format(selectedDate)}",
            Toast.LENGTH_SHORT
        ).show()
        
        loadEPGForDate()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
