package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.repository.CiggsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GetCountdownUseCase(
        private val repository: CiggsRepositoryImpl
) {
     suspend operator fun invoke(): Long? {
         val result = repository.getLastCigg()
         return if (result == null) null else result.dateLong + result.timeLong
    }
}