package com.example.a1csummerpractice.ui.chart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a1csummerpractice.R
import com.example.a1csummerpractice.data.mappers.MyXAxis
import com.example.a1csummerpractice.data.mappers.jsonMapper
import com.example.a1csummerpractice.data.mappers.jsonToEntry
import com.example.a1csummerpractice.databinding.FragmentChartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null

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
        val chart:LineChart = binding.chart

        val jsonDT = context?.let { jsonMapper(context = it) }
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = MyXAxis()

        val lineDataSet: LineDataSet = LineDataSet(jsonDT?.let { jsonToEntry(it) },"Dt 1")
        val dataSets: MutableList<ILineDataSet> = arrayListOf()
        dataSets.add(lineDataSet)
        val data: LineData = LineData(dataSets)
        chart.data = data
        chart.invalidate()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

