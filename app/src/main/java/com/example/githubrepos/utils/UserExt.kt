package com.example.githubrepos.utils

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

fun FirebaseUser.isFacebookSignIn(): Boolean =
    this.providerData.any { it.providerId == FacebookAuthProvider.PROVIDER_ID }

fun FirebaseUser.isGoogleSignIn(): Boolean =
    this.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }
