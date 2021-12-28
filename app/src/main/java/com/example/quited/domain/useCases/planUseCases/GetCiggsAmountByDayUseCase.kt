package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.repository.CiggsRepositoryImpl
import com.example.quited.domain.model.DayCiggs
import com.example.quited.domain.model.DayStat
import com.example.quited.domain.model.Plan
import com.example.quited.presentation.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GetCiggsAmountByDayUseCase(
        private val repository: CiggsRepositoryImpl
) {
    operator fun invoke(plan: Plan?, date: Date): Flow<DayStat> {
        return repository.getCiggsAmountByDay(date.dateLong).map { day ->
                DayStat(
                        date = date,
                        maxAmount = plan?.getMaxAmount(date) ?: 0,
                        ciggsAmount = day?.ciggsAmount ?: 0
                )
            }
        }
    }