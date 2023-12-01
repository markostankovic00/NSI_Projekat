package com.nsi_projekat.ui.screens.account.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(

): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val isLoggedIn = false

    fun onEndOfAnimation() = viewModelScope.launch {

        try {

            if (isLoggedIn)
                navigateToHome()
            else
                navigateToOnBoarding()

        } catch (e:Exception) {

            navigateToOnBoarding()
            e.printStackTrace()
        }
    }

    private fun navigateToHome() = viewModelScope.launch {
        events.emit(Events.NavigateToHome)
    }

    private fun navigateToOnBoarding() = viewModelScope.launch {
        events.emit(Events.NavigateToOnBoarding)
    }

    sealed class Events {
        object NavigateToOnBoarding: Events()
        object NavigateToHome: Events()
    }
}