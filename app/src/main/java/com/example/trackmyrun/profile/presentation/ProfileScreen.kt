package com.example.trackmyrun.profile.presentation

import com.example.trackmyrun.core.presentation.PullToRefreshLazyColumn
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmyrun.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onFloatingButtonClick: () ->Unit
) {

    val viewModel = hiltViewModel<ProfileViewModel>()

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

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFloatingButtonClick()
                }
            ) {
                Icon(
                    contentDescription = "add friend",
                    imageVector = Icons.Default.Add
                )
            }
        }
    ) {

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "profile image",
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .size(160.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Benvenuto ${user.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier
                    .basicMarquee()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "altezza: ${user.height} cm",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "peso: ${user.weight} Kg",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                text = "lista amici",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            PullToRefreshLazyColumn(
                lazyListState = lazyListState,
                isRefreshing = isRefreshing,
                items = state.items,
                onRefresh = { },
                itemContent = { item ->

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
}