package com.example.githubrepos.ui.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubrepos.data.remote.SignInResult
import com.example.githubrepos.ui.screens.home.HomeScreen
import com.example.githubrepos.ui.screens.home.HomeViewModel
import com.example.githubrepos.ui.screens.login.LoginScreen
import com.example.githubrepos.ui.screens.login.LoginViewModel
import com.example.githubrepos.ui.screens.registration.SignUpScreen
import com.example.githubrepos.ui.screens.registration.SignUpViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loginViewModel: LoginViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.name,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {

        composable(AppScreens.LoginScreen.name) {
            val state by loginViewModel.state.collectAsStateWithLifecycle()
            LaunchedEffect(key1 = Unit) {
                val userData = loginViewModel.getSignedInUser()
                if (userData != null) {
                    navController.navigate(
                        "${AppScreens.HomeScreen.name}?username=${userData.username ?: userData.email ?: ""}&profilePicture=${userData.profilePictureUrl ?: ""}"
                    ) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
            val callbackManager = remember { CallbackManager.Factory.create() }
            val loginManager = LoginManager.getInstance()
            val fbAuthLauncher = rememberLauncherForActivityResult(
                contract =
                loginManager.createLogInActivityResultContract(
                    callbackManager,
                    null
                )
            ) {}
            DisposableEffect(key1 = Unit) {
                loginManager.registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onCancel() {
                            val signInResult = SignInResult(
                                data = null,
                                errorMessage = "Cancelled"
                            )
                            loginViewModel.onSignInResult(signInResult)
                        }

                        override fun onError(error: FacebookException) {
                            val signInResult = SignInResult(
                                data = null,
                                errorMessage = error.message
                                    ?: "Failed to login"
                            )
                            loginViewModel.onSignInResult(signInResult)
                        }

                        override fun onSuccess(result: LoginResult) {
                            loginViewModel.signInByFacebook(result.accessToken)
                        }

                    })
                onDispose {
                    loginManager.unregisterCallback(callbackManager)
                }
            }
            if (state.isSignInSuccessful.not())
                LoginScreen(
                    state = state,
                    onEmailChanged = loginViewModel::updateEmail,
                    onPasswordChanged = loginViewModel::updatePassword,
                    onEmailPwSignInClicked = loginViewModel::signByEmailAndPw,
                    onSingUpCLicked = {
                        navController.navigate(AppScreens.RegistrationScreen.name)
                        loginViewModel.resetState()
                    },
                    onFacebookSignInCLicked = {
                        fbAuthLauncher.launch(listOf("email"))
                    },
                    onGoogleSignInClicked = {userData ->
                        if (userData != null) {
                            navController.navigate(
                                "${AppScreens.HomeScreen.name}?username=${userData.username ?: userData.email ?: ""}&profilePicture=${userData.profilePictureUrl ?: ""}"
                            ) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
        }

        composable(AppScreens.RegistrationScreen.name) {
            val viewModel: SignUpViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            LaunchedEffect(key1 = state.isSignUpSuccessful) {
                if (state.isSignUpSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign up success",
                        Toast.LENGTH_SHORT
                    ).show()

                    val userData = loginViewModel.getSignedInUser()
                    if (userData != null) {
                        navController.navigate(
                            "${AppScreens.HomeScreen.name}?username=${userData.username ?: userData.email ?: ""}&profilePicture=${userData.profilePictureUrl ?: ""}"
                        ) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                    viewModel.resetState()
                    loginViewModel.resetState()
                }
            }
            SignUpScreen(
                state = state,
                onNameChanged = viewModel::updateName,
                onEmailChanged = viewModel::updateEmail,
                onPasswordChanged = viewModel::updatePassword,
                onSingUpCLicked = viewModel::signUpByEmailAndPw,
            )
        }
        composable(
            "${AppScreens.HomeScreen.name}?username={username}&profilePicture={profilePicture}"
        ) {backStackEntry ->
            val arguments = backStackEntry.arguments
            val username = arguments?.getString("username")
            val profilePicture = arguments?.getString("profilePicture")
            requireNotNull(username) { "Username not provided!" }
            val viewModel: HomeViewModel = hiltViewModel()
            val repos = viewModel.githubRepList.collectAsLazyPagingItems()
            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()


            HomeScreen(
                username = username,
                profilePicture = profilePicture ?: "",
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::searchQueryChanged,
                onSearchClicked = viewModel::search,
                loadList = viewModel::fetchList,
                repos = repos
            ) {
                coroutineScope.launch {
                    loginViewModel.signOut()
                    Toast.makeText(
                        context,
                        "Signed out",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(AppScreens.LoginScreen.name) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }


    }
}