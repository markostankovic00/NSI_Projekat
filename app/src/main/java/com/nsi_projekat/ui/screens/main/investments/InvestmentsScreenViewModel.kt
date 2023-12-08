package com.nsi_projekat.ui.screens.main.investments

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi_projekat.models.Investment
import com.nsi_projekat.repository.interactors.AuthRepositoryInteractor
import com.nsi_projekat.repository.interactors.InvestmentsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvestmentsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val investmentsRepository: InvestmentsInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _listOfInvestments = MutableStateFlow<List<Investment>>(emptyList())
    var listOfInvestments = _listOfInvestments.asStateFlow()

    var isLoading = mutableStateOf(false)

    init {

        try {

            val userId = authRepository.getUserId()
            loadInvestments(userId)

        } catch (e: Exception) {

            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun loadInvestments(userId: String) = viewModelScope.launch {

        isLoading.value = true

        investmentsRepository.getAllInvestments(userId).collect {

            _listOfInvestments.value = it.data ?: emptyList()
            isLoading.value = false
        }
    }

    //region Events Helpers

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {
        object MakeGenericErrorToast: Events()
    }
}