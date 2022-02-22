package com.pcandroiddev.repotracker.api

import com.pcandroiddev.repotracker.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RepoAPI by lazy {
        retrofit.create(RepoAPI::class.java)
    }

}