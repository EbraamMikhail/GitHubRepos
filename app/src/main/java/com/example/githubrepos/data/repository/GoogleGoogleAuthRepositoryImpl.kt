package com.example.githubrepos.data.repository

import android.content.Intent
import android.content.IntentSender
import com.example.githubrepos.data.remote.SignInResult
import com.example.githubrepos.domain.model.toUserData
import com.example.githubrepos.domain.repository.GoogleAuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GoogleAuthRepositoryImpl @Inject constructor(
    private val signInRequest: BeginSignInRequest,
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth
) : GoogleAuthRepository {
    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (ex is CancellationException) throw ex
        }
    }

}