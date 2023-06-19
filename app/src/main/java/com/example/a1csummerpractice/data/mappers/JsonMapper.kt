package com.example.a1csummerpractice.data.mappers

import android.content.Context
import android.util.Log
import com.example.a1csummerpractice.domain.dtypes.JsonDT
import com.github.mikephil.charting.data.Entry
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalStdlibApi::class)
fun jsonMapper(context: Context): JsonDT? {
    val jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter: JsonAdapter<JsonDT> = moshi.adapter<JsonDT>()
    return jsonAdapter.fromJson(jsonString)
}

fun jsonToEntry(jsonDT: JsonDT): MutableList<Entry>{
    val entries: MutableList<Entry> = arrayListOf()
    //Даты для оси x
    val dates = jsonDT.calculations.get(0).items.map{it.date}
    //Даты для оси y
    val sum = jsonDT.calculations.get(0).items.map{it.end_amount}
    /*
    entries.add(Entry(1.66984205E9f, sum[0].toFloat()))
    entries.add(Entry(1.67519885E9f, sum[1].toFloat()))
    entries.add(Entry(1.67761805E9f, sum[2].toFloat()))
    */
    entries.add(Entry(dates[3].toFloat(),sum[0].toFloat()))
    entries.add(Entry(dates[2].toFloat(),sum[1].toFloat()))
    entries.add(Entry(dates[1].toFloat(),sum[2].toFloat()))
    entries.add(Entry(dates[0].toFloat(),sum[3].toFloat()))

    return entries
}