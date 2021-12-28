package com.example.quited.presentation.settings

import com.example.quited.presentation.util.Date

data class SettingsState(
        val loading: Boolean = true,
        val startAmount: Int = 20,
        val endAmount: Int = 5,
        val startDate: Date = Date.fromUTC(System.currentTimeMillis()),
        val daysAmount: Int = 60,
        val startTime: Date = Date.fromLocalTime(7, 30),
        val endTime: Date = Date.fromLocalTime(23, 0)
)
