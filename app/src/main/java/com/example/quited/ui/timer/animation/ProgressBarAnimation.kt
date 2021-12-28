package com.example.quited.ui.timer.animation

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import kotlin.math.abs

private const val PROGRESS_BAR_ANIM_COEF = 10

class ProgressBarAnimation(
        private val progressBar: ProgressBar,
        private val from: Int = progressBar.progress,
        private val to: Int
        ) : Animation()
{
    init {
        duration = (abs(to - from) * PROGRESS_BAR_ANIM_COEF).toLong()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
        val c = progressBar.progress
    }
}