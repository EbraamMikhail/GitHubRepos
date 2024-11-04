package com.example.githubrepos.domain.repository

import androidx.paging.PagingData
import com.example.githubrepos.data.local.entity.RepoEntity
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun search(query: String): Flow<PagingData<RepoEntity>>
    fun getList(): Flow<PagingData<RepoEntity>>
}