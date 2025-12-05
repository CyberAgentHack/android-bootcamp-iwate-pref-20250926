package com.example.androidbootcamp2025.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.androidbootcamp2025.ui.components.VideoItem
import com.example.androidbootcamp2025.viewmodel.YoutubeViewModel

///サンプルのホーム画面
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: YoutubeViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    // 動画リスト
    LazyColumn {
        items(uiState.videos) { video ->
            VideoItem(video = video)
        }
    }
}
