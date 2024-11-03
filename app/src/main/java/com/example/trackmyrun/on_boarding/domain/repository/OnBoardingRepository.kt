package com.example.trackmyrun.on_boarding.domain.repository

import com.example.trackmyrun.on_boarding.data.local.OnBoardingModel

interface OnBoardingRepository {
    fun getData(): List<OnBoardingModel>
}