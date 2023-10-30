package com.fcsibbul.ui

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

class CubeInScalingTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.cameraDistance = 20000F
        when {
            position < -1 -> {   //{-infinity,-1}
                // page offset to left side
                page.alpha = 0F
            }
            position <= 0 -> {
                // transition from left
                // side of page to the current page
                page.alpha = 1F
                page.pivotX = page.width.toFloat()
                page.rotationY = 90F * abs(position)
            }
            position <= 1 -> {
                // transition from the current page to the right side
                page.alpha = 1F
                page.pivotX = 0F
                page.rotationY = -90F * abs(position)
            }
            //{1,+infinity}
            else -> { // Page offset to the right side
                page.alpha = 0F
            }
        }

        when {
            // transition between page1 and page2
            abs(position) <= 0.5 -> {
                page.scaleY = max(0.4f, 1 - abs(position))
            }
            abs(position) <= 1 -> {
                page.scaleY = max(0.4f, abs(position))
            }
        }
    }
}
