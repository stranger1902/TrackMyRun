package com.example.trackmyrun.on_boarding.presentation

import com.example.trackmyrun.on_boarding.domain.repository.OnBoardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
): ViewModel() {

    val onBoardingPageData = onBoardingRepository.getData()

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    fun navigateNextPage() {
        if (_currentPage.value < onBoardingPageData.size - 1) _currentPage.value += 1
    }

    fun navigatePreviousPage() {
        if (_currentPage.value > 0) _currentPage.value -= 1
    }
}