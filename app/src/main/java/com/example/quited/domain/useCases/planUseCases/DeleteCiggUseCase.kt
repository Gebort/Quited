package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.repository.CiggsRepositoryImpl
import com.example.quited.domain.model.Cigg

class DeleteCiggUseCase(
        private val repository: CiggsRepositoryImpl
) {
    suspend operator fun invoke(cigg: Cigg){
        return repository.deleteCigg(cigg.toDto())
    }
}