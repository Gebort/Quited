package com.example.quited.presentation.util

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

private const val TIME_PATTERN = "HH.mm"
private const val DATE_PATTERN = "dd.MM.yyyy"

data class Date(
        val datestamp: Long,
        val dateStr: String,
        val timeStr: String,
        val dateLong: Long,
        val timeLong: Long,
        val dateTime: ZonedDateTime
){

    fun daysDiff(date: Date): Int {
     return ((dateLong - date.dateLong)/1000/60/60/24).toInt()
    }

    fun plusDays(days: Int): Date {
        val datestampNew = datestamp + days*24*60*60*1000L
        val instant = Instant.ofEpochMilli(datestampNew)
        val dateSnap = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
        val timeStr  = DateTimeFormatter.ofPattern(TIME_PATTERN).format(dateSnap)
        val dateStr  = DateTimeFormatter.ofPattern(DATE_PATTERN).format(dateSnap)
        val dateLong = LocalDate.of(dateSnap.year, dateSnap.month, dateSnap.dayOfMonth).toEpochDay()*24*60*60*1000
        val timeLong = datestampNew - dateLong
        return Date(datestampNew, dateStr, timeStr, dateLong, timeLong, dateSnap)
    }

    fun toUTC(): Date {
        val dateUTC = datestamp - TimeZone.getDefault().rawOffset - TimeZone.getDefault().dstSavings
        val instant = Instant.ofEpochMilli(dateUTC)
        val dateSnap = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
        val timeStr  = DateTimeFormatter.ofPattern(TIME_PATTERN).format(dateSnap)
        val dateStr  = DateTimeFormatter.ofPattern(DATE_PATTERN).format(dateSnap)
        val dateLong = LocalDate.of(dateSnap.year, dateSnap.month, dateSnap.dayOfMonth).toEpochDay()*24*60*60*1000
        val datestamp = dateSnap.toEpochSecond()*1000L + dateSnap.offset.totalSeconds*1000L
        val timeLong = datestamp - dateLong
        return Date(datestamp, dateStr, timeStr, dateLong, timeLong, dateSnap)
    }

    companion object {

        fun fromUTC(dateUTC: Long): Date {
            val instant = Instant.ofEpochMilli(dateUTC)
            val dateSnap = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
            val timeStr  = DateTimeFormatter.ofPattern(TIME_PATTERN).format(dateSnap)
            val dateStr  = DateTimeFormatter.ofPattern(DATE_PATTERN).format(dateSnap)
            val dateLong = LocalDate.of(dateSnap.year, dateSnap.month, dateSnap.dayOfMonth).toEpochDay()*24*60*60*1000
            val datestamp = dateSnap.toEpochSecond()*1000L + dateSnap.offset.totalSeconds*1000L
            val timeLong = datestamp - dateLong
            return Date(datestamp, dateStr, timeStr, dateLong, timeLong, dateSnap)
        }
        fun fromLocal(dateLocal: Long): Date {
            val instant = Instant.ofEpochMilli(dateLocal)
            val dateSnap = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
            val timeStr  = DateTimeFormatter.ofPattern(TIME_PATTERN).format(dateSnap)
            val dateStr  = DateTimeFormatter.ofPattern(DATE_PATTERN).format(dateSnap)
            val dateLong = LocalDate.of(dateSnap.year, dateSnap.month, dateSnap.dayOfMonth).toEpochDay()*24*60*60*1000
            val timeLong = dateLocal - dateLong
            return Date(dateLocal, dateStr, timeStr, dateLong, timeLong, dateSnap)
        }
        fun fromLocalTime(hour: Int, minutes: Int): Date {
            val localDateTime = LocalDateTime.of(1970, 1, 1, hour, minutes)
            val dateSnap = ZonedDateTime.ofLocal(localDateTime, ZoneOffset.UTC, ZoneOffset.UTC)
            val timeStr  = DateTimeFormatter.ofPattern(TIME_PATTERN).format(dateSnap)
            val dateStr  = DateTimeFormatter.ofPattern(DATE_PATTERN).format(dateSnap)
            val dateLong = LocalDate.of(dateSnap.year, dateSnap.month, dateSnap.dayOfMonth).toEpochDay()*24*60*60*1000
            val timeLong = dateSnap.toEpochSecond()*1000L + dateSnap.offset.totalSeconds*1000L - dateLong
            return Date(timeLong, dateStr, timeStr, dateLong, timeLong, dateSnap)
        }
    }
}
