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
 * プロフィール画面のナビゲーションとDataStore統合を管理
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataStoreインスタンスを作成
        val profileDataStore = ProfileDataStore(this)
        
        setContent{
            //状態管理
            var currentTheme by remember { mutableStateOf(AppTheme.SYSTEM) }

            //プロフィールデータ全体を管理
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
            
            // DataStoreからデータを読み込む
            LaunchedEffect(Unit) {
                combine(
                    profileDataStore.nicknameFlow,
                    profileDataStore.idFlow,
                    profileDataStore.bioFlow,
                    profileDataStore.genderIndexFlow,
                    profileDataStore.birthDateFlow,
                    profileDataStore.hobbiesFlow,
                    profileDataStore.themeFlow
                ) { values ->
                    val nickname = values[0] as String
                    val id = values[1] as String
                    val bio = values[2] as String
                    val genderIndex = values[3] as Int
                    val birthDate = values[4] as Long?
                    val hobbies = values[5] as List<*>
                    val theme = values[6] as String
                    
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
                }.collect {}
            }
            //システムがダークモードかどうかを取得
            val systemIsDark = isSystemInDarkTheme()
            //実際にテーマに渡す
            val useDarkTheme = when (currentTheme) {
                AppTheme.SYSTEM -> systemIsDark
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }
            AndroidBootcampIwatePrefTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = ProfileRoutes.View
                    ) {
                        composable<ProfileRoutes.View> {
                            ProfileViewScreen(
                                profileData = profileData,
                                useDarkTheme = useDarkTheme,
                                onThemeToggle = {
                                    currentTheme = if (useDarkTheme) AppTheme.LIGHT else AppTheme.DARK
                                    // テーマ設定を保存
                                    lifecycleScope.launch {
                                        profileDataStore.saveTheme(currentTheme.name)
                                    }
                                },
                                onEditClick = {
                                    navController.navigate(ProfileRoutes.Edit)
                                }
                            )
                        }

                        composable<ProfileRoutes.Edit> {
                            ProfileEditScreen(
                                profileData = profileData,
                                useDarkTheme = useDarkTheme,
                                onThemeToggle = {
                                    currentTheme = if (useDarkTheme) AppTheme.LIGHT else AppTheme.DARK
                                    // テーマ設定を保存
                                    lifecycleScope.launch {
                                        profileDataStore.saveTheme(currentTheme.name)
                                    }
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onSaveClick = { updatedData ->
                                    profileData = updatedData
                                    // DataStoreに保存
                                    lifecycleScope.launch {
                                        profileDataStore.saveProfileData(
                                            nickname = updatedData.nickname,
                                            id = updatedData.id,
                                            bio = updatedData.bio,
                                            genderIndex = updatedData.genderIndex,
                                            birthDateMillis = updatedData.birthDateMillis,
                                            hobbies = updatedData.hobbies
                                        )
                                    }
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