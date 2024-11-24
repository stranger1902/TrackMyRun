package com.example.trackmyrun.core.domain.model

import com.example.trackmyrun.core.domain.interfaces.IKey
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class FriendModel(

    override val id: String,

    val startTimestamp: Long,
    val name: String
): IKey<String>