package com.nsi_projekat.repository.di

import com.nsi_projekat.csv.parsers.CSVParser
import com.nsi_projekat.models.CompanyListing
import com.nsi_projekat.models.IntradayInfo
import com.nsi_projekat.network.apis.StockApi
import com.nsi_projekat.repository.implementations.AuthRepository
import com.nsi_projekat.repository.implementations.InvestmentsRepository
import com.nsi_projekat.repository.implementations.StockRepository
import com.nsi_projekat.repository.implementations.UsersDataRepository
import com.nsi_projekat.repository.interactors.AuthInteractor
import com.nsi_projekat.repository.interactors.InvestmentsInteractor
import com.nsi_projekat.repository.interactors.StockInteractor
import com.nsi_projekat.repository.interactors.UsersDataInteractor
import com.nsi_projekat.storage.daos.StockDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class RepoModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthInteractor {
        return AuthRepository()
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(): UsersDataInteractor {
        return UsersDataRepository()
    }

    @Provides
    @Singleton
    fun provideInvestmentsRepository(): InvestmentsInteractor {
        return InvestmentsRepository()
    }

    @Provides
    @Singleton
    fun provideStockRepository(
        stockApi: StockApi,
        stockDao: StockDAO,
        companyListingsParser: CSVParser<CompanyListing>,
        intradayInfoParser: CSVParser<IntradayInfo>
    ): StockInteractor {
        return StockRepository(
            api = stockApi,
            dao = stockDao,
            companyListingsParser = companyListingsParser,
            intradayInfoParser = intradayInfoParser
        )
    }
}