package com.nsi_projekat.network.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IntradayInfoDTO(

    @SerialName("timestamp")
    val timestamp: String? = null,

    @SerialName("close")
    val close: Double? = null
)