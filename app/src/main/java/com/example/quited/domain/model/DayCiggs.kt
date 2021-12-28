package com.example.quited.domain.model

import com.example.quited.data.dto.DayCiggsDto
import com.example.quited.presentation.util.Date

data class DayCiggs(
        val date: Date,
        val ciggsAmount: Int
) {
    fun toDto(): DayCiggsDto{
        return DayCiggsDto(
                dateLong = date.dateLong,
                ciggsAmount = ciggsAmount
        )
    }
}
