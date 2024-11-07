package com.example.trackmyrun.home.presentation

import com.example.trackmyrun.core.data.local.model.ResponseErrorModel
import com.example.trackmyrun.home.domain.repository.RunRepository
import com.example.trackmyrun.core.utils.DatabasePaginator
import com.example.trackmyrun.core.utils.FileImageManager
import com.example.trackmyrun.core.domain.model.RunModel
import com.example.trackmyrun.core.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import android.graphics.Bitmap
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fileImageManager: FileImageManager,
    private val runRepository: RunRepository,
    private val userManager: UserManager
): ViewModel() {

    val currentUser = userManager.currentUser

    private val _state = MutableStateFlow(HomeState(pageSize = 10))
    val state = _state.asStateFlow()

    private val errorChannel = Channel<ResponseErrorModel>()

    private val paginator = DatabasePaginator<Long, RunModel>(
        onRequest = { nextPage ->
            runRepository.getRuns(_state.value.pageSize, nextPage)
        },
        onSuccess = { newItems, _ ->
            _state.value = _state.value.copy(items = _state.value.items + newItems)
        },
        onEndReached = {
            _state.value = _state.value.copy(isEndReached = it)
        },
        isLoading = {
            _state.value = _state.value.copy(isLoading = it)
        },
        onNextKey = { newItems, oldKey ->
            newItems.size + oldKey
        },
        onError = {
            errorChannel.send(it)
        },
        initialKey = 0
    )

//    init {
//        viewModelScope.launch {
//            repeat(25) {
//                runRepository.insertRun(
//                    RunModel(
//                        startTimestamp = System.currentTimeMillis() + (it * 1000),
//                        durationMillis = it * 3600000L,
//                        distanceMeters = it * 1000f,
//                        avgSpeedMs = it.toFloat(),
//                        kcalBurned = it * 100f,
//                        id = it.toString()
//                    )
//                )
//            }
//        }
//    }

    fun loadNextPage() {
        viewModelScope.launch {
            paginator.loadNextPage()
        }
    }

    fun loadImage(filename: String): Deferred<Bitmap?> {
        return viewModelScope.async {
            fileImageManager.loadImage(filename)
        }
    }

}