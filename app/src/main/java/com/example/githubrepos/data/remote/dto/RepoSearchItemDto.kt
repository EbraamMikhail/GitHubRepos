package com.example.githubrepos.data.remote.dto

data class RepoSearchItemDto(
    val incomplete_results: Boolean,
    val items: List<RepoDtoItem>,
    val total_count: Int
)