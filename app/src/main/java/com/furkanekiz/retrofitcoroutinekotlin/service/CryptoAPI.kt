package com.furkanekiz.retrofitcoroutinekotlin.service

import com.furkanekiz.retrofitcoroutinekotlin.model.CryptoModel
import retrofit2.Response
import retrofit2.http.GET

interface CryptoAPI {

    @GET("users/{user}/repos")
    suspend fun getData(): Response<List<CryptoModel>>
}