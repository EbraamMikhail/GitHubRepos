package com.example.githubrepos.domain.repository

import com.example.githubrepos.domain.Result

interface EmailPwAuthRepository {
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<Boolean>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean>
    fun signOut()

}