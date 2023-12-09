package com.nsi_projekat.network.di

import android.content.Context
import android.net.ConnectivityManager
import com.nsi_projekat.network.apis.StockApi
import com.nsi_projekat.network.config.ConnectivityInterceptor
import com.nsi_projekat.network.config.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {

    @Provides
    @Singleton
    @Named("retrofit")
    fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit {
        return retrofitBuilder.retrofit
    }

    @Provides
    fun providesRetrofitBuilder(
        connectivityInterceptor: ConnectivityInterceptor
    ): RetrofitBuilder {
        return RetrofitBuilder(connectivityInterceptor)
    }

    @Provides
    fun provideConnectivityInterceptor(connectivityManager: ConnectivityManager): ConnectivityInterceptor {
        return ConnectivityInterceptor(connectivityManager)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideStockApi(@Named("retrofit") retrofit: Retrofit): StockApi {
        return retrofit.create(StockApi::class.java)
    }
}