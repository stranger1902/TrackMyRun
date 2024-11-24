package com.example.trackmyrun.on_boarding.presentation

import com.example.trackmyrun.on_boarding.presentation.component.PagerIndicator
import com.example.trackmyrun.on_boarding.presentation.screen.SecondScreen
import com.example.trackmyrun.on_boarding.presentation.screen.FinalScreen
import com.example.trackmyrun.on_boarding.presentation.screen.FirstScreen
import com.example.trackmyrun.on_boarding.presentation.screen.ThirdScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import kotlin.uuid.ExperimentalUuidApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    onBoardingCompleted: () -> Unit
) {

    val context = LocalContext.current

    val viewModel = hiltViewModel<OnBoardingViewModel>()

    val currentPage by viewModel.currentPage.collectAsStateWithLifecycle()

    val pagerState = PagerState(
        currentPage = currentPage,
        pageCount = {
            viewModel.onBoardingPageData.size
        }
    )

    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            PagerIndicator(
                size = viewModel.onBoardingPageData.size,
                currentPage = currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            HorizontalPager(
                userScrollEnabled = false,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
            ) { page ->

                when(page) {

                    0 -> FirstScreen(
                        item = viewModel.onBoardingPageData[page],
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 16.dp)
                    )

                    1 -> SecondScreen(
                        item = viewModel.onBoardingPageData[page],
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 16.dp)
                    )

                    2 -> ThirdScreen(
                        item = viewModel.onBoardingPageData[page],
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 16.dp)
                    )

                    3 -> FinalScreen(
                        item = viewModel.onBoardingPageData[page],
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 16.dp)
                    )

                    else -> throw RuntimeException("page $page is NOT valid")
                }
            }

            Row(
                horizontalArrangement = if (currentPage != 0) Arrangement.SpaceBetween else Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                if (currentPage != 0)
                    OutlinedButton(
                        onClick = {
                            viewModel.navigatePreviousPage()
                        }
                    ) { Text(text = "Indietro") }

                Button(
                    onClick = {

                        when(currentPage) {

                            viewModel.onBoardingPageData.size - 1 -> {
                                viewModel.saveUserInPreferences(Uuid.random().toHexString())
                                onBoardingCompleted()
                            }

                            1 -> {
                                if (!viewModel.checkUser())
                                    Toast.makeText(context, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                                else {
                                    viewModel.navigateNextPage()
                                }
                            }

                            else -> viewModel.navigateNextPage()
                        }
                    }
                ) { Text(text = if (currentPage != viewModel.onBoardingPageData.size - 1) "Avanti" else "Iniziamo") }
            }
        }
    }
}