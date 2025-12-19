package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.ui.components.ChannelItem
import com.example.androidbootcampiwatepref.ui.components.VideoItem
import com.example.androidbootcampiwatepref.viewmodel.YoutubeViewModel

@Composable
fun YoutubeScreen(
    modifier: Modifier = Modifier,
    viewModel: YoutubeViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier) {

        // チャンネルリスト
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(uiState.channels) { channel ->
                ChannelItem(channel = channel)
            }
        }

        // 動画リスト
        LazyColumn {
            items(uiState.videos) { video ->
                VideoItem(
                    video = video,
                    showLikeButton = true,
                    viewModel = viewModel
                )
            }
        }
    }
}
