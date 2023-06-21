package com.example.a1csummerpractice.domain.newsdt

import com.example.a1csummerpractice.data.remote.NewsItemDto

data class NewsData(
    var news: List<NewsItemData>,
    val count: Int,
    val error_msg: String
)
