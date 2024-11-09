package com.example.trackmyrun.home.presentation.component

import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <Item> PullToRefreshLazyColumn(
    modifier: Modifier = Modifier,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    lazyListState: LazyListState = rememberLazyListState(),
    enabled: Boolean = false,
    isRefreshing: Boolean,
    items: List<Item>,
    itemContent: @Composable (item: Item) -> Unit,
    emptyContent: @Composable () -> Unit,
    onRefresh: () -> Unit
) {

    Box (
        modifier = modifier
            .pullToRefresh(
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
                onRefresh = onRefresh,
                enabled = enabled
            )
    ) {

        if (!isRefreshing && items.isEmpty())
            emptyContent()
        else
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(items) { item ->
                    itemContent(item)
                }
            }

        PullToRefreshDefaults.Indicator(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }

}