package com.nsi_projekat.repository.implementations

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nsi_projekat.models.Investment
import com.nsi_projekat.repository.constants.INVESTMENTS_COLLECTION_REF
import com.nsi_projekat.repository.interactors.InvestmentsInteractor
import com.nsi_projekat.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InvestmentsRepository: InvestmentsInteractor {

    private val investmentsDataRef: CollectionReference =
        Firebase.firestore.collection(INVESTMENTS_COLLECTION_REF)

    override fun getAllInvestments(
        userId: String
    ): Flow<Resource<List<Investment>>> = callbackFlow {

        var snapshotStateListener: ListenerRegistration? = null

        try {

            snapshotStateListener = investmentsDataRef
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->

                    val response = if (snapshot != null) {

                        val userData = snapshot.toObjects(Investment::class.java)
                        Resource.Success(data = userData)

                    } else {

                        Resource.Error(message = e?.message ?: "Something went wrong")
                    }

                    trySend(response)
                }

        } catch (e:Exception) {

            trySend(Resource.Error(e.message ?: "Something went wrong"))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    override fun addInvestment(
        userId: String,
        cash: Double,
        companyName: String,
        companySymbol: String,
        onComplete: (Boolean) -> Unit
    ) {

        val documentId = investmentsDataRef.document().id

        val investment = Investment(
            userId = userId,
            cash = cash,
            companyName = companyName,
            companySymbol = companySymbol
        )

        investmentsDataRef
            .document(documentId)
            .set(investment)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }
}