package com.example.a1csummerpractice.domain

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.a1csummerpractice.domain.newsdt.NewsData
import com.example.a1csummerpractice.domain.newsdt.NewsItemData

@Dao
interface NewsDao {
    @Insert
    fun insert(news: NewsItemData)

    @Query("delete from news_table")
    fun deleteAllNews()

    @Query("select * from news_table order by roomId")
    fun getAllNews(): List<NewsItemData>
}