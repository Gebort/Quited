package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.repository.CiggsRepositoryImpl

data class CiggsUseCases(
        val insertCigg: InsertCiggUseCase = InsertCiggUseCase(CiggsRepositoryImpl()),
        val deleteCigg: DeleteCiggUseCase = DeleteCiggUseCase(CiggsRepositoryImpl()),
        val getPlan: GetPlanUseCase = GetPlanUseCase(CiggsRepositoryImpl()),
        val getCiggsAmountByDay: GetCiggsAmountByDayUseCase = GetCiggsAmountByDayUseCase(CiggsRepositoryImpl()),
        val getCiggsAmountByDaysAfter: GetCiggsAmountByDaysAfterUseCase = GetCiggsAmountByDaysAfterUseCase(CiggsRepositoryImpl()),
        val getCountdownUseCase: GetCountdownUseCase = GetCountdownUseCase(CiggsRepositoryImpl())
) {
}