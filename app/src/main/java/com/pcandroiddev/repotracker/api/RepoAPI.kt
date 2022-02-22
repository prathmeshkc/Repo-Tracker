package com.pcandroiddev.repotracker.api

import com.pcandroiddev.repotracker.models.RepositoryResponse.RepositoryResponse
import com.pcandroiddev.repotracker.util.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoAPI {

    @GET("{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String
    ): Response<RepositoryResponse>




}