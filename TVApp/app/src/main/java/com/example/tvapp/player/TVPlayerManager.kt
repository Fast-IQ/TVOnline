package com.example.tvapp.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.tvapp.data.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.ConnectException

class TVPlayerManager(private val context: Context) {
    
    private var exoPlayer: ExoPlayer? = null
    private val preferences = AppPreferences(context)
    
    interface PlayerCallback {
        fun onPlaybackReady()
        fun onPlaybackError(error: String)
        fun onBuffering(isBuffering: Boolean)
    }
    
    @OptIn(UnstableApi::class)
    fun initializePlayer(playerView: PlayerView, callback: PlayerCallback? = null) {
        // Настраиваем контроллер загрузки с увеличенными буферами для ТВ
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                15000, // minBufferMs - 15 секунд
                60000, // maxBufferMs - 60 секунд
                3000,  // bufferForPlaybackMs - 3 секунды до начала воспроизведения
                6000   // bufferForPlaybackAfterRebufferMs - 6 секунд после повторной буферизации
            )
            .build()
        
        exoPlayer = ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build().apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_READY -> {
                                callback?.onPlaybackReady()
                            }
                            Player.STATE_BUFFERING -> {
                                callback?.onBuffering(true)
                            }
                            Player.STATE_IDLE -> {
                                callback?.onBuffering(false)
                            }
                            Player.STATE_ENDED -> {
                                // Воспроизведение завершено
                            }
                        }
                    }
                    
                    override fun onPlayerError(error: PlaybackException) {
                        val errorMessage = when (error.errorCode) {
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> 
                                "Ошибка сетевого подключения. Проверьте интернет-соединение."
                            PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE -> 
                                "Неподдерживаемый формат потока."
                            PlaybackException.ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED -> 
                                "Формат видео не поддерживается."
                            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS ->
                                "Сервер вернул ошибку доступа."
                            else -> "${error.message ?: "Неизвестная ошибка"} (код: ${error.errorCode})"
                        }
                        callback?.onPlaybackError(errorMessage)
                    }
                    
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        if (isPlaying) {
                            callback?.onBuffering(false)
                        }
                    }
                })
            }
        playerView.player = exoPlayer
    }
    
    fun playChannel(streamUrl: String, channelId: String) {
        exoPlayer?.apply {
            // Очищаем текущий медиа элемент перед загрузкой нового
            clearMediaItems()
            
            // Создаем MediaItem с настройками для/live потоков
            val mediaItem = MediaItem.fromUri(streamUrl).buildUpon()
                .setTag(channelId) // Добавляем тег для идентификации
                .build()
            
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            
            // Сохраняем последний канал
            preferences.lastChannelId = channelId
        }
    }
    
    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }
    
    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: 0L
    }
    
    fun getDuration(): Long {
        return exoPlayer?.duration ?: 0L
    }
    
    fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying == true
    }
    
    fun pause() {
        exoPlayer?.pause()
    }
    
    fun resume() {
        exoPlayer?.play()
    }
    
    fun releasePlayer() {
        exoPlayer?.apply {
            stop()
            release()
        }
        exoPlayer = null
    }
    
    fun setQualityMode(qualityMode: AppPreferences.QualityMode) {
        preferences.qualityMode = qualityMode
        // Здесь можно реализовать логику переключения качества
        // Для HLS потоков это может быть выбор соответствующей дорожки
    }
}
