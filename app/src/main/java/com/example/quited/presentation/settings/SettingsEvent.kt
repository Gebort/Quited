package com.example.quited.presentation.settings

import com.example.quited.presentation.util.Date

sealed class SettingsEvent{
    data class EnteredStartAmount (val startAmount: Int): SettingsEvent()
    data class EnteredEndAmount (val endAmount: Int): SettingsEvent()
    data class EnteredStartDate (val startDate: Date): SettingsEvent()
    data class EnteredDaysAmount (val daysAmount: Int): SettingsEvent()
    data class EnteredStartTime (val startTime: Date): SettingsEvent()
    data class EnteredEndTime (val endTime: Date): SettingsEvent()
    object SwitchNotification: SettingsEvent()
    object Save: SettingsEvent()
}
