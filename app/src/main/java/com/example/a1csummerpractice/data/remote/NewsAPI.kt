package com.example.a1csummerpractice.data.remote

import retrofit2.http.GET

interface NewsAPI {
    @GET("list")
    suspend fun getNewsData(): NewsDto
}