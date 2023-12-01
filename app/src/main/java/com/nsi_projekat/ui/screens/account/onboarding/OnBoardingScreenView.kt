package com.nsi_projekat.ui.screens.account.onboarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nsi_projekat.R
import com.nsi_projekat.ui.navigation.Routes
import com.nsi_projekat.ui.screens.account.onboarding.OnBoardingScreenViewModel.Events
import com.nsi_projekat.ui.theme.spacing
import com.nsi_projekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.nsi_projekat.ui.uiutil.composables.LogoImage
import com.nsi_projekat.ui.uiutil.composables.PrimaryButton

@ExperimentalAnimationApi
@Composable
fun OnBoardingScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<OnBoardingScreenViewModel>()

    EventsHandler(navController, viewModel)

    OnBoardingScreenView(viewModel)
}

@Composable
fun OnBoardingScreenView(
    viewModel: OnBoardingScreenViewModel
) {

    BoxWithBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LogoImageWithSlogan()

            NavigationButtonsRow(viewModel)
        }
    }
}

@Composable
private fun ColumnScope.LogoImageWithSlogan() {

    Column(
        modifier = Modifier.weight(2f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LogoImage()

        //TODO Dodaj slogan ili ime aplikacije
    }
}

@Composable
private fun ColumnScope.NavigationButtonsRow(
    viewModel: OnBoardingScreenViewModel
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {

        PrimaryButton(
            modifier = Modifier
                .height(57.dp)
                .weight(1f)
                .padding(
                    start = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.medium
                ),
            text = stringResource(id = R.string.on_boarding_screen_log_in_button),
            onClick = viewModel::navigateToLogIn
        )

        PrimaryButton(
            modifier = Modifier
                .height(57.dp)
                .weight(1f)
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.large
                ),
            text = stringResource(id = R.string.on_boarding_screen_sign_up_button),
            onClick = viewModel::navigateToSignUp
        )
    }
}

@Composable
fun EventsHandler(
    navController: NavHostController,
    viewModel: OnBoardingScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            Events.NavigateToLogIn -> {
                navController.navigate(Routes.LOG_IN_SCREEN)
            }
            Events.NavigateToSignUp -> {
                navController.navigate(Routes.SIGN_UP_SCREEN)
            }
            else -> {}
        }
    }
}