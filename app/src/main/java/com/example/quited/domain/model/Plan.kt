package com.example.quited.domain.model

import com.example.quited.data.dto.PlanDto
import com.example.quited.presentation.util.Date

data class Plan(
    val startDate: Date,
    val endDate: Date,
    val startTime: Date,
    val endTime: Date,
    val startAmount: Int,
    val endAmount: Int
) {

    fun getTimeDelay(date: Date): Long {
        val amount = getMaxAmount(date)
        val time = endTime.timeLong - startTime.timeLong
        return time/amount
    }

    fun getMaxAmount(date: Date): Int {
        return if (date.dateLong > endDate.dateLong){
            endAmount
        }
        else if (date.dateLong < startDate.dateLong){
            startAmount
        }
        else {
            val currDaysDiff = date.daysDiff(startDate)
            val daysDiff = endDate.daysDiff(startDate)
            val value = startAmount - currDaysDiff * ((startAmount - endAmount).toFloat() / daysDiff)
            value.toInt()
        }
    }

    fun toDto(): PlanDto {
        return PlanDto(
                startAmount = startAmount,
                endAmount = endAmount,
                startTime = startTime.timeLong,
                endTime = endTime.timeLong,
                startDate = startDate.dateLong,
                endDate = endDate.dateLong
        )
    }

}