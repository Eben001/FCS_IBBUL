

package com.ebenezer.gana.fcsibbul.core

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat.getColor
import com.ebenezer.gana.fcsibbul.R

/**
 * Sets navigation bar colour based off android version
 */

@Suppress("DEPRECATION")
fun Window.setNavigationBarColourDefault() {
    if (!inDarkMode(this.context)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, // value
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS // mask
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        navigationBarColor = getColor(context, R.color.background)
    }
}

fun inDarkMode(context: Context): Boolean {
    context.resources.configuration.run {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) isNightModeActive
        else uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}

@Suppress("DEPRECATION")
fun Window.setDarkStatusIcons() {
    if (!inDarkMode(this.context)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, // value
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS // mask
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

private fun Context.resolveThemeAttr(@AttrRes attrRes: Int): TypedValue {
    return TypedValue().apply { theme.resolveAttribute(attrRes, this, true) }
}

@ColorInt
fun Context.resolveColorAttr(@AttrRes colorAttr: Int): Int {
    val resolvedAttr = resolveThemeAttr(colorAttr)
    val colorRes = if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
    return getColor(this, colorRes)
}


fun List<Any>.find(item: Any): Int {
    for (i in 0..this.size) {
        if (this[i] == item) return i
    }
    return -1
}

