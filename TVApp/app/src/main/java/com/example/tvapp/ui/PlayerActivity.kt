package com.example.tvapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.ui.PlayerView
import com.example.tvapp.R
import com.example.tvapp.player.TVPlayerManager
import com.example.tvapp.data.EPGRepository
import com.example.tvapp.data.AppPreferences
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {
    
    private lateinit var playerView: PlayerView
    private lateinit var playerManager: TVPlayerManager
    private val epgRepository = EPGRepository()
    private val preferences by lazy { AppPreferences(this) }
    
    private var channelId: String? = null
    private var streamUrl: String? = null
    private var channelName: String? = null
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        
        playerView = findViewById<PlayerView>(R.id.playerView)
        
        // Получаем данные о канале
        channelId = intent.getStringExtra("channel_id")
        streamUrl = intent.getStringExtra("stream_url")
        channelName = intent.getStringExtra("channel_name")
        
        // Инициализируем плеер
        playerManager = TVPlayerManager(this)
        playerManager.initializePlayer(playerView, object : TVPlayerManager.PlayerCallback {
            override fun onPlaybackReady() {
                // Воспроизведение готово
            }
            
            override fun onPlaybackError(error: String) {
                Toast.makeText(this@PlayerActivity, "Ошибка воспроизведения: $error", Toast.LENGTH_SHORT).show()
            }
            
            override fun onBuffering(isBuffering: Boolean) {
                // Индикация буферизации
            }
        })
        
        // Запускаем воспроизведение
        streamUrl?.let { url ->
            channelId?.let { id ->
                playerManager.playChannel(url, id)
            }
        }
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                if (playerManager.isPlaying()) {
                    playerManager.pause()
                } else {
                    playerManager.resume()
                }
                true
            }
            KeyEvent.KEYCODE_MEDIA_STOP -> {
                finish()
                true
            }
            KeyEvent.KEYCODE_INFO -> {
                // Показать информацию о текущей программе
                showCurrentProgramInfo()
                true
            }
            KeyEvent.KEYCODE_MENU -> {
                // Показать меню с опциями
                showOptionsMenu()
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                // Перемотка вперед (если поддерживается)
                seekRelative(30000) // +30 секунд
                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                // Перемотка назад (если поддерживается)
                seekRelative(-30000) // -30 секунд
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
    
    private fun showCurrentProgramInfo() {
        if (channelId == null) return
        
        scope.launch {
            try {
                val programs = withContext(Dispatchers.IO) {
                    epgRepository.getProgramForChannel(channelId!!, Date())
                }
                
                val currentProgram = epgRepository.getCurrentProgram(programs)
                currentProgram?.let { program ->
                    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val startTime = dateFormat.format(Date(program.startTime))
                    val endTime = dateFormat.format(Date(program.endTime))
                    
                    Toast.makeText(
                        this@PlayerActivity,
                        "${program.title}\n$startTime - $endTime",
                        Toast.LENGTH_LONG
                    ).show()
                } ?: run {
                    Toast.makeText(
                        this@PlayerActivity,
                        "Информация о программе недоступна",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@PlayerActivity,
                    "Ошибка загрузки информации",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun showOptionsMenu() {
        // Здесь можно показать меню с дополнительными опциями
        // Например, выбор качества, переключение аудио дорожки и т.д.
        Toast.makeText(this, "Меню (нажмите Настройки для основных настроек)", Toast.LENGTH_SHORT).show()
    }
    
    private fun seekRelative(offsetMs: Long) {
        val newPosition = playerManager.getCurrentPosition() + offsetMs
        if (newPosition >= 0) {
            playerManager.seekTo(newPosition)
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Приостанавливаем воспроизведение при переходе в фон
        playerManager.pause()
    }
    
    override fun onResume() {
        super.onResume()
        // Возобновляем воспроизведение при возврате
        playerManager.resume()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        playerManager.releasePlayer()
        scope.cancel()
    }
}
