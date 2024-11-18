package com.example.trackmyrun.service.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface CountdownManager {

    val isRunning: StateFlow<Boolean>
    val countdown: StateFlow<Int>

    fun startCountdown(initialValue: Int, onCountdownEnd: () -> Unit)
    fun skipCountdown(onSkipCountdown: () -> Unit)
    fun resetCountdown()
}