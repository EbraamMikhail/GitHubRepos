package com.example.githubrepos.data.repository

import com.example.githubrepos.domain.Result
import com.example.githubrepos.domain.repository.EmailPwAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmailPwAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
    ) : EmailPwAuthRepository {
    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Failed to sign in!")

        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            e.printStackTrace()
            Result.Error("User not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Failed to sign in!")
        }
    }

    override fun signOut() = auth.signOut()


}