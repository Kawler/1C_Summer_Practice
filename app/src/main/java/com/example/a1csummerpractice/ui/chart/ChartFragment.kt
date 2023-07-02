package com.example.a1csummerpractice.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1csummerpractice.R
import com.example.a1csummerpractice.data.mappers.jsonMapper
import com.example.a1csummerpractice.data.mappers.jsonToEntry
import com.example.a1csummerpractice.databinding.FragmentChartBinding
import com.example.a1csummerpractice.domain.adapters.CalcAdapter
import com.example.a1csummerpractice.domain.adapters.MyXAxis
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private lateinit var _mainAdapter: CalcAdapter
    private lateinit var _capAdapter: CalcAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)

        //Парсим Json и устанавливаем форматер для X оси
        val jsonDT = context?.let { jsonMapper(context = it) }
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.xAxis.valueFormatter = MyXAxis()

        //Стиль для графика
        val lineDataSet: LineDataSet =
            LineDataSet(jsonDT?.let { jsonToEntry(it) }, "Динамика изменения суммы начислений")
        lineDataSet.lineWidth = 6F
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.color = R.color.light_cyan
        lineDataSet.setCircleColor(R.color.dark_cyan)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = R.color.dark_cyan
        lineDataSet.fillAlpha = 70
        lineDataSet.circleRadius = 7f
        lineDataSet.valueTextSize = 10f

        //Добавляем датасет
        val dataSets: MutableList<ILineDataSet> = arrayListOf()
        dataSets.add(lineDataSet)
        val data: LineData = LineData(dataSets)
        binding.chart.data = data
        binding.chart.description.isEnabled = false
        binding.chart.invalidate()

        //Прикреаляем адаптеры
        _mainAdapter = CalcAdapter()
        _mainAdapter.data = jsonDT!!.calculations[0].items
        _capAdapter = CalcAdapter()
        _capAdapter.data = jsonDT.calculations[1].items

        //Связываем со списками
        val layManagerMain = LinearLayoutManager(context)
        val layManagerCap = LinearLayoutManager(context)
        binding.rvChartMain.layoutManager = layManagerMain
        binding.rvChartMain.adapter = _mainAdapter
        binding.rvChartCap.layoutManager = layManagerCap
        binding.rvChartCap.adapter = _capAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

