package com.example.a1csummerpractice.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a1csummerpractice.databinding.ItemChartRvBinding
import com.example.a1csummerpractice.domain.jsondt.Item
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

class CalcAdapter : RecyclerView.Adapter<CalcAdapter.CalcViewHolder>() {

    var data: List<Item> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class CalcViewHolder(val binding: ItemChartRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalcViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChartRvBinding.inflate(inflater, parent, false)

        return CalcViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    //Заполняет списоки на странице с графиком данными
    override fun onBindViewHolder(holder: CalcViewHolder, position: Int) {
        val item = data[position]
        val context = holder.itemView.context

        with(holder.binding) {
            rvItemDate.text =
                "Дата: " + SimpleDateFormat("dd/MM/yyyy").format(
                    Date.from(
                        Instant.ofEpochSecond(
                            item.date
                        )
                    )
                )
            rvItemAdditionalPeriod.text = "Доп.период: " + item.additional_period
            rvItemStartAmount.text = "Задолжность: " + item.start_amount.toString()
            rvItemEndAmount.text = "Сумма к оплате: " + item.end_amount.toString()
            rvItemCalculated.text = "Начислено: " + item.calculated.toString()
            rvItemPayed.text = "Оплачено: " + item.payed.toString()
            rvItemPenalties.text = "Пенни: " + item.penalties.toString()
            rvItemUploadTime.text = "Год: " + item.upload_time.toString()
            rvItemLastPaymentDate.text =
                "Дата последней оплаты: " + SimpleDateFormat("dd/MM/yyyy").format(
                    Date.from(
                        Instant.ofEpochSecond(
                            item.last_payment_date
                        )
                    )
                )
            rvItemCurrentDate.text =
                "Текущая дата: " + SimpleDateFormat("dd/MM/yyyy").format(
                    Date.from(
                        Instant.ofEpochSecond(
                            item.currentDate
                        )
                    )
                )
            rvItemCorrection.text = "Коррекция: " + item.correction.toString()
        }
    }
}
