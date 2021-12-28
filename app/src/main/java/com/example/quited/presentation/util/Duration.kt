package com.example.quited.presentation.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private const val TIME_PATTERN = "HH:mm:ss"

data class Duration(
        val timeLong: Long,
        val timeStr: String,
) {
    companion object {
        fun fromLocal(time: Long): Duration {
            val instant = Instant.ofEpochMilli(time)
            val dateSnap = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
            val timeStr  = DateTimeFormatter.ofPattern(TIME_PATTERN).format(dateSnap)
            return Duration(time, timeStr)
        }
        fun fromString(string: String): Duration {
            val time = try {
                LocalTime.parse(string, DateTimeFormatter.ofPattern(TIME_PATTERN))
            } catch(e: DateTimeParseException){
                LocalTime.of(0, 0)
            }
            val timeLong = time.hour*60*60*1000L + time.minute*60*1000L + time.second*1000L
            return Duration(timeLong, string)
        }
    }
}
