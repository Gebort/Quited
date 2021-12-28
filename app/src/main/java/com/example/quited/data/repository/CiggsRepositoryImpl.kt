package com.example.quited.data.repository

import com.example.quited.data.dto.CiggDto
import com.example.quited.data.dto.DayCiggsDto
import com.example.quited.data.dto.PlanDto
import com.example.quited.data.util.MyApp
import com.example.quited.domain.repository.CiggsRepository
import kotlinx.coroutines.flow.Flow

class CiggsRepositoryImpl: CiggsRepository{

    private val dao = MyApp.instance?.getDatabase()?.ciggsDao!!

    override fun getCiggsAmountByDay(dateLong: Long): Flow<DayCiggsDto?> {
        return dao.getCiggsAmountByDay(dateLong)
    }

    override fun getCiggsAmountByDaysAfter(dateLong: Long): Flow<List<DayCiggsDto>?> {
        return dao.getCiggsAmountByDaysAfter(dateLong)
    }

    override fun getPlan(): Flow<PlanDto?> {
        return dao.getPlan()
    }

    override suspend fun getLastCigg(): CiggDto? {
        return dao.getLastCigg()
    }

    override suspend fun insertCigg(ciggDto: CiggDto): Long {
        return dao.insertCigg(ciggDto)
    }

    override suspend fun deleteCigg(ciggDto: CiggDto) {
        return dao.deleteCigg(ciggDto)
    }

    override suspend fun setPlan(planDto: PlanDto) {
        return dao.setPlan(planDto)
    }

}