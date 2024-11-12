package com.example.trackmyrun.core.domain.model

import android.net.Uri

data class UserModel(
    val profilePicUri: Uri? = null,
    val name: String = "",
    val height: Int = 0,
    val weight: Int = 0
)