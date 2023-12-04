package com.nsi_projekat.repository.implementations

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.nsi_projekat.models.UserData
import com.nsi_projekat.repository.interactors.UsersDataRepositoryInteractor

const val USERS_DATA_COLLECTION_REF = "users_data"

const val USER_IMAGES_STORAGE_REF = "users_images"

class UsersDataRepository: UsersDataRepositoryInteractor {

    private val usersDataRef: CollectionReference =
        Firebase.firestore.collection(USERS_DATA_COLLECTION_REF)

    private val usersImagesRef: StorageReference =
        Firebase.storage.reference.child(USER_IMAGES_STORAGE_REF)


    override fun getUserData(
        userId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (UserData?) -> Unit
    ) {

        usersDataRef
            .document(userId)
            .get()
            .addOnSuccessListener { onSuccess.invoke(it.toObject(UserData::class.java)) }
            .addOnFailureListener { onError.invoke(it.cause) }
    }

    override fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String,
        cash: Double,
        onComplete: (Boolean) -> Unit
    ) {

        val userData = UserData(
            userId = userId,
            name = name,
            surname = surname,
            email = email,
            cash = cash
        )

        usersDataRef
            .document(userId)
            .set(userData)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun increaseUserCash(
        userId: String,
        amount: Double,
        onComplete: (Boolean) -> Unit
    ) {

        usersDataRef.document(userId)
            .update("cash", FieldValue.increment(amount))
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun decreaseUserCash(
        userId: String,
        amount: Double,
        onComplete: (Boolean) -> Unit
    ) {

        usersDataRef.document(userId)
            .update("cash", FieldValue.increment(-amount))
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun uploadUserPhoto(
        userId: String,
        photoUri: Uri,
        onSuccess: () -> Unit,
        onError: (Throwable?) -> Unit
    ) {

        val imageRef = usersImagesRef.child(userId)

        imageRef.putFile(photoUri).addOnCompleteListener { result ->

            if (result.isSuccessful) {

                imageRef.downloadUrl.addOnSuccessListener { uri ->

                    usersDataRef.document(userId)
                        .update("photoUrl", uri.toString())
                        .addOnSuccessListener {
                            onSuccess.invoke()
                        }
                        .addOnFailureListener {
                            onError.invoke(it.cause)
                        }
                }

            } else {

                onError.invoke(result.exception?.cause)
            }
        }
    }
}