package com.example.githubrepos.domain.model

import com.google.firebase.auth.FirebaseUser

data class UserData(
    val userId: String,
    val username: String?,
    val email: String?,
    val profilePictureUrl: String?
)

fun FirebaseUser.toUserData() = UserData(
    userId = uid,
    username = displayName,
    profilePictureUrl = photoUrl?.toString(),
    email = email
)

