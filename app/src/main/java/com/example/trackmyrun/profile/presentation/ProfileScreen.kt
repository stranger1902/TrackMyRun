package com.example.trackmyrun.profile.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.derivedStateOf
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import android.content.Intent

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onFloatingButtonClick: () ->Unit
) {

    val viewModel = hiltViewModel<ProfileViewModel>()

    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    val context = LocalContext.current

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

    val photoPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
            it?.let { uri ->
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION )
                viewModel.saveProfilePic(uri)
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
    ) { innerPadding ->

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            val defaultImage = rememberVectorPainter(
                image = Icons.Default.AccountCircle
            )

            if (user.profilePicUri != null)
                AsyncImage(
                    error = rememberVectorPainter(image = Icons.Default.Clear),
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop,
                    model = user.profilePicUri,
                    placeholder = defaultImage,
                    onError = {
                        it.result.throwable.printStackTrace()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(160.dp)
                        .clickable {
                            photoPickerLauncher.launch(arrayOf("image/*"))
                        }
                )
            else
                Image(
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop,
                    painter = defaultImage,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(160.dp)
                        .clickable {
                            photoPickerLauncher.launch(arrayOf("image/*"))
                        }
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

//            Text(
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Start,
//                text = "lista amici",
//                fontSize = 24.sp,
//                modifier = Modifier
//                    .fillMaxWidth()
//            )

//            Spacer(modifier = Modifier.height(16.dp))

//            PullToRefreshLazyColumn(
//                lazyListState = lazyListState,
//                isRefreshing = isRefreshing,
//                items = state.items,
//                onRefresh = { },
//                itemContent = { item ->
//
//                },
//                emptyContent = {
//                    Image(
//                        painter = painterResource(R.drawable.empty_list),
//                        contentDescription = "empty list",
//                        modifier = Modifier
//                            .fillMaxSize()
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxSize()
//            )

        }
    }
}