package com.example.a1csummerpractice.data.mappers

import android.content.Context
import com.example.a1csummerpractice.domain.jsondt.JsonDT
import com.github.mikephil.charting.data.Entry
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

//Конвертер Json
@OptIn(ExperimentalStdlibApi::class)
fun jsonMapper(context: Context): JsonDT? {
    val jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter: JsonAdapter<JsonDT> = moshi.adapter<JsonDT>()
    return jsonAdapter.fromJson(jsonString)
}

//Конвертирует данные и заполняет график
fun jsonToEntry(jsonDT: JsonDT): MutableList<Entry> {
    val entries: MutableList<Entry> = arrayListOf()
    //Даты для оси x
    val dates = jsonDT.calculations.get(0).items.map { it.date }
    //Даты для оси y
    val sum = jsonDT.calculations.get(0).items.map { it.end_amount }
    val size = sum.size - 1
    //Добавление записей
    for (i in size downTo 0) {
        entries.add(Entry(dates[i].toFloat(), sum[i].toFloat()))
    }
    return entries
}