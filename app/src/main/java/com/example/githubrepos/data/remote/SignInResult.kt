package com.example.githubrepos.data.remote

import com.example.githubrepos.domain.model.UserData


data class SignInResult(
    val data: UserData?,
    val errorMessage: String? = null
)
