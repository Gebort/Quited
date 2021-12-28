package com.example.quited.domain.repository

import com.example.quited.data.dto.CiggDto
import com.example.quited.data.dto.DayCiggsDto
import com.example.quited.data.dto.PlanDto
import kotlinx.coroutines.flow.Flow

interface CiggsRepository {

    fun getCiggsAmountByDay(dateLong: Long): Flow<DayCiggsDto?>

    fun getCiggsAmountByDaysAfter(dateLong: Long): Flow<List<DayCiggsDto>?>

    fun getPlan(): Flow<PlanDto?>

    suspend fun getLastCigg(): CiggDto?

    suspend fun insertCigg(ciggDto: CiggDto): Long

    suspend fun deleteCigg(ciggDto: CiggDto)

    suspend fun setPlan(planDto: PlanDto)

}