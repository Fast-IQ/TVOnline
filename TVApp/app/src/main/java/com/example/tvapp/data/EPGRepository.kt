package com.example.tvapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class EPGRepository {
    
    private val epgBaseUrl = "https://epg.iptv-manager.com/epg"
    private val dateFormat = SimpleDateFormat("yyyyMMddHHmmss Z", Locale.getDefault())
    
    suspend fun getProgramForChannel(channelId: String, date: Date): List<Program> {
        return withContext(Dispatchers.IO) {
            try {
                // Пытаемся загрузить EPG для канала
                val url = "$epgBaseUrl/$channelId.xml"
                val xmlContent = URL(url).readText()
                parseEPGXml(xmlContent, channelId, date)
            } catch (e: Exception) {
                // Если не удалось загрузить, возвращаем заглушку
                generateFakeEPG(channelId, date)
            }
        }
    }
    
    suspend fun getProgramsForAllChannels(date: Date): Map<String, List<Program>> {
        return withContext(Dispatchers.IO) {
            val programs = mutableMapOf<String, List<Program>>()
            
            for (channel in ChannelList.channels) {
                try {
                    val channelPrograms = getProgramForChannel(channel.id, date)
                    programs[channel.id] = channelPrograms
                } catch (e: Exception) {
                    programs[channel.id] = generateFakeEPG(channel.id, date)
                }
            }
            
            programs
        }
    }
    
    private fun parseEPGXml(xml: String, channelId: String, date: Date): List<Program> {
        val programs = mutableListOf<Program>()
        // Упрощенный парсинг XML (в реальном приложении лучше использовать XmlPullParser)
        // Здесь базовая реализация
        
        return if (programs.isEmpty()) {
            generateFakeEPG(channelId, date)
        } else {
            programs
        }
    }
    
    private fun generateFakeEPG(channelId: String, baseDate: Date): List<Program> {
        val programs = mutableListOf<Program>()
        val calendar = Calendar.getInstance().apply {
            time = baseDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val channel = ChannelList.channels.find { it.id == channelId }
        val channelName = channel?.name ?: "Канал"
        
        // Генерируем программу на 24 часа с разной длительностью передач
        val programTitles = listOf(
            "Новости",
            "Утреннее шоу",
            "Документальный фильм",
            "Сериал",
            "Ток-шоу",
            "Фильм",
            "Спорт",
            "Музыка",
            "Детская передача",
            "Вечерние новости",
            "Прайм-тайм шоу",
            "Ночной фильм"
        )
        
        // Разная длительность передач в минутах (чтобы не было всегда ровно в 00)
        val durations = listOf(45, 55, 30, 50, 40, 65, 25, 35, 60, 50, 45, 55)
        
        var currentTime = calendar.timeInMillis
        
        for (i in 0 until 12) {
            val durationMinutes = durations[i % durations.size]
            val duration = durationMinutes * 60 * 1000L
            val startTime = currentTime
            val endTime = startTime + duration
            
            val title = programTitles[i % programTitles.size]
            
            programs.add(
                Program(
                    channelId = channelId,
                    title = title,
                    description = "Описание передачи $title",
                    startTime = startTime,
                    endTime = endTime,
                    iconUrl = null
                )
            )
            
            currentTime = endTime
        }
        
        return programs
    }
    
    fun getCurrentProgram(programs: List<Program>, currentTime: Long = System.currentTimeMillis()): Program? {
        return programs.find { it.isLive(currentTime) }
    }
    
    fun adjustTimeForTimezone(time: Long, timezoneOffset: Int): Long {
        return time + (timezoneOffset * 60 * 60 * 1000L)
    }
}
