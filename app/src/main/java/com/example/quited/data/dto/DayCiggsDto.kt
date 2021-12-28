package com.example.quited.data.dto

import com.example.quited.domain.model.DayCiggs
import com.example.quited.presentation.util.Date

data class DayCiggsDto(
        val dateLong: Long,
        val ciggsAmount: Int
        ) {
    fun toDayCiggs(): DayCiggs {
        return DayCiggs(
                date = Date.fromLocal(dateLong),
                ciggsAmount = ciggsAmount
        )
    }
}
