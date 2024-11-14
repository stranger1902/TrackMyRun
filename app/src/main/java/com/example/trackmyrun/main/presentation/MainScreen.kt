package com.example.trackmyrun.main.presentation

import com.example.trackmyrun.main.navigation.registerBottomNavigationGraph
import com.example.trackmyrun.main.navigation.BottomNavigationGraph
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.compose.material3.TopAppBar
import androidx.navigation.compose.NavHost
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmyrun.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainGraphNavController: NavHostController
) {

    val viewModel = hiltViewModel<MainViewModel>()

    val navController = rememberNavController()

    var currentBottomNavigationItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        painter = painterResource(R.drawable.ic_launcher),
                        contentDescription = "app icon",
                        modifier = Modifier
                            .size(32.dp)
                    )
                },
                title = {
                    Text(
                        fontSize = 36.sp,
                        text = buildAnnotatedString {
                            append(" Track")
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic
                                )
                            ) {
                                append("MyRun")
                            }
                        }
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(64.dp)
            ) {
                viewModel.bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentBottomNavigationItem == index,
                        alwaysShowLabel = false,
                        onClick = {
                            currentBottomNavigationItem = index
                            navController.navigate(item.route) {
                                popUpTo<BottomNavigationGraph> {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentBottomNavigationItem == index) item.selectedIcon else item.unSelectedIcon,
                                contentDescription = "bottom navigation icon"
                            )
                        }
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->

        NavHost(
            startDestination = BottomNavigationGraph,
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            registerBottomNavigationGraph(mainGraphNavController)
        }
    }

}