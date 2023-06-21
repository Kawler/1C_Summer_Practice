package com.example.a1csummerpractice.data.repository

import android.util.Log
import com.example.a1csummerpractice.data.mappers.toNewsDataMap
import com.example.a1csummerpractice.data.remote.NewsAPI
import com.example.a1csummerpractice.data.remote.NewsDto
import com.example.a1csummerpractice.domain.newsdt.NewsData
import com.example.a1csummerpractice.domain.repository.INewsRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class NewsRepository() : INewsRepository {
    override suspend fun getNewsData(): NewsData {
        return getApi().getNewsData().data.toNewsDataMap()
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun getApi(): NewsAPI{
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    return Retrofit.Builder()
        .baseUrl("https://ws-tszh-1c-test.vdgb-soft.ru/api/mobile/news/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()
}