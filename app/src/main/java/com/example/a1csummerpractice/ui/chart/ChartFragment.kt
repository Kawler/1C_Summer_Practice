package com.example.a1csummerpractice.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1csummerpractice.R
import com.example.a1csummerpractice.data.mappers.MyXAxis
import com.example.a1csummerpractice.data.mappers.jsonMapper
import com.example.a1csummerpractice.data.mappers.jsonToEntry
import com.example.a1csummerpractice.databinding.FragmentChartBinding
import com.example.a1csummerpractice.domain.adapters.CalcAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private lateinit var _mainAdapter: CalcAdapter
    private lateinit var _capAdapter: CalcAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chartViewModel =
            ViewModelProvider(this).get(ChartViewModel::class.java)
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val chart: LineChart = binding.chart
        val rvMain: RecyclerView = binding.rvChartMain
        val rvCap: RecyclerView = binding.rvChartCap

        val jsonDT = context?.let { jsonMapper(context = it) }
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = MyXAxis()

        //LineDataSet styling
        val lineDataSet: LineDataSet = LineDataSet(jsonDT?.let { jsonToEntry(it) },"Динамика изменения суммы начислений")
        lineDataSet.lineWidth = 6F
        lineDataSet.color = R.color.purple_500
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.setCircleColor(Color.BLUE)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = R.color.purple_500
        lineDataSet.fillAlpha = 190
        lineDataSet.circleRadius = 7f
        lineDataSet.valueTextSize = 10f

        val dataSets: MutableList<ILineDataSet> = arrayListOf()
        dataSets.add(lineDataSet)
        val data: LineData = LineData(dataSets)
        chart.data = data
        chart.invalidate()

        _mainAdapter = CalcAdapter()
        _mainAdapter.data = jsonDT!!.calculations.get(0).items
        _capAdapter = CalcAdapter()
        _capAdapter.data = jsonDT.calculations.get(1).items

        val layManagerMain = LinearLayoutManager(context)
        val layManagerCap = LinearLayoutManager(context)
        rvMain.layoutManager = layManagerMain
        rvMain.adapter = _mainAdapter
        rvCap.layoutManager = layManagerCap
        rvCap.adapter = _capAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

