package com.example.trackmyrun.core.domain.model

sealed class ResourceModel<T>(
    val error: String? = null,
    val data: T? = null
) {

    class Error<T>(data: T, error: String): ResourceModel<T>(data = data, error = error)

    class Success<T>(data: T): ResourceModel<T>(data = data)
}