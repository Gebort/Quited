package com.example.quited.domain.useCases.planUseCases

import com.example.quited.data.repository.CiggsRepositoryImpl
import com.example.quited.domain.model.DayStat
import com.example.quited.domain.model.Plan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCiggsAmountByDaysAfterUseCase(
        private val repository: CiggsRepositoryImpl,
) {

    operator fun invoke(plan: Plan): Flow<List<DayStat>> {
        return repository.getCiggsAmountByDaysAfter(plan.startDate.dateLong).map { days ->
                val daysCount = plan.endDate.daysDiff(plan.startDate)
                List(daysCount) { day ->
                    val date = plan.startDate.plusDays(day)
                    DayStat(
                            date = date,
                            maxAmount = plan.getMaxAmount(date),
                            ciggsAmount = days?.find { it.dateLong == date.dateLong }?.ciggsAmount ?: 0
                    )
                }
            }
        }
    }