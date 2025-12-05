package com.example.androidbootcampiwatepref.viewmodel

import androidx.lifecycle.ViewModel
import androidx.annotation.DrawableRes
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampiwatepref.R
import com.example.androidbootcampiwatepref.data.repository.YoutubeRepository
import com.example.androidbootcampiwatepref.domain.domainobject.Channel
import com.example.androidbootcampiwatepref.domain.domainobject.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.map

// UIの状態を表すデータクラス
data class YoutubeUiState(
    val channels: List<Channel> = emptyList(),
    val videos: List<Video> = emptyList()
)

class YoutubeViewModel(
    private val repository: YoutubeRepository = YoutubeRepository()
) : ViewModel() {

    // UIに公開する状態
    private val _uiState = MutableStateFlow(YoutubeUiState())
    val uiState: StateFlow<YoutubeUiState> = _uiState.asStateFlow()

    init {
        // ViewModelが作成されたときにデータを取得する
        fetchData()
    }
    private fun fetchData() {

        //コルーチンの起動(この中の処理は非同期で実行される)
        viewModelScope.launch {
            val channels = repository.getChannels()
            val videos = repository.getVideos()

            //状態の更新
            //現在の状態をコピーし、一部のフィールドだけを変更
            _uiState.update { currentState ->
                currentState.copy(  //新しいデータの作成
                    //データの変換
                    channels = channels.map { channel ->
                        Channel(
                            name = channel.name,
                            imageId = getIconResource(channel.imageId)
                        )
                    },
                    videos = videos.map { video ->
                        Video(
                            title = video.title,
                            channelName = video.channelName,
                            thumbnailRes = getThumbnailResource(video.thumbnailRes),
                            channelIconRes = getIconResource(video.channelIconRes),
                            viewCount = video.viewCount,
                            uploadedAt = video.uploadedAt,
                            likeCount = video.likeCount
                        )
                    }
                )
            }
        }
    }
    // 画像IDをリソースIDに変換
    private fun getIconResource(imageId: Int): Int {
        return when (imageId) {
            1 -> R.drawable.sample_image1
            2 -> R.drawable.sample_image2
            3 -> R.drawable.sample_image3
            4 -> R.drawable.sample_image4
            5 -> R.drawable.sample_image5
            6 -> R.drawable.sample_image6
            7 -> R.drawable.sample_image7
            8 -> R.drawable.sample_image8
            else -> R.drawable.sample_image1
        }
    }

    private fun getThumbnailResource(thumbnailId: Int): Int {
        return when (thumbnailId) {
            1 -> R.drawable.youtube_thumbnail2
            2 -> R.drawable.youtube_thumbnail3
            else -> R.drawable.youtube_thumbnail2
        }
    }
}