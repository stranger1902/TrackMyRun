package com.example.trackmyrun.home.domain.repository

import com.example.trackmyrun.core.domain.model.ResourceModel
import com.example.trackmyrun.core.domain.model.RunModel

interface RunRepository {
    suspend fun getRuns(limit: Int, offset: Long?): ResourceModel<List<RunModel>>
    suspend fun insertRun(run: RunModel): Boolean
    suspend fun deleteRun(run: RunModel): Boolean
}