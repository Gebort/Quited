package com.example.quited.data.util

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.quited.data.dataSource.CiggsDatabase
import com.example.quited.domain.useCases.planUseCases.CiggsUseCases
import com.example.quited.domain.useCases.planUseCases.PlanUseCases


class MyApp : Application() {

    private var database: CiggsDatabase? = null

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(instance!!.applicationContext, CiggsDatabase::class.java, CiggsDatabase.DATABASE_NAME)
                .build()
    }

    fun getDatabase(): CiggsDatabase? {
        return database
    }

    companion object {
        var instance: MyApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}