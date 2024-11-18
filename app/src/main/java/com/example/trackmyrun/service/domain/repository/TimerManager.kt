package com.example.trackmyrun.service.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface TimerManager {

    val timeElapsedMillis: StateFlow<Long>

    fun startTimer()
    fun pauseTimer()
    fun stopTimer()
}