package com.example.androidbootcampiwatepref.domain.domainobject

// 登録チャンネルのデータ構造
data class Channel(
    val name: String,
    val imageId: Int
)

// 動画のデータ構造
data class Video(
    val id: Int = 0,
    val title: String,
    val channelName: String,
    val thumbnailRes: Int,
    val channelIconRes: Int,
    val viewCount: String,
    val uploadedAt: String,
    val likeCount: Int
)
