package com.example.quited.data.dataSource

import androidx.room.*
import com.example.quited.data.dto.CiggDto
import com.example.quited.data.dto.DayCiggsDto
import com.example.quited.data.dto.PlanDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CiggsDao {

    @Query ("SELECT dateLong, COUNT(*) as ciggsAmount FROM ciggdto WHERE dateLong = :dateLong")
    fun getCiggsAmountByDay(dateLong: Long): Flow<DayCiggsDto?>

    @Query ("SELECT dateLong, COUNT(*) as ciggsAmount FROM ciggdto WHERE dateLong >= :dateLong GROUP BY dateLong")
    fun getCiggsAmountByDaysAfter(dateLong: Long): Flow<List<DayCiggsDto>?>

    @Query("SELECT * FROM plandto WHERE id = 1")
    fun getPlan(): Flow<PlanDto?>

    @Query("SELECT * FROM ciggdto ORDER BY dateLong DESC, timeLong DESC LIMIT 1")
    suspend fun getLastCigg(): CiggDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCigg(ciggDto: CiggDto): Long

    @Delete
    suspend fun deleteCigg(ciggDto: CiggDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlan(plan: PlanDto)

}