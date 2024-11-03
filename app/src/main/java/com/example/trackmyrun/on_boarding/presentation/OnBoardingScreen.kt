package com.example.trackmyrun.on_boarding.presentation

import com.example.trackmyrun.on_boarding.presentation.component.PagerIndicator
import com.example.trackmyrun.on_boarding.presentation.screen.SecondScreen
import com.example.trackmyrun.on_boarding.presentation.screen.FinalScreen
import com.example.trackmyrun.on_boarding.presentation.screen.FirstScreen
import com.example.trackmyrun.on_boarding.presentation.screen.ThirdScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast

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

    Box(
        modifier = modifier
            .padding(16.dp)
    ) {

        HorizontalPager(
            userScrollEnabled = false,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->

            when(page) {

                0 -> FirstScreen(
                    item = viewModel.onBoardingPageData[page],
                    modifier = Modifier
                        .fillMaxSize()
                )

                1 -> SecondScreen(
                    item = viewModel.onBoardingPageData[page],
                    modifier = Modifier
                        .fillMaxSize()
                )

                2 -> ThirdScreen(
                    item = viewModel.onBoardingPageData[page],
                    modifier = Modifier
                        .fillMaxSize()
                )

                3 -> FinalScreen(
                    item = viewModel.onBoardingPageData[page],
                    modifier = Modifier
                        .fillMaxSize()
                )

                else -> throw RuntimeException("page $page is NOT valid")
            }
        }

        PagerIndicator(
            size = viewModel.onBoardingPageData.size,
            currentPage = currentPage,
            modifier = Modifier
                .offset(y = -ButtonDefaults.MinHeight / 2)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )

        Row(
            horizontalArrangement = if (currentPage != 0) Arrangement.SpaceBetween else Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
                            onBoardingCompleted()
                        }

                        1 -> {
                            if (!viewModel.checkUser())
                                Toast.makeText(context, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                            else {
                                viewModel.saveUserInPreferences()
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