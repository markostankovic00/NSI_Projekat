package com.nsi_projekat.ui.screens.main.companyinfo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi_projekat.models.CompanyInfo
import com.nsi_projekat.models.IntradayInfo
import com.nsi_projekat.repository.interactors.AuthInteractor
import com.nsi_projekat.repository.interactors.InvestmentsInteractor
import com.nsi_projekat.repository.interactors.StockInteractor
import com.nsi_projekat.repository.interactors.UsersDataInteractor
import com.nsi_projekat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val stockRepository: StockInteractor,
    private val investmentsRepository: InvestmentsInteractor,
    private val authRepository: AuthInteractor,
    private val userDataRepository: UsersDataInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val stockInfoList = mutableStateOf(emptyList<IntradayInfo>())

    val company = mutableStateOf<CompanyInfo?>(null)

    val isInvestDialogVisible = mutableStateOf(false)
    val isNoCashDialogVisible = mutableStateOf(false)

    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {

            val symbol = savedStateHandle.get<String>("companySymbol") ?: return@launch

            loadInfo(symbol)
        }
    }

    fun loadInfo(symbol: String) = viewModelScope.launch {

        isLoading.value = true

        val companyInfoResult = async { stockRepository.getCompanyInfo(symbol) }
        val intradayInfoResult = async { stockRepository.getIntradayInfo(symbol) }

        when (val result = companyInfoResult.await()) {

            is Resource.Success -> {
                company.value = result.data
                isLoading.value = false
                isError.value = null
            }

            is Resource.Error -> {
                isLoading.value = false
                isError.value = result.message
            }

            is Resource.Loading -> {
                isLoading.value = true
            }
        }

        when (val result = intradayInfoResult.await()) {

            is Resource.Success -> {

                stockInfoList.value = result.data ?: emptyList()
                isLoading.value = false
                isError.value = null
            }

            is Resource.Error -> {
                isLoading.value = false
                isError.value = result.message
            }

            is Resource.Loading -> {
                isLoading.value = true
            }
        }
    }

    fun onInvestButtonClick() {

        isInvestDialogVisible.value = true
    }

    fun onConfirmInvestDialogClick(amount: Double?) = viewModelScope.launch {

        amount?.let {

            try {

                val userId = authRepository.getUserId()

                userDataRepository.getUserData(
                    userId = userId,
                    onError = { makeGenericErrorToast() }
                ) { userData ->

                    if ((userData?.cash ?: 0.0) < amount) {

                        isInvestDialogVisible.value = false
                        isNoCashDialogVisible.value = true

                    } else {

                        investmentsRepository.addInvestment(
                            userId = userId,
                            cash = it,
                            companyName = company.value?.name ?: "",
                            companySymbol = company.value?.symbol ?: ""
                        ) { isSuccessful ->

                            if (isSuccessful)

                                userDataRepository.decreaseUserCash(userId, amount) { result ->

                                    if (result) {

                                        makeSuccessfulInvestmentToast()

                                    } else {

                                        makeGenericErrorToast()
                                    }
                                }

                            else
                                makeGenericErrorToast()
                        }
                    }
                }

            }  catch (e: Exception) {

                makeGenericErrorToast()
                e.printStackTrace()
            }
        }
    }

    fun dismissInvestDialog() {

        isInvestDialogVisible.value = false
    }

    fun dismissNoCashDialog() {

        isNoCashDialogVisible.value = false
    }

    //region Events Helpers

    private fun makeSuccessfulInvestmentToast() = viewModelScope.launch {
        events.emit(Events.MakeSuccessfulInvestmentToast)
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateBack: Events()
        object MakeSuccessfulInvestmentToast: Events()
        object MakeGenericErrorToast: Events()
    }
}