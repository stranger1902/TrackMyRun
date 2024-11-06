package com.example.trackmyrun.home.domain.repository

import com.example.trackmyrun.core.data.local.model.ResponsePagingModel
import com.example.trackmyrun.home.domain.model.RunModel

interface RunRepository {
    suspend fun getRuns(limit: Int, offset: Long?): ResponsePagingModel<RunModel>
    suspend fun insertRun(run: RunModel): Boolean
    suspend fun deleteRun(run: RunModel): Boolean
}