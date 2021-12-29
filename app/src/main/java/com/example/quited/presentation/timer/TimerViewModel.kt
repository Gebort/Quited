package com.example.quited.presentation.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quited.domain.model.Cigg
import com.example.quited.domain.model.Plan
import com.example.quited.domain.useCases.planUseCases.CiggsUseCases
import com.example.quited.presentation.util.Date
import com.example.quited.presentation.util.Duration
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimerViewModel: ViewModel() {

    private val ciggsUseCases = CiggsUseCases()

    private var _state = MutableStateFlow(TimerState())
    val state get() = _state

    private var _uiEvent = MutableSharedFlow<TimerUiEvent>()
    val uiEvent get() = _uiEvent

    private var lastInsertedCigg: Cigg? = null
    private var plan: Plan? = null

    private var planJob: Job? = null
    private var statJob: Job? = null
    private var timeJob: Job? = null
    private var dayJob: Job? = null
    private var lastCiggJob: Job? = null

    init {
        getPlan()
    }

    private fun getPlan(){
        planJob?.cancel()
        planJob = viewModelScope.launch {
            _state.value = state.value.copy(
                    loading = true
            )
            ciggsUseCases.getPlan().collect { planNew ->
                plan = planNew
                _state.value = state.value.copy(
                        loading = false,
                        planSet = planNew != null
                )
                getStat(planNew)
            }

        }
    }

    private fun getStat(plan: Plan?){
        statJob?.cancel()
        statJob = viewModelScope.launch {
            ciggsUseCases.getCiggsAmountByDay(plan, Date.fromUTC(System.currentTimeMillis())).collect {  dayStat ->
                _state.value = state.value.copy(
                        dayStat = dayStat
                )
                if (plan != null) {
                    val date = state.value.dayStat.date
                    if (dayStat.ciggsAmount < dayStat.maxAmount) {
                        if (date.timeLong < plan.endTime.timeLong) {
                            if (date.timeLong > plan.startTime.timeLong){
                                //Задержка с последней сигареты
                                setLastCiggDelay()
                            }
                        }

                    } else {
                        //Задержка до следующего дняра
                        setNextDayDelay()
                    }
                }
            }
        }
    }

    private fun setLastCiggDelay() {
        lastCiggJob?.cancel()
        lastCiggJob = viewModelScope.launch {
            val lastTime = ciggsUseCases.getLastCiggUseCase() ?: 0L
            plan?.let {
                val now = Date.fromUTC(System.currentTimeMillis())
                val delay = now.datestamp - lastTime
                val planDelay = plan!!.getTimeDelay(now)
                _state.value = state.value.copy(
                        maxDuration = Duration.fromLocal(planDelay)
                )
                val differ = planDelay - delay
                setTimer(if (differ >= 0L) differ else 0L)
            }
        }
    }

    private fun setNextDayDelay() {
        lastCiggJob?.cancel()
        lastCiggJob = viewModelScope.launch {
            val lastTime = ciggsUseCases.getLastCiggUseCase() ?: 0L
            val now = Date.fromUTC(System.currentTimeMillis())
            val tomorrow =
                    if (now.timeLong > plan!!.startTime.timeLong) now.plusDays(1).dateLong + plan!!.startTime.timeLong
                    else now.dateLong + plan!!.startTime.timeLong
            _state.value = state.value.copy(
                    maxDuration = Duration.fromLocal(tomorrow - lastTime)
                    )
            setTimer(tomorrow - now.datestamp)
        }
    }

    private fun setTimer(delay: Long) {
        timeJob?.cancel()
        if (delay == 0L) {
            _state.value = state.value.copy(
                    duration = Duration.fromLocal(0)
            )
        }
        else {
            timeJob = viewModelScope.launch {
                val endTime = System.currentTimeMillis() + delay
                ciggsUseCases.getTimeUseCase().collect { timeLong ->
                    val countdown = if (timeLong >= endTime) 0L else endTime - timeLong
                    _state.value = state.value.copy(
                            duration = Duration.fromLocal(countdown)
                    )
                    if (countdown == 0L) {
                        getPlan()
                        timeJob?.cancel()
                    }
                }
            }
        }
    }

    fun onEvent(event: TimerEvent){
        when(event){
            is TimerEvent.saveCigg -> {
                viewModelScope.launch {
                    lastInsertedCigg = Cigg()
                    val id = ciggsUseCases.insertCigg(lastInsertedCigg!!)
                    lastInsertedCigg = lastInsertedCigg!!.copy(
                            id = id.toInt()
                    )
                    _uiEvent.emit(TimerUiEvent.CiggSaved)
                }
            }
            is TimerEvent.undoCigg -> {
                viewModelScope.launch {
                    lastInsertedCigg?.let {
                        ciggsUseCases.deleteCigg(lastInsertedCigg!!)
                        lastInsertedCigg = null
                    }
                }
            }
        }
    }
}