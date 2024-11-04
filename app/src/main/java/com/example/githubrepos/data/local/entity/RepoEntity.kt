package com.example.githubrepos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepoEntity(
    @PrimaryKey val id: Int,
    val repoName: String,
    val url: String,
    val description: String,
    val ownerName: String,
    val ownerProfilePicture: String
)