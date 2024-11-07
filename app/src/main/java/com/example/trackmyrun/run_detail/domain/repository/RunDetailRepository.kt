package com.example.trackmyrun.run_detail.domain.repository

import com.example.trackmyrun.core.domain.model.RunModel

interface RunDetailRepository {
    suspend fun getDetail(runId: String): RunModel?
}