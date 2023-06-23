package com.example.a1csummerpractice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1csummerpractice.R
import com.example.a1csummerpractice.data.repository.NewsRepository
import com.example.a1csummerpractice.databinding.FragmentHomeBinding
import com.example.a1csummerpractice.domain.adapters.NewsAdapter
import com.example.a1csummerpractice.domain.newsdt.NewsData
import com.example.a1csummerpractice.domain.newsdt.NewsItemData
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.ZoneId
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var _newsAdapter: NewsAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rvNews: RecyclerView = binding.homeRv
        val tbBtn: Button = binding.homeTbReloadBtn
        val tbSpinnerM: Spinner = binding.homeTbSpinnerMonth
        val tbSpinnerY: Spinner = binding.homeTbSpinnerYear
        val tvStatus: TextView = binding.tvHomeStatus
        val toolbar = binding.appBarLayout
        val fab: FloatingActionButton = binding.fabHome

        //Управление кнопкой в зависимости от прокрутки тулбара
        toolbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener(
            fun(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    //Collapsed
                    fab.visibility = View.VISIBLE
                } else if (verticalOffset == 0) {
                    //Extended
                    fab.visibility = View.GONE
                } else {
                    //Transition
                    fab.visibility = View.GONE
                }
            }
        ))

        fab.setOnClickListener {
            toolbar.setExpanded(true)
        }

        //Адаптеры для спинеров
        val spinMAdapter: SpinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_months,
            android.R.layout.simple_spinner_item
        )
        val spinYAdapter: SpinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_years,
            android.R.layout.simple_spinner_item
        )
        tbSpinnerM.adapter = spinMAdapter
        tbSpinnerY.adapter = spinYAdapter

        //Получение новостей
        var newsData: NewsData?
        val newsRepository = NewsRepository()
        runBlocking {
            try {
                newsData = newsRepository.getNewsData()
            } catch (e: Exception) {
                newsData = null
                tvStatus.text = "Сервис новостей не отвечает"
            }

        }

        if (newsData != null)
            if (newsData!!.count == 0) {
                Log.i("API Error mesage", newsData!!.error_msg)
                tvStatus.text = "Не удалось получить новости"
                tvStatus.visibility = View.VISIBLE
            }

        //Заполнение списка новостей
        val layManager = LinearLayoutManager(context)
        _newsAdapter = NewsAdapter()
        if (newsData != null)
            if (newsData!!.news != null)
                _newsAdapter.data = newsData!!.news!!

        rvNews.layoutManager = layManager
        rvNews.adapter = _newsAdapter

        //Логика работы кнопки в тулбаре - обновление и сортировка данных
        tbBtn.setOnClickListener {
            tvStatus.visibility = View.INVISIBLE
            runBlocking {
                try {
                    newsData = newsRepository.getNewsData()
                } catch (e: Exception) {
                    newsData = null
                    tvStatus.text = "Сервис новостей не отвечает"
                    tvStatus.visibility = View.VISIBLE
                }
            }
            if (newsData != null && newsData!!.news != null) {
                _newsAdapter.data = newsData!!.news!!
                _newsAdapter.notifyDataSetChanged()
                val newData: NewsData =
                    setMonthYear(spinnerM = tbSpinnerM, spinnerY = tbSpinnerY, newsData!!)
                if (newData.count == 0) {
                    tvStatus.text = "Нет новостей за выбранный период"
                    tvStatus.visibility = View.VISIBLE
                }
                _newsAdapter.data = newData.news!!
                _newsAdapter.notifyDataSetChanged()
            } else {
                tvStatus.text = "Сервис новостей не отвечает"
                tvStatus.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun setMonthYear(
    spinnerM: Spinner,
    spinnerY: Spinner,
    newsData: NewsData
): NewsData {
    var year: Int? = null
    var month: Int? = null
    when (spinnerM.selectedItemPosition) {
        1 -> month = 1
        2 -> month = 2
        3 -> month = 3
        4 -> month = 4
        5 -> month = 5
        6 -> month = 6
        7 -> month = 7
        8 -> month = 8
        9 -> month = 9
        10 -> month = 10
        11 -> month = 11
        12 -> month = 12
    }
    when (spinnerY.selectedItemPosition) {
        1 -> year = 2020
        2 -> year = 2021
        3 -> year = 2022
        4 -> year = 2023
    }
    return getData(newsData, month, year)
}

//Проводится проверка на выбранные условия в тулбаре и конвертируется время новости, после этого, если
//новость соответсвует условиям, то она добовляется в отсортированный список новостей
fun getData(newsData: NewsData, month: Int?, year: Int?): NewsData {
    val newNewsData: NewsData = newsData
    val newsArray: MutableList<NewsItemData> = mutableListOf()
    if (month != null && year == null) {
        for (item in newsData.news!!) {
            val zdt =
                Instant.ofEpochSecond(item.news_date_uts.toLong()).atZone(ZoneId.of("Etc/UTC"))
            if (zdt.monthValue == month) {
                newsArray.add(item)
            }
        }
        newNewsData.news = newsArray
        newNewsData.count = newsArray.size
    }
    if (month == null && year != null) {
        for (item in newsData.news!!) {
            val zdt =
                Instant.ofEpochSecond(item.news_date_uts.toLong()).atZone(ZoneId.of("Etc/UTC"))
            if (zdt.year == year) {
                newsArray.add(item)
            }
        }
        newNewsData.news = newsArray
        newNewsData.count = newsArray.size
    }
    if (month != null && year != null) {
        for (item in newsData.news!!) {
            val zdt =
                Instant.ofEpochSecond(item.news_date_uts.toLong()).atZone(ZoneId.of("Etc/UTC"))
            if (zdt.monthValue == month && zdt.year == year) {
                newsArray.add(item)
            }
        }
        newNewsData.news = newsArray
        newNewsData.count = newsArray.size
    }
    return newNewsData
}
