package com.example.githubrepos.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubrepos.data.remote.SignInResult
import com.example.githubrepos.domain.Result
import com.example.githubrepos.domain.repository.EmailPwAuthRepository
import com.example.githubrepos.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val emailPwAuthRepository: EmailPwAuthRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun updateEmail(email: String) {
        _state.update {
            it.copy(
                email = email,
                isEmailError = isValidEmail(email).not()
            )
        }
    }

    fun updateName(name: String) {
        _state.update {
            it.copy(
                name = name,
                isNameError = isValidName(name).not()
            )
        }
    }

    fun updatePassword(password: String) {
        _state.update {
            it.copy(
                password = password,
                isPasswordError = isValidPassword(password).not()
            )
        }
    }

    fun signUpByEmailAndPw() {
        if (isDataNotValid()) {
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, signInError = null) }
            val result = emailPwAuthRepository.signUpWithEmailAndPassword(
                email = state.value.email,
                password = state.value.password
            )
            val signInResult = when (result) {
                is Result.Error -> {
                    SignInResult(data = null, result.message)
                }

                is Result.Success -> {
                    val currentUser = firebaseAuth.currentUser!!
                    val userData = userRepository.saveUserToFireStore(currentUser, state.value.name)
                    SignInResult(data = userData, null)
                }
            }
            onSignInResult(signInResult)
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return password.trim().isNotEmpty() && password.length >= 6
    }

    private fun isValidEmail(email: String): Boolean {
        return email.trim().isNotEmpty()
    }

    private fun isValidName(name: String): Boolean {
        return name.trim().isNotEmpty()
    }

    private fun isDataNotValid(): Boolean {
        val isPasswordError = isValidPassword(state.value.password).not()
        val isEmailError = isValidEmail(state.value.email).not()
        val isNameError = isValidName(state.value.name).not()
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isPasswordError = isPasswordError,
                    isEmailError = isEmailError,
                    isNameError = isNameError
                )
            }
        }
        return isPasswordError || isEmailError
    }

    private fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignUpSuccessful = result.data != null,
                signInError = result.errorMessage,
                isLoading = false
            )
        }
    }

    fun resetState() = _state.update { SignUpState() }


}