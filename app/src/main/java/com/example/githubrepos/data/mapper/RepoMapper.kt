package com.example.githubrepos.data.mapper

import com.example.githubrepos.data.local.entity.RepoEntity
import com.example.githubrepos.data.remote.dto.RepoDtoItem
import com.example.githubrepos.domain.model.RepoItem


fun RepoDtoItem.toRepoEntity() = RepoEntity(
    id = id,
    repoName = full_name.split("/")[1],
    url = html_url,
    description = description ?: "",
    ownerName = owner.login,
    ownerProfilePicture = owner.avatar_url
)

fun RepoEntity.toRepItem() = RepoItem(
    id = id,
    repoName = repoName,
    url = url,
    description = description,
    ownerName = ownerName,
    ownerProfilePicture = ownerProfilePicture
)

