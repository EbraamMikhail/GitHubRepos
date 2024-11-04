package com.example.githubrepos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey
    val searchQuery: String,
    val prevKey: Int?,
    val nextKey: Int?
)