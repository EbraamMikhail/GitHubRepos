package com.example.githubrepos.domain.repository


interface GoogleAuthRepository {
    suspend fun signOut()
}
