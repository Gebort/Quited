package com.example.quited.data.util

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.quited.data.dataSource.CiggsDatabase
import com.example.quited.domain.useCases.planUseCases.CiggsUseCases
import com.example.quited.domain.useCases.planUseCases.PlanUseCases
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn

private const val TIME_STEP = 1000L

class MyApp : Application() {

    private var database: CiggsDatabase? = null
    private var timeFlow: Flow<Long>? = null
    private var timeJob: Job? = null
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(instance!!.applicationContext, CiggsDatabase::class.java, CiggsDatabase.DATABASE_NAME)
                .build()
        timeFlow = flow {
            while (true) {
                emit(System.currentTimeMillis())
                delay(TIME_STEP)
            }
        }
        timeJob = timeFlow?.launchIn(applicationScope)
    }

    fun getDatabase(): CiggsDatabase? {
        return database
    }

    fun getTimeFlow(): Flow<Long>? {
        return timeFlow
    }

    companion object {
        var instance: MyApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}