package com.example.quited.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quited.domain.model.Plan
import com.example.quited.presentation.util.Date

@Entity
data class PlanDto(
        @PrimaryKey (autoGenerate = false)  val id: Int = 1,
        val startDate: Long,
        val endDate: Long,
        val startTime: Long,
        val endTime: Long,
        val startAmount: Int,
        val endAmount: Int
){

    fun toPlan(): Plan {
        return Plan (
                startAmount = startAmount,
                endAmount = endAmount,
                startTime = Date.fromLocal(startTime),
                endTime = Date.fromLocal(endTime),
                startDate = Date.fromLocal(startDate),
                endDate = Date.fromLocal(endDate)
        )
    }
}