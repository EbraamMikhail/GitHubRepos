package com.example.githubrepos.domain.model

data class RepoItem(
    val id: Int,
    val repoName: String,
    val url: String,
    val description: String,
    val ownerName: String,
    val ownerProfilePicture: String,
)
