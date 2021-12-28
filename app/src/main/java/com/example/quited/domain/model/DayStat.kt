package com.example.quited.domain.model

import com.example.quited.presentation.util.Date

data class DayStat(
        val date: Date,
        val ciggsAmount: Int,
        val maxAmount: Int
)