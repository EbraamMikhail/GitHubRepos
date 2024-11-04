package com.example.githubrepos.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.githubrepos.R
import com.example.githubrepos.domain.model.RepoItem
import com.example.githubrepos.ui.screens.components.CommonTextField
import retrofit2.HttpException
import java.net.UnknownHostException


@Composable
fun HomeScreen(
    username: String,
    profilePicture: String,
    searchQuery: String,
    repos: LazyPagingItems<RepoItem>,
    onSearchQueryChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    loadList: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = repos.loadState) {
        if (repos.loadState.refresh is LoadState.Error) {
            val loadStateError = (repos.loadState.refresh as LoadState.Error)
            val message = when (loadStateError.error) {
                is UnknownHostException -> "No Connection Available!"
                is HttpException ->
                    if ((loadStateError.error as HttpException).code() == 401) {
                        "Please Add API key"
                    } else {
                        (loadStateError.error as HttpException).message()
                    }

                else -> loadStateError.error.message ?: "Failed to load data"
            }
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    LaunchedEffect(key1 = searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            loadList()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderContent(
            username = username,
            profilePicture = profilePicture,
            onSignOut = onSignOut,
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
        CommonTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = searchQuery,
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = {
                        onSearchQueryChange("")
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = ""
                        )
                    }
                }
            } else null,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = ""
                )
            },
            placeholder = stringResource(id = R.string.search_here),
            onValueChange = onSearchQueryChange,
            imeActions = ImeAction.Search,
            onAction = KeyboardActions(
                onSearch = {
                    onSearchClicked()
                    keyboardController?.hide()
                }
            )
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (repos.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = repos.itemCount,
                        key = repos.itemKey { it.id }
                    ){index ->
                        repos[index]?.let {
                            RepoItemScreen(
                                repoItem = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )
                            HorizontalDivider(Modifier.padding(8.dp))
                        }
                    }
                    item {
                        if (repos.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun HeaderContent(
    username: String,
    profilePicture: String,
    onSignOut: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (profilePicture.isNotBlank()) {
                    AsyncImage(
                        model = profilePicture,
                        contentDescription = "",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(color = Color.LightGray),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(color = Color.LightGray),
                        tint = Color.DarkGray
                    )
                }

                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontSize = 18.sp)
                            ) {
                                append("Welcome" + "\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 16.sp
                                )
                            ) {
                                append(username)
                            }
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Serif,
                    )
                }
            }
            IconButton(onClick = onSignOut) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "")
            }

        }
    }

}