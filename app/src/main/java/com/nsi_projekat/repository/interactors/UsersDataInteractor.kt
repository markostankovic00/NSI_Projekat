package com.nsi_projekat.repository.interactors

import android.net.Uri
import com.nsi_projekat.models.UserData

interface UsersDataInteractor {

    fun getUserData(
        userId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (UserData?) -> Unit
    )

    fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String,
        cash: Double,
        onComplete: (Boolean) -> Unit
    )

    fun increaseUserCash(
        userId: String,
        amount: Double,
        onComplete: (Boolean) -> Unit
    )

    fun decreaseUserCash(
        userId: String,
        amount: Double,
        onComplete: (Boolean) -> Unit
    )

    fun uploadUserPhoto(
        userId: String,
        photoUri: Uri,
        onSuccess: () -> Unit,
        onError: (Throwable?) -> Unit
    )
}