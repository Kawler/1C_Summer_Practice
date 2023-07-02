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

    class CalcViewHolder(val binding: ItemChartRvBinding) : RecyclerView.ViewHolder(binding.root)

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
            rvItemDate.text = String.format(
                "Дата: %s", SimpleDateFormat("dd/MM/yyyy").format(
                    Date.from(
                        Instant.ofEpochSecond(
                            item.date
                        )
                    )
                )
            )
            rvItemAdditionalPeriod.text = String.format("Доп. период: %s", item.additional_period)
            rvItemStartAmount.text = String.format("Задолжность: %s", item.start_amount)
            rvItemEndAmount.text = String.format("Сумма к оплате: %s", item.end_amount)
            rvItemCalculated.text = String.format("Начислено: %s", item.calculated)
            rvItemPayed.text = String.format("Оплачено: %s", item.payed.toString())
            rvItemPenalties.text = String.format("Пенни: %s", item.penalties.toString())
            rvItemUploadTime.text = String.format("Год: %s", item.upload_time.toString())
            rvItemLastPaymentDate.text = String.format(
                "Дата последней оплаты: %s", SimpleDateFormat("dd/MM/yyyy").format(
                    Date.from(
                        Instant.ofEpochSecond(
                            item.last_payment_date
                        )
                    )
                )
            )
            rvItemCurrentDate.text = String.format(
                "Текущая дата: %s", SimpleDateFormat("dd/MM/yyyy").format(
                    Date.from(
                        Instant.ofEpochSecond(
                            item.currentDate
                        )
                    )
                )
            )
            rvItemCorrection.text = String.format("Коррекция: %s", item.correction.toString())
        }
    }
}
