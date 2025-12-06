package com.example.androidbootcampiwatepref

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.androidbootcampiwatepref.data.ProfileDataStore
import com.example.androidbootcampiwatepref.domain.model.AppTheme
import com.example.androidbootcampiwatepref.navigation.ProfileRoutes
import com.example.androidbootcampiwatepref.ui.navigation.AppNavigationGraph
import com.example.androidbootcampiwatepref.ui.screen.SplashScreen
import com.example.androidbootcampiwatepref.ui.theme.AndroidBootcampIwatePrefTheme
import com.example.androidbootcampiwatepref.ui.viewmodel.MainViewModel

/**
 * メインアクティビティ
 * 
 * アプリのエントリーポイント。
 * ViewModelを介してデータ管理を行い、ナビゲーショングラフを表示する。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataStoreインスタンスを作成
        val profileDataStore = ProfileDataStore(this)
        
        // ViewModelを作成
        val viewModel = MainViewModel(profileDataStore)
        
        setContent {
            // ViewModelから状態を取得
            val isDataLoaded by viewModel.isDataLoaded.collectAsState()
            val currentTheme by viewModel.currentTheme.collectAsState()
            val currentFont by viewModel.currentFont.collectAsState()
            val currentCardDesign by viewModel.currentCardDesign.collectAsState()
            val profileData by viewModel.profileData.collectAsState()
            val profileImageUri by viewModel.profileImageUri.collectAsState()
            val profileImageOriginalUri by viewModel.profileImageOriginalUri.collectAsState()
            val headerImageUri by viewModel.headerImageUri.collectAsState()
            val savedCards by viewModel.savedCards.collectAsState()
            
            // テーマ設定に基づいてダークモード判定
            val isDarkTheme = when (currentTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }
            
            // アプリ全体のテーマを適用
            AndroidBootcampIwatePrefTheme(
                darkTheme = isDarkTheme,
                fontFamily = currentFont.fontFamily
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isDataLoaded) {
                        // スプラッシュ画面表示
                        SplashScreen()
                    } else {
                        // メインコンテンツ表示
                        MainContent(
                            viewModel = viewModel,
                            profileDataStore = profileDataStore,
                            profileData = profileData,
                            profileImageUri = profileImageUri ?: "",
                            profileImageOriginalUri = profileImageOriginalUri ?: "",
                            headerImageUri = headerImageUri ?: "",
                            currentTheme = currentTheme,
                            currentFont = currentFont,
                            currentCardDesign = currentCardDesign,
                            savedCards = savedCards
                        )
                    }
                }
            }
        }
    }
}

/**
 * メインコンテンツ（ナビゲーション + ボトムバー）
 */
@Composable
private fun MainContent(
    viewModel: MainViewModel,
    profileDataStore: ProfileDataStore,
    profileData: com.example.androidbootcampiwatepref.domain.model.ProfileData,
    profileImageUri: String,
    profileImageOriginalUri: String,
    headerImageUri: String,
    currentTheme: AppTheme,
    currentFont: com.example.androidbootcampiwatepref.domain.model.AppFont,
    currentCardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    savedCards: List<com.example.androidbootcampiwatepref.domain.model.BusinessCardData>
) {
    // タブの選択状態を管理
    var selectedTab by remember { mutableStateOf(0) }
    var previousTab by remember { mutableStateOf(0) }
    
    // ナビゲーションコントローラーを作成
    val navController = rememberNavController()
    
    // ボトムバーの定義
    val bottomBar: @Composable () -> Unit = {
        NavigationBar {
            // プロフィールタブ
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "プロフィール") },
                label = { Text("プロフィール") },
                selected = selectedTab == 0,
                onClick = {
                    previousTab = selectedTab
                    selectedTab = 0
                    navController.navigate(ProfileRoutes.View) {
                        popUpTo(ProfileRoutes.View) { inclusive = true }
                    }
                }
            )
            // QRスキャンタブ
            NavigationBarItem(
                icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "QRスキャン") },
                label = { Text("QRスキャン") },
                selected = selectedTab == 1,
                onClick = {
                    previousTab = selectedTab
                    selectedTab = 1
                    navController.navigate(ProfileRoutes.QRCodeScanner) {
                        popUpTo(ProfileRoutes.View)
                    }
                }
            )
            // 名刺ホルダータブ
            NavigationBarItem(
                icon = { Icon(Icons.Default.ContactPage, contentDescription = "名刺ホルダー") },
                label = { Text("名刺ホルダー") },
                selected = selectedTab == 2,
                onClick = {
                    previousTab = selectedTab
                    selectedTab = 2
                    navController.navigate(ProfileRoutes.CardHolder) {
                        popUpTo(ProfileRoutes.View)
                    }
                }
            )
        }
    }
    
    // ナビゲーショングラフを表示
    AppNavigationGraph(
        navController = navController,
        profileDataStore = profileDataStore,
        lifecycleScope = androidx.compose.ui.platform.LocalLifecycleOwner.current.lifecycleScope,
        profileData = profileData,
        profileImageUri = profileImageUri,
        profileImageOriginalUri = profileImageOriginalUri,
        headerImageUri = headerImageUri,
        currentTheme = currentTheme,
        currentFont = currentFont,
        currentCardDesign = currentCardDesign,
        savedCards = savedCards,
        selectedTab = selectedTab,
        previousTab = previousTab,
        bottomBar = bottomBar
    )
}
