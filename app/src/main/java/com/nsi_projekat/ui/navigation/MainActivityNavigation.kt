@file:OptIn(ExperimentalAnimationApi::class)

package com.nsi_projekat.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.nsi_projekat.R
import com.nsi_projekat.ui.activities.MainViewModel
import com.nsi_projekat.ui.activities.MainViewModel.Events
import com.nsi_projekat.ui.screens.main.home.HomeScreen
import com.nsi_projekat.ui.screens.main.investments.InvestmentsScreen
import com.nsi_projekat.ui.screens.main.profile.ProfileScreen
import com.nsi_projekat.ui.uiutil.composables.bottomnav.BottomNavBar
import com.nsi_projekat.ui.uiutil.composables.bottomnav.BottomNavItem

@Composable
fun MainActivityLayoutAndNavigation(
    viewModel: MainViewModel
) {

    val navController = rememberAnimatedNavController()

    NavHostAndBottomNavigation(navController, viewModel)

    EventsHandler(navController, viewModel)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun NavHostAndBottomNavigation(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavBarVisibilityState = rememberSaveable { viewModel.bottomNavBarVisibilityState }

    viewModel.setBottomNavBarVisibilityState(currentRoute)

    val bottomNavBarItems = listOf(
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_investments),
            route = Routes.INVESTMENTS_SCREEN,
            icon = Icons.Default.MonetizationOn
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_home),
            route = Routes.HOME_SCREEN,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_profile),
            route = Routes.PROFILE_SCREEN,
            icon = Icons.Default.Person
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                items = bottomNavBarItems,
                bottomBarState = bottomNavBarVisibilityState.value,
                onItemClick = { viewModel.onBottomNavItemClicked(it.route) }
            )
        }
    ) {

        AnimatedNavigation(navController)
    }
}

@Composable
private fun AnimatedNavigation(
    navController: NavHostController
) {

    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.HOME_SCREEN,
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
            route = Routes.HOME_SCREEN
        ) {
            HomeScreen(navController)
        }

        composable(
            route = Routes.PROFILE_SCREEN
        ) {
            ProfileScreen(navController)
        }

        composable(
            route = Routes.INVESTMENTS_SCREEN
        ) {
            InvestmentsScreen(navController)
        }
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            Events.NavigateToHome -> {
                navController.popBackStack()
                navController.navigate(Routes.HOME_SCREEN)
            }
            Events.NavigateToProfile -> {
                navController.popBackStack()
                navController.navigate(Routes.PROFILE_SCREEN)
            }
            Events.NavigateToInvestments -> {
                navController.popBackStack()
                navController.navigate(Routes.INVESTMENTS_SCREEN)
            }
            else -> {}
        }
    }
}