package com.example.trackmyrun.core.utils

import com.example.trackmyrun.core.data.local.model.ResponsePagingModel
import com.example.trackmyrun.core.data.local.model.ResponseErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabasePaginator<Key, Item>(
    private inline val onRequest: suspend (nextKey: Key) -> ResponsePagingModel<Item>,
    private inline val onSuccess: suspend (newItems: List<Item>, newKey: Key) -> Unit,
    private inline val onNextKey: (newItems: List<Item>, oldKey: Key) -> Key,
    private inline val onError: suspend (e: ResponseErrorModel) -> Unit,
    private inline val onEndReached: (Boolean) -> Unit,
    private inline val isLoading: (Boolean) -> Unit,
    private val initialKey: Key
){

    private var currentKey = initialKey

    private var isMakingRequest = false
    private var isEndReached = false

    suspend fun loadNextPage() {

        if (isMakingRequest || isEndReached) return

        isMakingRequest = true
        isLoading(true)

        val result = withContext(Dispatchers.IO) {
            onRequest(currentKey)
        }

        isMakingRequest = false

        if (result.error != null) {
            onError(result.error)
            isLoading(false)
            return
        }

        val items = result.data!!

        isEndReached = items.isEmpty()

        currentKey = onNextKey(items, currentKey)

        onEndReached(isEndReached)

        withContext(Dispatchers.Main) {
            onSuccess(items, currentKey)
        }

        isLoading(false)
    }
}