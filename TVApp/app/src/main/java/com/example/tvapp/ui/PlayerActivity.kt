package com.example.tvapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tvapp.R
import android.widget.TextView

class PlayerActivity : AppCompatActivity() {
    
    private var channelId: String? = null
    private var streamUrl: String? = null
    private var channelName: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ТЕСТОВАЯ ВЕРСИЯ - пустая активность без плеера
        // Создаем простой TextView для отображения информации
        val textView = TextView(this).apply {
            text = "PlayerActivity запущена успешно!\n\n" +
                   "Данные канала:\n" +
                   "ID: ${intent.getStringExtra("channel_id")}\n" +
                   "URL: ${intent.getStringExtra("stream_url")}\n" +
                   "Название: ${intent.getStringExtra("channel_name")}"
            textSize = 24f
            setTextColor(android.graphics.Color.WHITE)
            setPadding(50, 50, 50, 50)
        }
        
        setContentView(textView)
        
        // Получаем данные о канале
        channelId = intent.getStringExtra("channel_id")
        streamUrl = intent.getStringExtra("stream_url")
        channelName = intent.getStringExtra("channel_name")
        
        Toast.makeText(this, "Активность открыта! Это тестовая версия без плеера.", Toast.LENGTH_LONG).show()
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
    }
}
