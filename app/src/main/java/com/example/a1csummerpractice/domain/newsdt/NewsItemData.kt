package com.example.a1csummerpractice.domain.newsdt

import java.util.Date

data class NewsItemData(
    val id: Int,
    val title: String,
    val img: String,
    val local_img: String,
    val news_date: String,
    val annotation: String,
    val id_resource: Int,
    val type: Int,
    val news_date_uts: String,
    val mobile_url: String
)
