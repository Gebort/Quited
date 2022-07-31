package com.example.quited.presentation.util

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private const val TIME_PATTERN = "HH:mm:ss"

data class Duration(
        val timeLong: Long,
        val timeStr: String,
        val timeSnap: LocalTime
) {
    companion object {
        fun fromLocal(time: Long): Duration {
            val instant = Instant.ofEpochMilli(time)
            val dateSnap = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
            val timeStr  = DateTimeFormatter.ofPattern(TIME_PATTERN).format(dateSnap)
            return Duration(time, timeStr, dateSnap.toLocalTime())
        }
        fun fromString(string: String): Duration {
            val time = try {
                LocalTime.parse(string, DateTimeFormatter.ofPattern(TIME_PATTERN))
            } catch(e: DateTimeParseException){
                LocalTime.of(0, 0)
            }
            val dateTime = LocalDateTime.of(1970, 1, 1, time.hour, time.minute, time.second, time.nano)
            val timeLong = dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
            return Duration(timeLong, string, time)
        }
    }
}
