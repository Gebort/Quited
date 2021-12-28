package com.example.quited.presentation.timer

import com.example.quited.domain.model.DayStat
import com.example.quited.presentation.util.Date
import com.example.quited.presentation.util.Duration

data class TimerState(
        val dayStat: DayStat = DayStat(Date.fromUTC(System.currentTimeMillis()), 0, 0),
        val duration: Duration = Duration.fromLocal(1L),
        val maxDuration: Duration = Duration.fromLocal(1L),
        val loading: Boolean = true,
        val planSet: Boolean = false
)
