package com.example.androidbootcampiwatepref.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidbootcampiwatepref.ui.screen.HomeScreen
import com.example.androidbootcampiwatepref.ui.screen.YoutubeScreen
import com.example.androidbootcampiwatepref.viewmodel.YoutubeViewModel
import androidx.hilt.navigation.compose.hiltViewModel

///画面遷移を管理するコンポーザブル
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = "home"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        //以下で画面(ルート)を定義
        composable("home") {
            val youtubeViewModel: YoutubeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = youtubeViewModel
            )
        }
        composable("RegisteredChannels") {
            val youtubeViewModel: YoutubeViewModel = hiltViewModel()
            YoutubeScreen(
                viewModel = youtubeViewModel
            )
        }
    }
}
