package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.service.domain.repository.TimerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job

class TrackingTimerManager: TimerManager {

    private val timerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _timeElapsedMillis = MutableStateFlow(0L)

    override val timeElapsedMillis
        get() = _timeElapsedMillis.asStateFlow()

    private var timerJob: Job? = null

    override fun startTimer() {
        timerJob = timerScope.launch {
            while (isActive) {
                delay(1000)
                _timeElapsedMillis.value += 1000
            }
        }
    }

    override fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun stopTimer() {
        _timeElapsedMillis.value = 0
        timerJob?.cancel()
        timerJob = null
    }

}