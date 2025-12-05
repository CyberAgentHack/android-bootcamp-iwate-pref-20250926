package com.example.androidbootcampiwatepref.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidbootcamp2025.ui.screens.HomeScreen
import com.example.androidbootcampiwatepref.ui.screen.YoutubeScreen

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
            HomeScreen(
                viewModel = viewModel()
            )
        }
        composable("RegisteredChannels") {
            YoutubeScreen(
                viewModel = viewModel()
            )
        }
    }
}
