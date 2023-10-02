package com.fcsibbul.ui.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.fcsibbul.ui.common.Accent.convertAccentColorToInt
import com.fcsibbul.ui.common.Accent.convertToAccentColor

class Prefs(context: Context) {

    private var sharedPrefs: SharedPreferences = getDefaultSharedPreferences(context)
    private var editor: SharedPreferences.Editor = sharedPrefs.edit()


    inner class Settings {
        private val themeKey = "THEME_KEY"
        private val accentKey = "ACCENT_KEY"
        private val barKey = "BOTTOM_BAR_KEY"

        // The theme for the app
        var theme: Int
            get() = sharedPrefs.getInt(themeKey, MODE_NIGHT_FOLLOW_SYSTEM)
            set(value) = editor.putInt(themeKey, value).apply()

        // The accent colour
        var accent: AccentColor
            get() = convertToAccentColor(sharedPrefs.getInt(accentKey, DEFAULT))
            set(value) = editor.putInt(accentKey, convertAccentColorToInt(value)).apply()

        var bottomBar: Int
            get() = sharedPrefs.getInt(barKey, 0)
            set(value) = editor.putInt(barKey, value).apply()
    }


    companion object {
        const val DEFAULT: Int = 0
    }
}
