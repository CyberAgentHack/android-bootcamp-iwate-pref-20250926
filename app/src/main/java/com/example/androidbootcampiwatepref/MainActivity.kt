package com.example.androidbootcampiwatepref

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.*
import com.example.androidbootcampiwatepref.data.ProfileDataStore
import com.example.androidbootcampiwatepref.domain.model.*
import com.example.androidbootcampiwatepref.navigation.ProfileRoutes
import com.example.androidbootcampiwatepref.ui.screen.*
import com.example.androidbootcampiwatepref.ui.theme.AndroidBootcampIwatePrefTheme
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * メインアクティビティ
 * 
 * このアクティビティは以下の責務を持つ:
 * - プロフィール画面間のナビゲーション管理
 * - DataStoreを使用したデータの永続化
 * - テーマ設定の管理と保存
 * - アプリ全体の状態管理
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataStoreインスタンスを作成（データ永続化に使用）
        val profileDataStore = ProfileDataStore(this)
        
        setContent{
            // 現在のテーマ設定を管理（SYSTEM/LIGHT/DARK）
            var currentTheme by remember { mutableStateOf(AppTheme.SYSTEM) }

            // プロフィールデータ全体を管理（初期値として空のデータを設定）
            var profileData by remember {
                mutableStateOf(
                    ProfileData(
                        nickname = "",
                        id ="",
                        bio = "",
                        genderIndex = 0,
                        birthDateMillis = Calendar.getInstance().apply{ set(2000,0,1) }.timeInMillis,
                        hobbies = emptyList()
                    )
                )
            }
            
            // 画像URIの状態管理
            var profileImageUri by remember { mutableStateOf<String?>(null) }
            var headerImageUri by remember { mutableStateOf<String?>(null) }
            
            /**
             * DataStoreからデータを読み込む
             * 
             * LaunchedEffect(Unit)により、Composableの初回表示時に一度だけ実行される。
             * combine関数で複数のFlowを結合し、いずれかの値が変更されたら
             * 全てのデータを取得して状態を更新する。
             */
            LaunchedEffect(Unit) {
                combine(
                    profileDataStore.nicknameFlow,
                    profileDataStore.idFlow,
                    profileDataStore.bioFlow,
                    profileDataStore.genderIndexFlow,
                    profileDataStore.birthDateFlow,
                    profileDataStore.hobbiesFlow,
                    profileDataStore.themeFlow,
                    profileDataStore.profileImageUriFlow,
                    profileDataStore.headerImageUriFlow
                ) { values ->
                    val nickname = values[0] as String
                    val id = values[1] as String
                    val bio = values[2] as String
                    val genderIndex = values[3] as Int
                    val birthDate = values[4] as Long?
                    val hobbies = values[5] as List<*>
                    val theme = values[6] as String
                    val profImageUri = values[7] as String?
                    val headImageUri = values[8] as String?
                    
                    profileData = ProfileData(
                        nickname = nickname,
                        id = id,
                        bio = bio,
                        genderIndex = genderIndex,
                        birthDateMillis = birthDate ?: Calendar.getInstance().apply{ set(2000,0,1) }.timeInMillis,
                        hobbies = hobbies.filterIsInstance<String>()
                    )
                    currentTheme = try {
                        AppTheme.valueOf(theme)
                    } catch (e: IllegalArgumentException) {
                        AppTheme.SYSTEM
                    }
                    profileImageUri = profImageUri
                    headerImageUri = headImageUri
                }.collect {}
            }
            
            // システムの現在のダークモード設定を取得
            val systemIsDark = isSystemInDarkTheme()
            
            // 実際に適用するテーマを決定
            // SYSTEM: システム設定に従う、LIGHT: 強制ライト、DARK: 強制ダーク
            val useDarkTheme = when (currentTheme) {
                AppTheme.SYSTEM -> systemIsDark
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }
            
            // アプリのテーマを適用
            AndroidBootcampIwatePrefTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ナビゲーションコントローラーを作成（画面遷移を管理）
                    val navController = rememberNavController()

                    // ナビゲーショングラフを定義（開始画面はView画面）
                    NavHost(
                        navController = navController,
                        startDestination = ProfileRoutes.View
                    ) {
                        // プロフィール閲覧画面の定義
                        composable<ProfileRoutes.View> {
                            ProfileViewScreen(
                                profileData = profileData,
                                profileImageUri = profileImageUri,
                                headerImageUri = headerImageUri,
                                useDarkTheme = useDarkTheme,
                                // テーマ切り替えボタンが押された時の処理
                                onThemeToggle = {
                                    // 現在のテーマに応じて次のテーマに切り替え
                                    currentTheme = if (useDarkTheme) AppTheme.LIGHT else AppTheme.DARK
                                    // 変更したテーマ設定をDataStoreに保存
                                    lifecycleScope.launch {
                                        profileDataStore.saveTheme(currentTheme.name)
                                    }
                                },
                                // 編集ボタンが押された時の処理
                                onEditClick = {
                                    // 編集画面に遷移
                                    navController.navigate(ProfileRoutes.Edit)
                                }
                            )
                        }

                        // プロフィール編集画面の定義
                        composable<ProfileRoutes.Edit> {
                            ProfileEditScreen(
                                profileData = profileData,
                                profileImageUri = profileImageUri,
                                headerImageUri = headerImageUri,
                                useDarkTheme = useDarkTheme,
                                onThemeToggle = {
                                    currentTheme = if (useDarkTheme) AppTheme.LIGHT else AppTheme.DARK
                                    // テーマ設定を保存
                                    lifecycleScope.launch {
                                        profileDataStore.saveTheme(currentTheme.name)
                                    }
                                },
                                // 戻るボタンが押された時の処理
                                onBackClick = {
                                    // 前の画面に戻る（変更は保存されない）
                                    navController.popBackStack()
                                },
                                // 保存ボタンが押された時の処理
                                onSaveClick = { updatedData, newProfileImageUri, newHeaderImageUri ->
                                    // アプリ内の状態を更新
                                    profileData = updatedData
                                    profileImageUri = newProfileImageUri
                                    headerImageUri = newHeaderImageUri
                                    
                                    // 変更したプロフィールデータをDataStoreに永続化
                                    lifecycleScope.launch {
                                        profileDataStore.saveProfileData(
                                            nickname = updatedData.nickname,
                                            id = updatedData.id,
                                            bio = updatedData.bio,
                                            genderIndex = updatedData.genderIndex,
                                            birthDateMillis = updatedData.birthDateMillis,
                                            hobbies = updatedData.hobbies
                                        )
                                        // 画像URIも保存
                                        profileDataStore.saveProfileImageUri(newProfileImageUri)
                                        profileDataStore.saveHeaderImageUri(newHeaderImageUri)
                                    }
                                    // 閲覧画面に戻る
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}