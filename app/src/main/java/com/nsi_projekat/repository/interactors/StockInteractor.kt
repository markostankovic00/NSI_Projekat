package com.nsi_projekat.repository.interactors

import com.nsi_projekat.models.CompanyInfo
import com.nsi_projekat.models.CompanyListing
import com.nsi_projekat.models.IntradayInfo
import com.nsi_projekat.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockInteractor {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}