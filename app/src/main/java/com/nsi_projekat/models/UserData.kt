package com.nsi_projekat.models

data class UserData(
    val userId: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val cash: Double = 0.0,
    val photoUrl: String = ""
)