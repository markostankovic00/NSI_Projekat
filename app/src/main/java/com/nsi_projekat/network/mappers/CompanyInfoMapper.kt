package com.nsi_projekat.network.mappers

import com.nsi_projekat.models.CompanyInfo
import com.nsi_projekat.network.dtos.CompanyInfoDTO

fun CompanyInfoDTO.toCompanyInfo(): CompanyInfo {

    return CompanyInfo(
        name = name ?: "",
        symbol = symbol ?: "",
        description = description ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}