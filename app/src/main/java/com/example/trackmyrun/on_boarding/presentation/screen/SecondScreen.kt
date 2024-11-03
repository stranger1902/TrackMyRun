package com.example.trackmyrun.on_boarding.presentation.screen

import com.example.trackmyrun.on_boarding.presentation.OnBoardingViewModel
import com.example.trackmyrun.on_boarding.data.local.OnBoardingModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SecondScreen(
    modifier: Modifier = Modifier,
    item: OnBoardingModel
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        val viewModel = hiltViewModel<OnBoardingViewModel>()

        val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

        Image(
            contentDescription = "second screen image",
            painter = painterResource(item.resImage),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5/4f)
        )

        Spacer(Modifier.height(40.dp))

        Text(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            text = item.title,
            fontSize = 36.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            textAlign = TextAlign.Start,
            text = item.description,
            lineHeight = 24.sp,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = currentUser.name,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = "nome")
            },
            onValueChange = {
                viewModel.saveName(it)
            }
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            OutlinedTextField(
                value = currentUser.height.toString(),
                singleLine = true,
                modifier = Modifier
                    .weight(1f),
                placeholder = {
                    Text(text = "altezza")
                },
                suffix = {
                    Text(text = "cm")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    viewModel.saveHeight(it.toInt())
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = currentUser.weight.toString(),
                singleLine = true,
                modifier = Modifier
                    .weight(1f),
                placeholder = {
                    Text(text = "peso")
                },
                suffix = {
                    Text(text = "Kg")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    viewModel.saveWeight(it.toInt())
                }
            )
        }
    }
}