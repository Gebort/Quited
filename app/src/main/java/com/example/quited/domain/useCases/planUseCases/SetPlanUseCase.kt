package com.example.quited.domain.useCases.planUseCases

import com.example.quited.domain.model.Plan
import com.example.quited.domain.repository.CiggsRepository

class SetPlanUseCase(
        private val repository: CiggsRepository
) {
    suspend operator fun invoke(plan: Plan) {
        return repository.setPlan(plan.toDto())
    }
}