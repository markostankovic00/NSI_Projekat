package com.nsi_projekat.ui.screens.account.splash

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@ExperimentalAnimationApi
@Composable
fun SplashScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<SplashScreenViewModel>()

    EventsHandler(navController, viewModel)
    SplashScreenView(viewModel)
}

@Composable
fun SplashScreenView(
    viewModel: SplashScreenViewModel
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Splash screen",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h2
        )
    }
}

@Composable
fun EventsHandler(
    navController: NavHostController,
    viewModel: SplashScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

    }
}

