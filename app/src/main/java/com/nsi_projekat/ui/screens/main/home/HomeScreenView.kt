package com.nsi_projekat.ui.screens.main.home

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
import com.nsi_projekat.ui.theme.OnBackgroundColor

@Composable
fun HomeScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<HomeScreenViewModel>()

    EventsHandler(navController, viewModel)

    HomeScreenView(viewModel)
}

@Composable
private fun HomeScreenView(
    viewModel: HomeScreenViewModel
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Home Screen",
            color = OnBackgroundColor,
            style = MaterialTheme.typography.h2
        )
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            else -> {}
        }
    }
}