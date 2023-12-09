package com.nsi_projekat.repository.implementations

import com.nsi_projekat.csv.parsers.CSVParser
import com.nsi_projekat.models.CompanyInfo
import com.nsi_projekat.models.CompanyListing
import com.nsi_projekat.models.IntradayInfo
import com.nsi_projekat.network.apis.StockApi
import com.nsi_projekat.network.mappers.toCompanyInfo
import com.nsi_projekat.repository.interactors.StockInteractor
import com.nsi_projekat.storage.daos.StockDAO
import com.nsi_projekat.storage.mappers.toCompanyListing
import com.nsi_projekat.storage.mappers.toCompanyListingEntity
import com.nsi_projekat.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


class StockRepository @Inject constructor(
    private val api: StockApi,
    private val dao: StockDAO,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
): StockInteractor {


    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {

        return flow {

            emit(Resource.Loading(true))

            val localListings = dao.searchCompanyListings(query)

            emit(Resource.Success(localListings.map { it.toCompanyListing() }))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {

                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {

                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())

            } catch (e: IOException) {

                e.printStackTrace()
                emit(Resource.Error("Error loading company listings!"))
                null

            } catch (e: HttpException) {

                e.printStackTrace()
                emit(Resource.Error("Error loading company listings!"))
                null
            }

            remoteListings?.let { listings ->

                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )

                emit(Resource.Success(
                    data = dao
                        .searchCompanyListings("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {

        return try {

            val response = api.getIntradayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())

            Resource.Success(result)

        } catch (e: IOException) {

            e.printStackTrace()
            Resource.Error("Error loading intraday info!")

        } catch (e: HttpException) {

            e.printStackTrace()
            Resource.Error("Error loading intraday info!")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {

        return try {

            val result = api.getCompanyInfo(symbol)
            Timber.i("TAGA: $result")
            Resource.Success(result.toCompanyInfo())

        } catch (e: IOException) {

            e.printStackTrace()
            Resource.Error("Error loading company info!")

        } catch (e: HttpException) {

            e.printStackTrace()
            Resource.Error("Error loading company info!")
        }
    }
}