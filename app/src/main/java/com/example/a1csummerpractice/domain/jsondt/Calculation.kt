package com.example.a1csummerpractice.domain.jsondt

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Calculation(
    val culc_name: String,
    val culc_type: String,
    val items: List<Item>
)