package com.nsi_projekat.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.nsi_projekat.ui.screens.account.splash.SplashScreen

@ExperimentalAnimationApi
@Composable
fun AccountActivityNavigation() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.SPLASH_SCREEN,
        enterTransition = {
            fadeIn(
                initialAlpha = 0.1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        }
    ) {

        composable(
            route = Routes.SPLASH_SCREEN
        ) {
            SplashScreen(navController)
        }
    }
}