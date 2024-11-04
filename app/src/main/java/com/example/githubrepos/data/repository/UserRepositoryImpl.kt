package com.example.githubrepos.data.repository

import com.example.githubrepos.domain.model.UserData
import com.example.githubrepos.domain.model.toUserData
import com.example.githubrepos.domain.repository.UserRepository
import com.example.githubrepos.utils.isFacebookSignIn
import com.example.githubrepos.utils.isGoogleSignIn
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun saveUserToFireStore(
        firebaseUser: FirebaseUser,
        username: String
    ): UserData {
        val map = hashMapOf(
            "email" to firebaseUser.email,
            "name" to username
        )
        val document = firestore.collection("USERS").document(firebaseUser.uid)
        document.set(map)
        return firebaseUser.toUserData().copy(username = username)
    }


    override suspend fun getUserFromFireStore(firebaseUser: FirebaseUser): UserData {
        return when {
            firebaseUser.isFacebookSignIn() -> {
                val accessToken = AccessToken.getCurrentAccessToken()?.token
                val photoUrl =
                    firebaseUser.photoUrl.toString() + "?access_token=${accessToken}"
                firebaseUser.toUserData().copy(profilePictureUrl = photoUrl)
            }

            firebaseUser.isGoogleSignIn() -> firebaseUser.toUserData()

            else -> {
                val name = getUserNameFromFireStore(firebaseUser)
                firebaseUser.toUserData().copy(username = name)
            }
        }

    }

    private suspend fun getUserNameFromFireStore(firebaseUser: FirebaseUser): String {
        val userDoc =
            firestore.collection("USERS").document(firebaseUser.uid).get().await()
        return userDoc.data?.getOrDefault("name", "") as String
    }

    companion object {
        private const val TAG = "UserRepositoryImplTAG"
    }
}