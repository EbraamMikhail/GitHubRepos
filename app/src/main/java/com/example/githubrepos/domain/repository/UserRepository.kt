package com.example.githubrepos.domain.repository

import com.example.githubrepos.domain.model.UserData
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    suspend fun saveUserToFireStore(firebaseUser: FirebaseUser, username: String): UserData
    suspend fun getUserFromFireStore(firebaseUser: FirebaseUser): UserData
}