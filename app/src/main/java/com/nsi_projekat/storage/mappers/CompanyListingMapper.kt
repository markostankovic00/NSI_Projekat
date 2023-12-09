package com.nsi_projekat.storage.mappers

import com.nsi_projekat.models.CompanyListing
import com.nsi_projekat.storage.entities.CompanyListingEntity

fun CompanyListingEntity.toCompanyListing(): CompanyListing {

    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {

    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}