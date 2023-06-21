package com.example.a1csummerpractice.domain.jsondt

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    val additional_period: String,
    val calculated: Double,
    val correction: Double,
    val currentDate: Long,
    val date: Long,
    val end_amount: Double,
    val id: String,
    val last_payment_date: Long,
    val payed: Double,
    val penalties: Double,
    val start_amount: Double,
    val upload_time: Long
)