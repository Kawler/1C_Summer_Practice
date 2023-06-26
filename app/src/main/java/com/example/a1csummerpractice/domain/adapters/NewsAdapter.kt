package com.example.a1csummerpractice.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.a1csummerpractice.databinding.ItemHomeRvBinding
import com.example.a1csummerpractice.domain.newsdt.NewsItemData

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var data: List<NewsItemData> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class NewsViewHolder(val binding: ItemHomeRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeRvBinding.inflate(inflater, parent, false)

        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    //Заполняет список новостей
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = data[position]

        with(holder.binding) {
            rvNewsTitle.text = item.title
            rvNewsImg.load(item.img)
            rvNewsAnnotation.text = item.annotation
            rvNewsDate.text = item.news_date
            rvNewsLink.text = item.mobile_url
        }
    }
}