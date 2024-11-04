package com.example.githubrepos.di

import android.content.Context
import androidx.room.Room
import com.android.installreferrer.BuildConfig
import com.example.githubrepos.data.local.RepDatabase
import com.example.githubrepos.data.remote.GitHubApi
import com.example.githubrepos.data.remote.GitHubApi.Companion.BASE_URL
import com.example.githubrepos.data.repository.GithubRepositoryImpl
import com.example.githubrepos.data.repository.UserRepositoryImpl
import com.example.githubrepos.domain.repository.GithubRepository
import com.example.githubrepos.domain.repository.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRepoDatabase(@ApplicationContext context: Context): RepDatabase {
        return Room.databaseBuilder(
            context,
            RepDatabase::class.java,
            "repos.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepoApi(): GitHubApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpBuilder =
            OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(BearerAuthInterceptor())

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    private class BearerAuthInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                .header("Authorization", "Bearer " + "GITHUB_TOKEN").build()
            return chain.proceed(authenticatedRequest)
        }
    }


    @Provides
    @Singleton
    fun provideGithubRepository(githubRepositoryImpl: GithubRepositoryImpl): GithubRepository {
        return githubRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository {
        return userRepositoryImpl
    }

}