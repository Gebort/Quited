package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.repository.CiggsRepositoryImpl

data class PlanUseCases(
        val getPlan: GetPlanUseCase = GetPlanUseCase(CiggsRepositoryImpl()),
        val setPlan: SetPlanUseCase = SetPlanUseCase(CiggsRepositoryImpl()),
) {
}