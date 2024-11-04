package com.example.githubrepos.ui.screens.registration

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isNameError: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val signInError: String? = null,
    val isLoading: Boolean = false
)
