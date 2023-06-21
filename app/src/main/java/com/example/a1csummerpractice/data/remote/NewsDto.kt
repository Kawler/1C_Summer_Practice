package com.example.a1csummerpractice.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDto(
    val success: Boolean,
    val data: NewsDataDto
)