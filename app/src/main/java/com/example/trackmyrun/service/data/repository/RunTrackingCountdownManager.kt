package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.service.domain.repository.CountdownManager
import com.example.trackmyrun.core.utils.Constants
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job

class RunTrackingCountdownManager: CountdownManager {

    private val countdownScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _countdown = MutableStateFlow(Constants.RUN_COUNTDOWN_INITIAL_VALUE)
    private val _isRunning = MutableStateFlow(false)

    override val countdown
        get() = _countdown.asStateFlow()

    override val isRunning
        get() = _isRunning.asStateFlow()

    private var timerJob: Job? = null

    override fun startCountdown(initialValue: Int, onCountdownEnd: () -> Unit) {

        if (initialValue <= 0) throw RuntimeException("Start countdown value must be grater than 1s")

        _countdown.value = initialValue
        _isRunning.value = true

        timerJob = countdownScope.launch {

            try {

                while (_countdown.value > 0) {
                    delay(1000)
                    _countdown.value -= 1
                }

                _isRunning.value = false
                onCountdownEnd()
            }
            catch (e: CancellationException) {
                _countdown.value = Constants.RUN_COUNTDOWN_INITIAL_VALUE
                _isRunning.value = false
            }
        }
    }

    override fun resetCountdown() {
        _countdown.value = Constants.RUN_COUNTDOWN_INITIAL_VALUE
        _isRunning.value = false
        timerJob?.cancel()
        timerJob = null
    }

    override fun skipCountdown(onSkipCountdown: () -> Unit) {
        onSkipCountdown().also {
            _isRunning.value = false
            _countdown.value = 0
        }
    }

}