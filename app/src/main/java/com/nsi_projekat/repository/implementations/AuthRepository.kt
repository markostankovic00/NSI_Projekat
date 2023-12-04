package com.nsi_projekat.repository.implementations

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nsi_projekat.repository.interactors.AuthRepositoryInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository: AuthRepositoryInteractor {

    override val currentUser: FirebaseUser?
        get() = Firebase.auth.currentUser

    override fun hasUser(): Boolean = Firebase.auth.currentUser != null

    override fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    override suspend fun signUpUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {

        Firebase.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    onComplete.invoke(true)

                } else {

                    onComplete.invoke(false)
                }
            }
            .await()
    }

    override suspend fun logInUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {

        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    onComplete.invoke(true)

                } else {

                    onComplete.invoke(false)
                }
            }
            .await()
    }

    override fun logOutUser() = Firebase.auth.signOut()
}