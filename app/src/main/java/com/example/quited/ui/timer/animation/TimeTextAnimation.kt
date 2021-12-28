package com.example.quited.ui.timer.animation

import android.widget.TextView
import com.example.quited.presentation.util.Duration
import android.view.animation.Animation
import android.view.animation.Transformation
import kotlin.math.abs

private const val TIME_ANIM_COEF = 0.0005

class TimeTextAnimation(
        private val textView: TextView,
        private val from: Duration,
        private val to: Duration
) : Animation()
{
    init {
        duration = (abs(to.timeLong - from.timeLong) * TIME_ANIM_COEF).toLong()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val value = from.timeLong + (to.timeLong - from.timeLong) * interpolatedTime
        textView.text = Duration.fromLocal(value.toLong()).timeStr
    }
}