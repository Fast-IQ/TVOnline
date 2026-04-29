package com.example.tvapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.R
import com.example.tvapp.data.Channel
import com.example.tvapp.data.ChannelList
import com.example.tvapp.data.EPGRepository
import com.example.tvapp.data.AppPreferences
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var channelsRecyclerView: RecyclerView
    private lateinit var currentProgramText: TextView
    private lateinit var channelAdapter: ChannelAdapter
    
    private val epgRepository = EPGRepository()
    private val preferences by lazy { AppPreferences(this) }
    
    private var allPrograms = mapOf<String, List<com.example.tvapp.data.Program>>()
    private var currentTimeZoneOffset = 0
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        currentTimeZoneOffset = preferences.timezoneOffset
        
        channelsRecyclerView = findViewById(R.id.channelsRecyclerView)
        currentProgramText = findViewById(R.id.currentProgramText)
        
        setupChannelsGrid()
        loadEPG()
        restoreLastChannel()
    }
    
    private fun setupChannelsGrid() {
        // Для TV используем сетку 4-5 колонок
        val spanCount = 5
        channelAdapter = ChannelAdapter(
            channels = ChannelList.channels,
            onChannelSelected = { channel ->
                openPlayer(channel)
            },
            onSettingsClicked = {
                openSettings()
            },
            onEPGClicked = {
                openEPG()
            }
        )
        
        channelsRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, spanCount)
            adapter = channelAdapter
            
            // Навигация с пульта
            descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
        }
    }
    
    private fun loadEPG() {
        scope.launch {
            try {
                val date = Date()
                allPrograms = withContext(Dispatchers.IO) {
                    epgRepository.getProgramsForAllChannels(date)
                }
                
                // Обновляем адаптер с текущими программами
                updateCurrentPrograms()
                
                // Периодически обновляем информацию о текущей программе
                launch {
                    while (isActive) {
                        delay(60000) // Каждую минуту
                        updateCurrentPrograms()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Ошибка загрузки программы передач", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateCurrentPrograms() {
        val adjustedTime = System.currentTimeMillis() + (currentTimeZoneOffset * 60 * 60 * 1000L)
        
        val currentProgramsMap = mutableMapOf<String, String>()
        
        for ((channelId, programs) in allPrograms) {
            val currentProgram = epgRepository.getCurrentProgram(programs, adjustedTime)
            currentProgram?.let {
                currentProgramsMap[channelId] = it.title
            }
        }
        
        channelAdapter.updateCurrentPrograms(currentProgramsMap)
        
        // Обновляем текст текущей программы для выбранного канала
        if (ChannelList.channels.isNotEmpty()) {
            val firstChannelId = ChannelList.channels[0].id
            currentProgramsMap[firstChannelId]?.let {
                currentProgramText.text = "Сейчас: $it"
            }
        }
    }
    
    private fun restoreLastChannel() {
        val lastChannelId = preferences.lastChannelId
        if (lastChannelId != null) {
            val channel = ChannelList.channels.find { it.id == lastChannelId }
            channel?.let {
                // Прокручиваем к последнему каналу
                val position = ChannelList.channels.indexOf(it)
                if (position != -1) {
                    channelsRecyclerView.scrollToPosition(position)
                }
            }
        }
    }
    
    private fun openPlayer(channel: Channel) {
        val intent = android.content.Intent(this, PlayerActivity::class.java).apply {
            putExtra("channel_id", channel.id)
            putExtra("channel_name", channel.name)
            putExtra("stream_url", channel.streamUrl)
            putExtra("logo_url", channel.logoUrl)
        }
        startActivity(intent)
    }
    
    private fun openSettings() {
        val intent = android.content.Intent(this, settings.SettingsActivity::class.java)
        startActivity(intent)
    }
    
    private fun openEPG() {
        val intent = android.content.Intent(this, EPGActivity::class.java)
        startActivity(intent)
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                openSettings()
                true
            }
            KeyEvent.KEYCODE_INFO -> {
                openEPG()
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
