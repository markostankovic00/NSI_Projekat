package com.nsi_projekat.csv.di

import com.nsi_projekat.csv.parsers.CSVParser
import com.nsi_projekat.csv.parsers.CompanyListingsParser
import com.nsi_projekat.csv.parsers.IntradayInfoParser
import com.nsi_projekat.models.CompanyListing
import com.nsi_projekat.models.IntradayInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CSVModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>
}