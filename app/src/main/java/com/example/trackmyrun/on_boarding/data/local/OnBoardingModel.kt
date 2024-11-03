package com.example.trackmyrun.on_boarding.data.local

import androidx.annotation.DrawableRes

data class OnBoardingModel(
    @DrawableRes val resImage: Int,
    val description: String,
    val title: String
)