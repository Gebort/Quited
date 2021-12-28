package com.example.quited.data.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quited.data.dto.CiggDto
import com.example.quited.data.dto.PlanDto

@Database(
        entities = [CiggDto::class, PlanDto::class],
        version = 1
)
abstract class CiggsDatabase: RoomDatabase() {

    abstract val ciggsDao: CiggsDao

    companion object{
        const val DATABASE_NAME = "ciggs_db"
    }
}