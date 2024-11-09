package com.example.trackmyrun.profile.presentation

import com.example.trackmyrun.profile.domain.repository.FriendRepository
import com.example.trackmyrun.core.data.local.model.ResponseErrorModel
import com.example.trackmyrun.core.domain.model.FriendModel
import com.example.trackmyrun.core.utils.DatabasePaginator
import com.example.trackmyrun.core.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val friendRepository: FriendRepository,
    private val userManager: UserManager
): ViewModel() {

    val currentUser = userManager.currentUser

    private val _state = MutableStateFlow(ProfileState(pageSize = 10))
    val state = _state.asStateFlow()

    private val errorChannel = Channel<ResponseErrorModel>()

    private val paginator = DatabasePaginator<Long, FriendModel>(
        onRequest = { nextPage ->
            friendRepository.getFriends(_state.value.pageSize, nextPage)
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

    init {
        viewModelScope.launch {
            loadNextPage()
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            paginator.loadNextPage()
        }
    }

}