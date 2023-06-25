package com.example.a1csummerpractice.domain.newsdt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsItemData(
    @PrimaryKey(autoGenerate = true) val roomId: Int = 0,
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
