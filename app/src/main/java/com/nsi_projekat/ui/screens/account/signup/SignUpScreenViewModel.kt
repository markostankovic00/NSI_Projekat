package com.nsi_projekat.ui.screens.account.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(

): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val nameTextState = mutableStateOf("")

    val surnameTextState = mutableStateOf("")

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    val repeatPasswordTextState = mutableStateOf("")

    //region Form Update Helpers

    fun onNameTextChanged(name: String) {
        nameTextState.value = name
    }

    fun onSurnameTextChanged(surname: String) {
        surnameTextState.value = surname
    }

    fun onEmailTextChanged(email: String) {
        emailTextState.value = email
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    fun onRepeatPasswordTextChanged(repeatPassword: String) {
        repeatPasswordTextState.value = repeatPassword
    }

    //endregion

    fun onSignUpClick() = viewModelScope.launch {

        //TODO to be implemented
    }

    //region Event Helpers

    private fun makeSignUpErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeSignupErrorToast)
    }

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHomeScreen)
    }

    fun navigateToLogIn() = viewModelScope.launch {
        events.emit(Events.NavigateToLogIn)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToHomeScreen: Events()
        object NavigateToLogIn: Events()
        object MakeSignupErrorToast: Events()
    }
}