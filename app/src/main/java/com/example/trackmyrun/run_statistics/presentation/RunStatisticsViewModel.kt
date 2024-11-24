package com.example.trackmyrun.run_statistics.presentation

import com.example.trackmyrun.run_statistics.domain.repository.RunStatisticsRepository
import com.example.trackmyrun.core.domain.model.RunStatisticsModel
import com.example.trackmyrun.core.domain.model.RunKcalBurnedModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class RunStatisticsViewModel @Inject constructor(
    private val runStatisticsRepository: RunStatisticsRepository
): ViewModel() {

    fun getStatistics(): Deferred<RunStatisticsModel?> {
        return viewModelScope.async {
            runStatisticsRepository.getStatistics()
        }
    }

    fun getRunsKcalburned(): Deferred<List<RunKcalBurnedModel>> {
        return viewModelScope.async {
            runStatisticsRepository.getRunsKcalburned()
        }
    }

}