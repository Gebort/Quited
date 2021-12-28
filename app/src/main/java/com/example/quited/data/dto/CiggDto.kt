package com.example.quited.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quited.domain.model.Cigg
import com.example.quited.presentation.util.Date

@Entity
data class CiggDto(
        @PrimaryKey(autoGenerate = true) val id: Int? = null,
        val dateLong: Long,
        val timeLong: Long
) {
    fun toCigg(): Cigg {
        return Cigg(
                id = id,
                date = Date.fromLocal(dateLong + timeLong)
        )
    }
}