package com.example.githubrepos.di

import android.content.Context
import com.example.githubrepos.data.repository.EmailPwAuthRepositoryImpl
import com.example.githubrepos.data.repository.GoogleAuthRepositoryImpl
import com.example.githubrepos.domain.repository.EmailPwAuthRepository
import com.example.githubrepos.domain.repository.GoogleAuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    fun provideSignInClient(@ApplicationContext context: Context) =
        Identity.getSignInClient(context)

    @Provides
    fun provideSignInRequest(@ApplicationContext context: Context): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("APP_ID")
                    .build()
            ).setAutoSelectEnabled(true)
            .build()
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideGoogleAuthRepository(impl: GoogleAuthRepositoryImpl): GoogleAuthRepository = impl

    @Provides
    fun provideEmailPwAuthRepository(impl: EmailPwAuthRepositoryImpl): EmailPwAuthRepository = impl

}