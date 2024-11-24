package com.example.trackmyrun.core.data.local.model

data class ResponsePagingModel<Data>(
    val error: ResponseErrorModel?,
    val data: List<Data>?
)