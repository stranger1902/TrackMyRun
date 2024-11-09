package com.example.trackmyrun.home.presentation

import com.example.trackmyrun.core.presentation.PullToRefreshLazyColumn
import com.example.trackmyrun.home.presentation.component.RunItem
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.trackmyrun.core.domain.model.RunModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmyrun.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToRunDetailScreen: (run: RunModel) -> Unit
) {

    val viewModel = hiltViewModel<HomeViewModel>()

    val user by viewModel.currentUser.collectAsStateWithLifecycle()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    val isEndScrollReached by remember {
        derivedStateOf {
            !lazyListState.canScrollForward
        }
    }

    val isRefreshing by remember {
        derivedStateOf {
            state.isLoading
        }
    }

    LaunchedEffect(isEndScrollReached, isRefreshing) {
        if (!state.isLoading && !state.isEndReached && isEndScrollReached)
            viewModel.loadNextPage()
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {

        Card(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Bentornato ${user.name}!",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .basicMarquee()
            )
        }

        PullToRefreshLazyColumn(
            lazyListState = lazyListState,
            isRefreshing = isRefreshing,
            items = state.items,
            onRefresh = { },
            itemContent = { item ->
                RunItem(
                    item = item,
                    onNavigateToRunDetailScreen = {
                        onNavigateToRunDetailScreen(item)
                    },
                    snapshot = {
                        viewModel.loadImage(item.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            emptyContent = {
                Image(
                    painter = painterResource(R.drawable.empty_list),
                    contentDescription = "empty list",
                    modifier = Modifier
                        .fillMaxSize()
                )
            },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}