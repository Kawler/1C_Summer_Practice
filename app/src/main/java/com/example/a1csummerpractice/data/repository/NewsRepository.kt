package com.example.a1csummerpractice.data.repository

import android.util.Log
import com.example.a1csummerpractice.data.mappers.toNewsDataMap
import com.example.a1csummerpractice.data.remote.NewsAPI
import com.example.a1csummerpractice.domain.newsdt.NewsData
import com.example.a1csummerpractice.domain.repository.INewsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


class NewsRepository() : INewsRepository {
    override suspend fun getNewsData(): NewsData? {
        val result = getApi().getNewsData().data.toNewsDataMap()
        return if (result == null){
            Log.i("API Success","Вызова API не был удачен")
            null
        } else {
            result
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getApi(): NewsAPI {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val client = OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
            .protocols(listOf(Protocol.HTTP_1_1))
        return Retrofit.Builder()
            .baseUrl("https://ws-tszh-1c-test.vdgb-soft.ru/api/mobile/news/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client.build())
            .build()
            .create()
    }
}