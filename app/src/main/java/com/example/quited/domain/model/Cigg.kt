package com.example.quited.domain.model

import com.example.quited.data.dto.CiggDto
import com.example.quited.presentation.util.Date

data class Cigg(
        val id: Int? = null,
        val date: Date = Date.fromUTC(System.currentTimeMillis())
) {
    fun toDto(): CiggDto {
        return CiggDto(
                id = id,
                dateLong = date.dateLong,
                timeLong = date.timeLong
        )
    }
}
