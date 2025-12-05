package com.example.androidbootcampiwatepref.data.repository

import com.example.androidbootcampiwatepref.domain.domainobject.Channel
import com.example.androidbootcampiwatepref.domain.domainobject.Video

// データの取得元を抽象化するクラス
class YoutubeRepository {

    fun getChannels(): List<Channel> {
        return listOf(
            Channel("チャンネル1", 1),
            Channel("チャンネル2", 2),
            Channel("チャンネル3", 3),
            Channel("チャンネル4", 4),
            Channel("チャンネル5", 5),
            Channel("チャンネル6", 6),
            Channel("チャンネル7", 7),
            Channel("チャンネル8", 8),
        )
    }

    fun getVideos(): List<Video> {
        return List(10) {
            Video(
                title = "【Kotlin】Youtubeクローンの作り方の解説",
                channelName = "チャンネル名",
                thumbnailRes = 1,
                channelIconRes = 1,
                viewCount = "100万回",
                uploadedAt = "1日前",
                likeCount = 5
            );
            Video(
                title = "【料理】自家製パンの作り方",
                channelName = "チャンネル名",
                thumbnailRes = 2,
                channelIconRes = 2,
                viewCount = "100万回",
                uploadedAt = "1日前",
                likeCount = 10
            )
        }
    }
}
