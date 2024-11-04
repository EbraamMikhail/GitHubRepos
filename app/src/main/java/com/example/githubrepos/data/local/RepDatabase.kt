package com.example.githubrepos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubrepos.data.local.dao.RemoteKeyDao
import com.example.githubrepos.data.local.dao.RepoDao
import com.example.githubrepos.data.local.entity.RemoteKeys
import com.example.githubrepos.data.local.entity.RepoEntity

@Database(
    entities = [RepoEntity::class, RemoteKeys::class],
    version = 1
)
abstract class RepDatabase : RoomDatabase() {
    abstract val dao: RepoDao
    abstract val remoteKeyDao: RemoteKeyDao
}