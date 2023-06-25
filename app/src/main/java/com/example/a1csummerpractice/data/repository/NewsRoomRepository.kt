package com.example.a1csummerpractice.data.repository

import com.example.a1csummerpractice.domain.NewsDao
import com.example.a1csummerpractice.domain.newsdt.NewsItemData

class NewsRoomRepository(private val newsDao: NewsDao){

    val readAllData: List<NewsItemData> = newsDao.getAllNews()

    fun insertNews(news: NewsItemData){
        newsDao.insert(news)
    }

    fun deleteAllNews(){
        newsDao.deleteAllNews()
    }
}
