package com.nsi_projekat.storage.di

import android.content.Context
import androidx.room.Room
import com.nsi_projekat.storage.daos.StockDAO
import com.nsi_projekat.storage.database.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
open class StorageModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StockDatabase {

        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            "stockDatabase.db"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideStockDao(database: StockDatabase): StockDAO = database.dao
}