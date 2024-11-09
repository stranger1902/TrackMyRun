package com.example.trackmyrun.on_boarding.presentation

import com.example.trackmyrun.on_boarding.domain.repository.OnBoardingRepository
import com.example.trackmyrun.core.domain.model.UserModel
import com.example.trackmyrun.core.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
    private val userManager: UserManager
): ViewModel() {

    val onBoardingPageData = onBoardingRepository.getData()

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val _currentUser = MutableStateFlow(UserModel())
    val currentUser = _currentUser.asStateFlow()

    fun navigateNextPage() {
        if (_currentPage.value < onBoardingPageData.size - 1) _currentPage.value += 1
    }

    fun navigatePreviousPage() {
        if (_currentPage.value > 0) _currentPage.value -= 1
    }

    fun saveUserInPreferences() = viewModelScope.launch {
        userManager.saveUserInPreferences(_currentUser.value)
    }

    fun checkUser(): Boolean = _currentUser.value.name != "" && _currentUser.value.weight != 0 && _currentUser.value.height != 0

    fun saveWeight(weight: Int) {
        _currentUser.value = _currentUser.value.copy(weight = weight)
    }

    fun saveHeight(height: Int) {
        _currentUser.value = _currentUser.value.copy(height = height)
    }

    fun saveName(name: String) {
        _currentUser.value = _currentUser.value.copy(name = name)
    }

}