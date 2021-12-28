package com.example.quited.domain.useCases.planUseCases

import com.example.quited.domain.model.Plan
import com.example.quited.domain.repository.CiggsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GetPlanUseCase(
        private val repository: CiggsRepository
) {
    operator fun invoke(): Flow<Plan?> {
        return repository.getPlan().map { planDto ->
            planDto?.toPlan()
        }
    }
}