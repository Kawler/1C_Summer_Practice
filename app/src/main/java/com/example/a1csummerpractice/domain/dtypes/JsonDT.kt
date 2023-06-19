package com.example.a1csummerpractice.domain.dtypes

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonDT(
    val calculations: List<Calculation>
)