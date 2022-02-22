package com.pcandroiddev.repotracker.models.RepoEntity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "repo_table")
data class Repo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerName: String,
    val repoName: String
)
