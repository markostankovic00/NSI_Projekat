package com.nsi_projekat.ui.screens.main.companyinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nsi_projekat.ui.uiutil.composables.BoxWithBackgroundPattern

@Composable
fun CompanyInfoScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<CompanyInfoScreenViewModel>()

    EventsHandler(navController, viewModel)

    CompanyInfoScreenView(viewModel)

    IsLoadingState(viewModel)
}

@Composable
private fun CompanyInfoScreenView(
    viewModel: CompanyInfoScreenViewModel
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Company Info Screen",
            style = MaterialTheme.typography.h2
        )
    }
}

@Composable
private fun IsLoadingState(
    viewModel: CompanyInfoScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading)
        BoxWithBackgroundPattern(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.onBackground
            )
        }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: CompanyInfoScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            else -> {}
        }
    }
}