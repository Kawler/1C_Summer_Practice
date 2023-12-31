package com.example.a1csummerpractice.domain.adapters

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyXAxis() : ValueFormatter() {
    //Стиль отображения данных Х оси на графике
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val conv: Long = value.toLong() * 1000
        val timeMil = Date(conv)
        val df = SimpleDateFormat("MM/yy", Locale.ENGLISH)
        return df.format(timeMil)
    }
}