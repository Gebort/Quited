package com.example.quited.presentation.stats

import com.example.quited.domain.model.Plan

sealed class StatsUiEvent{
    object debug: StatsUiEvent()
    object list: StatsUiEvent()

}
