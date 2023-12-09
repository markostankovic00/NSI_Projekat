package com.nsi_projekat.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nsi_projekat.storage.daos.StockDAO
import com.nsi_projekat.storage.entities.CompanyListingEntity

@Database(
    entities = [CompanyListingEntity::class],
    version = 1
)
abstract class StockDatabase: RoomDatabase() {

    abstract val dao: StockDAO
}