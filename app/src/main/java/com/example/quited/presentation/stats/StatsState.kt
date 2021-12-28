package com.example.quited.presentation.stats

import com.example.quited.domain.model.DayStat

data class StatsState(
  val days: List<DayStat> = listOf(),
  val planSet: Boolean = false,
  val loading: Boolean = true
)