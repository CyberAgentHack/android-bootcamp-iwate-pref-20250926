package com.example.androidbootcampiwatepref

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidbootcampiwatepref.ui.AppNavHost
import com.example.androidbootcampiwatepref.ui.components.AppBar
import com.example.androidbootcampiwatepref.ui.components.BottomNavigation
import com.example.androidbootcampiwatepref.ui.theme.AndroidBootcampIwatePrefTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcampIwatePrefTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // NavControllerから現在のルートを取得
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //各画面で共通している部分を構築
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar() },
        bottomBar = {
            BottomNavigation(
                currentRoute = currentRoute,
                //クリックされたら画面のルート名を受け取る
                onNavigate = { route ->

                    //同じ画面への遷移を防ぐ
                    if (currentRoute != route) {

                        //画面遷移実行
                        navController.navigate(route) {
                            //これがないと、画面遷移のたびに画面が積み重なってしまう
                            //startDestinationId：最初の画面のID
                            //スタックには常に[最初の画面, 現在の画面]だけが残る
                            popUpTo(navController.graph.startDestinationId) {

                                //遷移前の画面の状態を保存する
                                //(例: スクロール位置・入力フォーム内容・選択状態など)
                                saveState = true
                            }

                            //同じ画面を重複して開かない
                            //これがないと5回ホームボタンを押すと「ホーム」が5つ積まれる
                            launchSingleTop = true

                            //これがあることで、スクロール位置などを記憶し、画面遷移前の状態に戻せる
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues -> // Scaffoldの内のパディング値を取得
        AppNavHost(
            navController = navController,
            startDestination = "home",     // 最初に表示する画面
            modifier = Modifier.padding(paddingValues)  //パディングを適応
        )
    }
}