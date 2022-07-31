package com.example.quited.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quited.data.util.MyApp
import com.example.quited.domain.model.Plan
import com.example.quited.domain.useCases.planUseCases.PlanUseCases
import com.example.quited.presentation.util.Date
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettinsViewModel: ViewModel() {

    private val planUseCases = PlanUseCases()

    private var _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private var _uiEvents = MutableSharedFlow<SettingsUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private var planJob: Job? = null

    init {
        getPlan()
    }

    private fun getPlan() {
        planJob?.cancel()
        planJob = viewModelScope.launch {
            _state.value = state.value.copy(
                    loading = true
            )
            planUseCases.getPlan().collect { plan ->
                if (plan != null) {
                    val startDate = plan.startDate
                    val endDate = plan.endDate
                    val daysAmount = endDate.daysDiff(startDate)
                    _state.value = SettingsState(
                            loading = false,
                            daysAmount = daysAmount,
                            startDate = startDate,
                            startTime = plan.startTime,
                            endTime = plan.endTime,
                            startAmount = plan.startAmount,
                            endAmount = plan.endAmount,
                            notifications = plan.notifications
                    )
                }
                else {
                    _state.value = SettingsState(loading = false)
            }
        }
        }
    }

    private fun setPlan() {
        viewModelScope.launch {
            _state.value = state.value.copy(
                    loading = true
            )
            val endDate = state.value.startDate.plusDays(state.value.daysAmount)
            val plan = Plan(
                    startAmount = state.value.startAmount,
                    endAmount = state.value.endAmount,
                    startDate = state.value.startDate,
                    startTime = state.value.startTime,
                    endTime = state.value.endTime,
                    endDate = endDate,
                    notifications = state.value.notifications
            )
            planUseCases.setPlan(plan)

            _state.value = state.value.copy(
                    loading = false
            )
            _uiEvents.emit(SettingsUiEvent.PlanSaved)
        }
    }

        fun onEvent(event: SettingsEvent) {
            when (event) {
                is SettingsEvent.EnteredDaysAmount -> {
                    _state.value = state.value.copy(
                            daysAmount = event.daysAmount
                    )
                }
                is SettingsEvent.EnteredEndAmount -> {
                    _state.value = state.value.copy(
                            endAmount = event.endAmount
                    )
                }
                is SettingsEvent.EnteredStartAmount -> {
                    _state.value = state.value.copy(
                            startAmount = event.startAmount
                    )
                }
                is SettingsEvent.EnteredStartDate -> {
                    _state.value = state.value.copy(
                            startDate = event.startDate
                    )
                }
                is SettingsEvent.EnteredStartTime -> {
                    _state.value = state.value.copy(
                            startTime = event.startTime
                    )
                }
                is SettingsEvent.EnteredEndTime -> {
                    _state.value = state.value.copy(
                            endTime = event.endTime
                    )
                }
                is SettingsEvent.SwitchNotification -> {
                    _state.value = state.value.copy(
                            notifications = !state.value.notifications
                    )
                }
                is SettingsEvent.Save -> {
                    setPlan()
                }
            }
        }
    }