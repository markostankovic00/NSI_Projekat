package com.nsi_projekat.repository.interactors

import com.nsi_projekat.models.Investment
import com.nsi_projekat.util.Resource
import kotlinx.coroutines.flow.Flow

interface InvestmentsInteractor {

    fun getAllInvestments(
        userId: String
    ): Flow<Resource<List<Investment>>>

    fun addInvestment(
        userId: String,
        cash: Double,
        companyName: String,
        companySymbol: String,
        onComplete: (Boolean) -> Unit
    )
}