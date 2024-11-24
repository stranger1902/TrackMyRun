package com.example.trackmyrun.core.domain.model

import com.example.trackmyrun.core.domain.interfaces.IKey
import androidx.compose.runtime.Immutable

@Immutable
data class RunModel(

    override val id: String,

    val distanceMeters: Float,
    val startTimestamp: Long,
    val durationMillis: Long,
    val avgSpeedMs: Float,
    val kcalBurned: Float
): IKey<String>