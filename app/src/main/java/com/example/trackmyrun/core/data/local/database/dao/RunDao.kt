package com.example.trackmyrun.core.data.local.database.dao

import com.example.trackmyrun.core.data.local.entity.RunEntity
import com.example.trackmyrun.core.domain.model.RunModel
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Dao

@Dao
interface RunDao {

    @Query("SELECT * FROM RUN ORDER BY startTimestamp DESC LIMIT :limit OFFSET :offset")
    fun getRuns(limit: Int, offset: Long?): List<RunEntity>

    @Query("SELECT * FROM RUN WHERE id = :runId LIMIT 1")
    fun getRun(runId: String): RunModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunEntity)

    @Delete
    suspend fun deleteRun(run: RunEntity)

}