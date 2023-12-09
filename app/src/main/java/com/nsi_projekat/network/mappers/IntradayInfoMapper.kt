package com.nsi_projekat.network.mappers

import com.nsi_projekat.models.IntradayInfo
import com.nsi_projekat.network.dtos.IntradayInfoDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntradayInfoDTO.toIntradayInfo(): IntradayInfo {

    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = timestamp?.let {
        LocalDateTime.parse(it, formatter)
    }

    return IntradayInfo(
        date = localDateTime ?: LocalDateTime.now(),
        close = close ?: 0.0
    )
}