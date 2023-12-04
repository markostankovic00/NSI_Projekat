package com.nsi_projekat.ui.screens.main.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(

): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    sealed class Events {

    }
}