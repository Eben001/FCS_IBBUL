package com.fcsibbul.core

import android.content.Context
import android.graphics.Color
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat

object Bars {

    /**
     * Sets the status bar color and the icon mode.
     * @param color The color to set the status bar to.
     * @param darkIcons True if icons should be dark (for light backgrounds), false for light icons.
     */
    fun Window.setStatusBarColor(color: Int, darkIcons: Boolean) {
        this.statusBarColor = color
        WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = darkIcons
    }

    fun Window.updateNavbarColour(ctx: Context = this.context) {
        val isDark = inDarkMode(ctx)
        this.navigationBarColor = if (isDark) {
            Color.BLACK
        } else {
            Color.parseColor("#F5F5F5")
        }
        WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightNavigationBars = !isDark
    }

    fun inDarkMode(context: Context): Boolean {
        return (context.resources.configuration.uiMode and 
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) == 
                android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
}