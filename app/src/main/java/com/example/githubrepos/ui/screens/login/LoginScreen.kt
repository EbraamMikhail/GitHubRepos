package com.example.githubrepos.ui.screens.login

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.githubrepos.R
import com.example.githubrepos.domain.model.UserData
import com.example.githubrepos.domain.model.toUserData
import com.example.githubrepos.ui.screens.components.CommonButton
import com.example.githubrepos.ui.screens.components.CommonTextField
import com.example.githubrepos.ui.theme.FaceBookDarkBlue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(
    state: SignInState,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onSingUpCLicked: () -> Unit,
    onEmailPwSignInClicked: () -> Unit,
    onFacebookSignInCLicked: () -> Unit,
    onGoogleSignInClicked: (UserData?) -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isMailError by remember { mutableStateOf(state.isEmailError) }
    val isPasswordError by remember { mutableStateOf(state.isPasswordError) }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            onGoogleSignInClicked(result.user?.toUserData())
            user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    val token = stringResource(R.string.default_web_client_id)



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.3f)
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.coptic_logo),
                contentDescription = "",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds
            )
        }

        CommonTextField(
            modifier = Modifier.padding(top = 16.dp),
            value = state.email,
            placeholder = stringResource(id = R.string.email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email"
                )
            },
            keyboardType = KeyboardType.Email,
            onValueChange = onEmailChanged,
            isError = isMailError,
            errorMessage = stringResource(id = R.string.mail_cant_be_empty)
        )

        CommonTextField(modifier = Modifier.padding(top = 8.dp),
            value = state.password,
            placeholder = stringResource(id = R.string.password),
            keyboardType = KeyboardType.Password,
            imeActions = ImeAction.Done,
            onValueChange = onPasswordChanged,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Email"
                )
            },
            isError = isPasswordError,
            errorMessage = stringResource(id = R.string.password_cant_be_empty),
            onAction = KeyboardActions {
                keyboardController?.hide()
            })

        if (state.isLoading) {
            CircularProgressIndicator()
        }
        AnimatedVisibility(visible = state.isLoading.not()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CommonButton(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 20.dp)
                        .fillMaxWidth(0.5f),
                    text = stringResource(id = R.string.sign_in),
                    cornerRadius = 15.dp,
                    onClick = {
                        keyboardController?.hide()
                        onEmailPwSignInClicked()
                    },
                    enabled = state.password.isNotEmpty() && state.email.trim().isNotEmpty()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.or_connect_using),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 26.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
//                            onGoogleSignInClicked
                            val gso =
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(token)
                                    .requestEmail()
                                    .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            launcher.launch(googleSignInClient.signInIntent)
                          },
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_login_google),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = {
//                            onFacebookSignInCLicked()
                          },
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FaceBookDarkBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_login_facebook),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.don_t_have_account),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = stringResource(R.string.sign_up),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Blue,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clickable { onSingUpCLicked() }
                        )
                    }
                }
            }

        }

    }

}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}