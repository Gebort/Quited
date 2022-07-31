package com.example.quited.presentation.timer

import com.example.quited.presentation.util.Date

sealed class TimerUiEvent{
    object CiggSaved: TimerUiEvent()
    data class Notification(val date: Date): TimerUiEvent()
    object CancelNotification: TimerUiEvent()
}
