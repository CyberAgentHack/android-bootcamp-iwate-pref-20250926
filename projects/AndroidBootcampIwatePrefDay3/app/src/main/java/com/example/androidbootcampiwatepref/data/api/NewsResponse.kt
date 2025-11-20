package com.example.androidbootcampiwatepref.data.api

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val news: List<News>,
) {

    @Serializable
    data class News(
        val title: String,
        val body: String,
    )
}
