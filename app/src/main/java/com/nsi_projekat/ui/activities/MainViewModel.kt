package com.nsi_projekat.ui.activities

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi_projekat.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val bottomNavBarVisibilityState = (mutableStateOf(false))

    fun setBottomNavBarVisibilityState(currentRoute: String?) = viewModelScope.launch {

        bottomNavBarVisibilityState.value = currentRoute in listOf(
            Routes.HOME_SCREEN,
            Routes.PROFILE_SCREEN,
            Routes.INVESTMENTS_SCREEN
        )
    }

    fun onBottomNavItemClicked(itemRoute: String) {

        when (itemRoute) {

            Routes.HOME_SCREEN        -> navigateToHome()
            Routes.PROFILE_SCREEN     -> navigateToProfile()
            Routes.INVESTMENTS_SCREEN -> navigateToInvestments()
        }
    }

    //region Event Helpers

    private fun navigateToHome() = viewModelScope.launch {

        events.emit(Events.NavigateToHome)
    }

    private fun navigateToProfile() = viewModelScope.launch {

        events.emit(Events.NavigateToProfile)
    }

    private fun navigateToInvestments() = viewModelScope.launch {

        events.emit(Events.NavigateToInvestments)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToHome: Events()
        object NavigateToProfile: Events()
        object NavigateToInvestments: Events()
    }
}