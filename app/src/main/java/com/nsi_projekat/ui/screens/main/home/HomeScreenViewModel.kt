package com.nsi_projekat.ui.screens.main.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi_projekat.models.CompanyListing
import com.nsi_projekat.repository.interactors.StockInteractor
import com.nsi_projekat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val stockRepository: StockInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val companies = mutableStateOf(emptyList<CompanyListing>())

    val isLoading = mutableStateOf(false)

    val isRefreshing = mutableStateOf(false)

    val searchQuery = mutableStateOf("")

    private var searchJob: Job? = null

    init {

        getCompanyListings()
    }

    fun onSearchQueryChange(query: String) {

        searchQuery.value = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getCompanyListings()
        }
    }

    fun onSwipeToRefresh() {
        getCompanyListings(fetchFromRemote = true)
    }

    fun onCompanyListingClick(company: CompanyListing) {
        navigateToCompanyInfoScreen(company.symbol)
    }

    private fun getCompanyListings(
        query: String = searchQuery.value.lowercase(),
        fetchFromRemote: Boolean = false
    ) = viewModelScope.launch {

        stockRepository
            .getCompanyListings(fetchFromRemote, query)
            .collect { result ->

                when (result) {

                    is Resource.Success -> handleGetCompanyListingsSuccess(result)
                    is Resource.Error   -> handleGetCompanyListingsError()
                    is Resource.Loading -> handleGetCompanyListingsLoading(result)
                }
            }
    }

    //region Get Company Listings Helpers

    private fun handleGetCompanyListingsSuccess(result: Resource.Success<List<CompanyListing>>) {

        result.data?.let { listings ->
            companies.value = listings
        }
    }

    private fun handleGetCompanyListingsError() {

        makeLoadingErrorToast()
    }

    private fun handleGetCompanyListingsLoading(result: Resource.Loading<List<CompanyListing>>) {

        isLoading.value = result.isLoading
    }

    //endregion

    //region Events Helpers

    private fun makeLoadingErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeLoadingErrorToast)
    }

    private fun navigateToCompanyInfoScreen(companySymbol: String) = viewModelScope.launch {
        events.emit(Events.NavigateToCompanyInfoScreen(companySymbol))
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        data class NavigateToCompanyInfoScreen(val companySymbol: String): Events()
        object MakeLoadingErrorToast: Events()
    }
}