package com.nsi_projekat.ui.screens.account.splash

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nsi_projekat.R
import com.nsi_projekat.ui.activities.MainActivity
import com.nsi_projekat.ui.navigation.Routes
import com.nsi_projekat.ui.screens.account.splash.SplashScreenViewModel.Events
import com.nsi_projekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.nsi_projekat.util.findActivity
import kotlinx.coroutines.delay

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

    val interactionSource = remember { MutableInteractionSource() }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bull_lottie))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(key1 = Unit) {

        delay(1800)

        viewModel.onEndOfAnimation()
    }

    BoxWithBackgroundPattern(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                viewModel.onEndOfAnimation()
            }
    ) {

        LottieAnimation(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center),
            composition = composition,
            progress = { progress }
        )
    }
}

@Composable
fun EventsHandler(
    navController: NavHostController,
    viewModel: SplashScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {
            Events.NavigateToOnBoarding -> {
                navController.popBackStack()
                navController.navigate(Routes.ON_BOARDING_SCREEN)
            }

            Events.NavigateToHome -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity()?.finish()
            }

            else -> {}
        }
    }
}

