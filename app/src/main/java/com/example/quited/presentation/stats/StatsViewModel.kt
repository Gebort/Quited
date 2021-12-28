package com.example.quited.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quited.data.util.MyApp
import com.example.quited.domain.model.Plan
import com.example.quited.domain.useCases.planUseCases.CiggsUseCases
import com.example.quited.presentation.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StatsViewModel: ViewModel() {

    private val ciggsUseCases = CiggsUseCases()

    private var _state = MutableStateFlow(StatsState())
    val state get() = _state.asStateFlow()

    private var _uiEvent = MutableSharedFlow<StatsUiEvent>()
    val uiEvent get() = _uiEvent.asSharedFlow()

    private var planJob: Job? = null
    private var statJob: Job? = null

    init {
        getPlan()
    }

    private fun getPlan() {
        planJob?.cancel()
        planJob = viewModelScope.launch {
            _state.value = state.value.copy(
                    loading = true
            )
            ciggsUseCases.getPlan().collect { plan ->
                _state.value = state.value.copy(
                        loading = false,
                        planSet = plan != null
                )
                plan?.let { getStat(plan) }
            }

        }
    }

    private fun getStat(plan: Plan){
        statJob?.cancel()
        statJob = viewModelScope.launch {
            ciggsUseCases.getCiggsAmountByDaysAfter(plan).collect { days ->
                _state.value = state.value.copy(
                        days = days
                )
            }
        }
    }
}