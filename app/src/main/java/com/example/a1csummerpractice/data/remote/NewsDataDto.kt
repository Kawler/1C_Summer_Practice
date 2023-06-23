package com.example.a1csummerpractice.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDataDto(
    val news: List<NewsItemDto>?,
    val count: Int,
    val error_msg: String
)
