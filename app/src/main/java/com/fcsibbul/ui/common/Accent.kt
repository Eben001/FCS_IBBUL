package com.fcsibbul.ui.common

import android.content.Context
import com.fcsibbul.R

object Accent {
    fun Context.setAccentColour(accentColor: AccentColor) {

        this.setTheme(
            when (accentColor) {
                AccentColor.BLUE -> R.style.AppTheme_Blue
                AccentColor.CRIMSON -> R.style.AppTheme_Crimson
                AccentColor.GREEN -> R.style.AppTheme_Green
                AccentColor.LIGHT_BLUE -> R.style.AppTheme_LightBlue
                AccentColor.LIME -> R.style.AppTheme_Lime
                AccentColor.ORANGE -> R.style.AppTheme_Orange
                AccentColor.PINK -> R.style.AppTheme_Pink
                AccentColor.RED -> R.style.AppTheme_Red
                AccentColor.TEAL -> R.style.AppTheme_Teal
                AccentColor.VIOLET -> R.style.AppTheme_Violet
                AccentColor.YELLOW -> R.style.AppTheme_Yellow
                else -> R.style.Theme_FCSIBBUL
            }
        )
    }

    fun convertToAccentColor(int: Int): AccentColor {
        return when(int) {
            BLUE -> AccentColor.BLUE
            CRIMSON -> AccentColor.CRIMSON
            GREEN -> AccentColor.GREEN
            LIGHT_BLUE -> AccentColor.LIGHT_BLUE
            LIME -> AccentColor.LIME
            ORANGE -> AccentColor.ORANGE
            PINK -> AccentColor.PINK
            RED -> AccentColor.RED
            TEAL -> AccentColor.TEAL
            VIOLET -> AccentColor.VIOLET
            YELLOW -> AccentColor.YELLOW
            else -> AccentColor.ORIGINAL
        }
    }

    fun convertAccentColorToInt(accentColor: AccentColor): Int {
        return when(accentColor) {
            AccentColor.BLUE -> BLUE
            AccentColor.CRIMSON -> CRIMSON
            AccentColor.GREEN -> GREEN
            AccentColor.LIGHT_BLUE -> LIGHT_BLUE
            AccentColor.LIME -> LIME
            AccentColor.ORANGE -> ORANGE
            AccentColor.PINK -> PINK
            AccentColor.RED -> RED
            AccentColor.TEAL -> TEAL
            AccentColor.VIOLET -> VIOLET
            AccentColor.YELLOW -> YELLOW
            else -> ORIGINAL
        }
    }

    const val BLUE: Int = 1
    const val CRIMSON: Int = 2
    const val GREEN: Int = 3
    const val LIGHT_BLUE: Int = 4
    const val LIME: Int = 5
    const val ORANGE: Int = 6
    const val PINK: Int = 7
    const val RED: Int = 8
    const val TEAL: Int = 9
    const val VIOLET: Int = 10
    const val YELLOW: Int = 11
    const val ORIGINAL: Int = 12
}