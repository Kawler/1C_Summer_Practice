package com.example.a1csummerpractice.data.mappers

import com.example.a1csummerpractice.data.remote.NewsDataDto
import com.example.a1csummerpractice.data.remote.NewsItemDto
import com.example.a1csummerpractice.domain.newsdt.NewsData
import com.example.a1csummerpractice.domain.newsdt.NewsItemData


fun NewsDataDto.toNewsDataMap(): NewsData? {
    val data: MutableList<NewsItemData> = mutableListOf()
    if (news != null) {
        for (item in news) {
            data.add(item.toNewsItemDataMap())
        }
    }
    return NewsData(news = data, count = count, error_msg = error_msg)

}

fun NewsItemDto.toNewsItemDataMap(): NewsItemData {
    return NewsItemData(
        id = id,
        title = title,
        img = img,
        local_img = local_img,
        news_date = news_date,
        annotation = annotation,
        id_resource = id_resource,
        type = type,
        news_date_uts = news_date_uts,
        mobile_url = mobile_url
    )
}