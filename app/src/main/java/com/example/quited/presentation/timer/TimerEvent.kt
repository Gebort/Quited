package com.example.quited.presentation.timer

sealed class TimerEvent {
    object saveCigg: TimerEvent()
    object undoCigg: TimerEvent()
}