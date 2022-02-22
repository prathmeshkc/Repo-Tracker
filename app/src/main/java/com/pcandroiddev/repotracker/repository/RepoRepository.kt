package com.pcandroiddev.repotracker.repository

import com.pcandroiddev.repotracker.api.RetrofitInstance
import com.pcandroiddev.repotracker.db.RepoDatabase
import com.pcandroiddev.repotracker.models.RepoEntity.Repo

class RepoRepository(
    private val db: RepoDatabase
) {

    suspend fun insertRepo(repo: Repo) = db.getRepoDao().insertRepo(repo)

    fun getAllRepos() = db.getRepoDao().getAllRepos()

    suspend fun getRepository(ownerName: String, repoName: String) =
        RetrofitInstance.api.getRepository(ownerName, repoName)


}