package com.example.a1csummerpractice.domain.newsdt


data class NewsData(
    var news: List<NewsItemData>?,
    var count: Int,
    val error_msg: String
)
