package com.example.a1csummerpractice.domain.repository

import com.example.a1csummerpractice.domain.newsdt.NewsData

interface INewsRepository {
    suspend fun getNewsData(): NewsData
}
