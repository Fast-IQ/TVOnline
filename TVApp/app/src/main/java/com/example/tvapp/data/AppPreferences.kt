package com.example.tvapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val gson = Gson()

    companion object {
        private const val KEY_LAST_CHANNEL = "last_channel_id"
        private const val KEY_TIMEZONE_OFFSET = "timezone_offset"
        private const val KEY_QUALITY_MODE = "quality_mode"
        private const val KEY_LANGUAGE = "language"
    }

    var lastChannelId: String?
        get() = prefs.getString(KEY_LAST_CHANNEL, null)
        set(value) = prefs.edit().putString(KEY_LAST_CHANNEL, value).apply()

    var timezoneOffset: Int
        get() = prefs.getInt(KEY_TIMEZONE_OFFSET, 0)
        set(value) = prefs.edit().putInt(KEY_TIMEZONE_OFFSET, value).commit()

    var qualityMode: QualityMode
        get() = QualityMode.values()[prefs.getInt(KEY_QUALITY_MODE, QualityMode.AUTO.ordinal)]
        set(value) = prefs.edit().putInt(KEY_QUALITY_MODE, value.ordinal).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "ru") ?: "ru"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    enum class QualityMode {
        MINIMUM,
        MEDIUM,
        MAXIMUM,
        AUTO
    }
}
