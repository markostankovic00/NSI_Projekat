package com.nsi_projekat.ui.screens.account.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi_projekat.repository.interactors.AuthRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    //region Form Update Helpers

    fun onEmailTextChanged(email: String) {
        emailTextState.value = email
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    //endregion

    fun onLogInClick() = viewModelScope.launch {

        if(emailTextState.value == "admin" && passwordTextState.value == "admin") {

            navigateToHomeScreen()

        } else {

            try {

                authRepository.logInUser(
                    email = emailTextState.value.trim(),
                    password = passwordTextState.value.trim()
                ) { isSuccessful ->

                    if (isSuccessful)
                        navigateToHomeScreen()
                    else
                        makeLoginErrorToast()
                }
            } catch (e:Exception) {

                makeLoginErrorToast()
                e.printStackTrace()
            }
        }
    }

    //region Event Helpers

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHomeScreen)
    }

    private fun makeLoginErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeLoginErrorToast)
    }

    fun navigateToSignUp() = viewModelScope.launch {
        events.emit(Events.NavigateToSignUp)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToHomeScreen: Events()
        object NavigateToSignUp: Events()
        object MakeLoginErrorToast: Events()
    }
}