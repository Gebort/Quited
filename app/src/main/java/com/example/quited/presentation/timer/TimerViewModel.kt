package com.example.quited.presentation.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quited.domain.model.Cigg
import com.example.quited.domain.model.Plan
import com.example.quited.domain.useCases.planUseCases.CiggsUseCases
import com.example.quited.presentation.util.Date
import com.example.quited.presentation.util.Duration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private var lastCiggJob: Job? = null

    init {
        getPlan()
    }

    fun getPlan(){
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

    fun getStat(plan: Plan?){
        statJob?.cancel()
        statJob = viewModelScope.launch {
            ciggsUseCases.getCiggsAmountByDay(plan, state.value.dayStat.date).collect {  dayStat ->
                _state.value = state.value.copy(
                        dayStat = dayStat
                )
                if (plan != null) {
                    if (dayStat.ciggsAmount < dayStat.maxAmount) {
                        //Задержка с последней сигареты
                        setLastCiggDelay()
                    } else {
                        //Задержка до следующего дня
                        setNextDayDelay()
                    }
                }
            }
        }
    }

    fun setLastCiggDelay() {
        lastCiggJob?.cancel()
        lastCiggJob = viewModelScope.launch {
            val lastTime = ciggsUseCases.getCountdownUseCase() ?: 0L
            plan?.let {
                val now = Date.fromUTC(System.currentTimeMillis())
                val delay = now.dateLong + now.timeLong - lastTime
                val planDelay = plan!!.getTimeDelay(Date.fromUTC(System.currentTimeMillis()))
                _state.value = state.value.copy(
                        maxDuration = Duration.fromLocal(planDelay)
                )
                val differ = planDelay - delay
                setTimer(if (differ >= 0L) differ else 0L)
            }
        }
    }

    fun setNextDayDelay() {
        lastCiggJob?.cancel()
        lastCiggJob = viewModelScope.launch {
            val lastTime = ciggsUseCases.getCountdownUseCase() ?: 0L
            val tomorrow = Date.fromUTC(System.currentTimeMillis()).plusDays(1).dateLong + plan!!.startTime.timeLong
            val now = Date.fromUTC(System.currentTimeMillis())
            _state.value = state.value.copy(
                    maxDuration = Duration.fromLocal(tomorrow - lastTime)
                    )
            setTimer(tomorrow - now.timeLong - now.dateLong)
        }
    }

    fun setTimer(delay: Long) {
        timeJob?.cancel()
        timeJob = flow {
            var count = delay
            val step = 1000L
            emit(count)
            while (count > 0) {
                delay(step)
                count -= step
                emit(count)
            }
        }
                .onEach { count ->
                    _state.value = state.value.copy(
                            duration = Duration.fromLocal(count),
                    )
                }
                .launchIn(viewModelScope)
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