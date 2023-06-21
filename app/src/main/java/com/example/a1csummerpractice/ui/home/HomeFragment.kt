package com.example.a1csummerpractice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1csummerpractice.data.repository.NewsRepository
import com.example.a1csummerpractice.databinding.FragmentHomeBinding
import com.example.a1csummerpractice.domain.adapters.NewsAdapter
import com.example.a1csummerpractice.domain.newsdt.NewsData
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var _newsAdapter: NewsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        val newsData: NewsData
        val newsRepository = NewsRepository()
        runBlocking {
            newsData = newsRepository.getNewsData()
        }
        Log.i("news data",newsData.toString())

        val layManager = LinearLayoutManager(context)
        _newsAdapter = NewsAdapter()
        _newsAdapter.data = newsData.news

        rvNews.layoutManager = layManager
        rvNews.adapter = _newsAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
