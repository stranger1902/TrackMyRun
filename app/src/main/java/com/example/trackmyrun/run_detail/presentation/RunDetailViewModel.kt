package com.example.trackmyrun.run_detail.presentation

import com.example.trackmyrun.run_detail.domain.repository.RunDetailRepository
import com.example.trackmyrun.core.utils.FileImageManager
import com.example.trackmyrun.core.domain.model.RunModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import android.graphics.Bitmap
import javax.inject.Inject

@HiltViewModel
class RunDetailViewModel @Inject constructor(
    private val runDetailRepository: RunDetailRepository,
    private val fileImageManager: FileImageManager
): ViewModel() {

    fun getRunDetail(runId: String): Deferred<RunModel?> {
        return viewModelScope.async {
            runDetailRepository.getDetail(runId)
        }
    }

    fun loadImage(filename: String): Deferred<Bitmap?> {
        return viewModelScope.async {
            fileImageManager.loadImage(filename)
        }
    }

}