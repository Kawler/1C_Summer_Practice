package com.example.a1csummerpractice.domain.jsondt

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonDT(
    val calculations: List<Calculation>
)