package com.example.trackmyrun.main.presentation

import com.example.trackmyrun.main.domain.repository.BottomNavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val bottomNavigationRepository: BottomNavigationRepository
): ViewModel() {

    val bottomNavigationItems = bottomNavigationRepository.getBottomNavigationItems()

}