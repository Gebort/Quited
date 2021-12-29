package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.util.MyApp
import kotlinx.coroutines.flow.Flow

class GetTimeUseCase {

    operator fun invoke(): Flow<Long> {
        return MyApp.instance!!.getTimeFlow()!!
    }
}