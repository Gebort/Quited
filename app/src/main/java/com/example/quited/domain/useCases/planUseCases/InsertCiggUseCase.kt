package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.repository.CiggsRepositoryImpl
import com.example.quited.domain.model.Cigg

class InsertCiggUseCase(
        private val repository: CiggsRepositoryImpl
) {
    suspend operator fun invoke(cigg: Cigg): Long{
        return repository.insertCigg(cigg.toDto())
    }
}