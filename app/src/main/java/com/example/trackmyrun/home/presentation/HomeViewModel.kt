package com.example.trackmyrun.home.presentation

import com.example.trackmyrun.core.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userManager: UserManager
): ViewModel() {

    val currentUser = userManager.currentUser

}