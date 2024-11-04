package com.example.githubrepos.data.remote

import com.example.githubrepos.data.remote.dto.RepoDtoItem
import com.example.githubrepos.data.remote.dto.RepoSearchItemDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET("repositories")
    suspend fun getPublicRepositories(@Query("since") id: Int): List<RepoDtoItem>


    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): RepoSearchItemDto

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}