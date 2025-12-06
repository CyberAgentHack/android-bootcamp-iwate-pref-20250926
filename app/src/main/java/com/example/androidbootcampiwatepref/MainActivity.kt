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
import androidx.navigation.toRoute
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import com.example.androidbootcampiwatepref.data.ProfileDataStore
import com.example.androidbootcampiwatepref.domain.model.*
import com.example.androidbootcampiwatepref.domain.model.DEFAULT_BIRTH_DATE_MILLIS
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
                        bio = "",
                        genderIndex = 0,
                        birthDateMillis = DEFAULT_BIRTH_DATE_MILLIS,
                        hobbies = emptyList()
                    )
                )
            }
            
            // 画像URIの状態管理
            var profileImageUri by remember { mutableStateOf<String?>(null) }
            var profileImageOriginalUri by remember { mutableStateOf<String?>(null) }
            var headerImageUri by remember { mutableStateOf<String?>(null) }
            
            /**
             * DataStoreからデータを読み込む
             * 
             * LaunchedEffect(Unit)により、Composableの初回表示時に一度だけ実行される。
             * combine関数で複数のFlowを結合し、いずれかの値が変更されたら
             * 全てのデータを取得して状態を更新する。
             */
            // フォント設定を管理
            var currentFont by remember { mutableStateOf(AppFont.DEFAULT) }
            
            // カードデザイン設定を管理
            var currentCardDesign by remember { mutableStateOf(com.example.androidbootcampiwatepref.domain.model.CardDesign.CLASSIC) }
            
            // 受け取った名刺リストを管理
            var savedCards by remember { mutableStateOf<List<BusinessCardData>>(emptyList()) }
            
            LaunchedEffect(Unit) {
                combine(
                    profileDataStore.nicknameFlow,
                    profileDataStore.bioFlow,
                    profileDataStore.genderIndexFlow,
                    profileDataStore.birthDateFlow,
                    profileDataStore.hobbiesFlow,
                    profileDataStore.themeFlow,
                    profileDataStore.fontFlow,
                    profileDataStore.profileImageUriFlow,
                    profileDataStore.profileImageOriginalUriFlow,
                    profileDataStore.headerImageUriFlow,
                    profileDataStore.cardDesignFlow,
                    profileDataStore.savedCardsFlow
                ) { values ->
                    val nickname = values[0] as String
                    val bio = values[1] as String
                    val genderIndex = values[2] as Int
                    val birthDate = values[3] as Long?
                    val hobbies = values[4] as List<*>
                    val theme = values[5] as String
                    val font = values[6] as String
                    val profImageUri = values[7] as String?
                    val profImageOriginalUri = values[8] as String?
                    val headImageUri = values[9] as String?
                    val cardDesign = values[10] as String
                    @Suppress("UNCHECKED_CAST")
                    val cards = values[11] as List<BusinessCardData>
                    
                    profileData = ProfileData(
                        nickname = nickname,
                        bio = bio,
                        genderIndex = genderIndex,
                        birthDateMillis = birthDate ?: DEFAULT_BIRTH_DATE_MILLIS,
                        hobbies = hobbies.filterIsInstance<String>()
                    )
                    currentTheme = try {
                        AppTheme.valueOf(theme)
                    } catch (e: IllegalArgumentException) {
                        AppTheme.SYSTEM
                    }
                    currentFont = try {
                        AppFont.valueOf(font)
                    } catch (e: IllegalArgumentException) {
                        AppFont.DEFAULT
                    }
                    currentCardDesign = com.example.androidbootcampiwatepref.domain.model.CardDesign.fromName(cardDesign)
                    profileImageUri = profImageUri
                    profileImageOriginalUri = profImageOriginalUri
                    headerImageUri = headImageUri
                    savedCards = cards
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
            
            // フォントファミリーの決定
            val fontFamily = when (currentFont) {
                AppFont.DEFAULT -> androidx.compose.ui.text.font.FontFamily.Default
                AppFont.SERIF -> androidx.compose.ui.text.font.FontFamily.Serif
                AppFont.MONOSPACE -> androidx.compose.ui.text.font.FontFamily.Monospace
                AppFont.CURSIVE -> androidx.compose.ui.text.font.FontFamily.Cursive
            }
            
            // アプリのテーマを適用
            AndroidBootcampIwatePrefTheme(darkTheme = useDarkTheme, fontFamily = fontFamily) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ナビゲーションコントローラーを作成（画面遷移を管理）
                    val navController = rememberNavController()

                    // ナビゲーショングラフを定義（開始画面はView画面）
                    NavHost(
                        navController = navController,
                        startDestination = ProfileRoutes.View,
                        enterTransition = {
                            // 右から左にスライドイン（ゆっくり）
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        },
                        exitTransition = {
                            // 左にスライドアウト
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        },
                        popEnterTransition = {
                            // 戻る時は左から右にスライドイン（ゆっくり）
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        },
                        popExitTransition = {
                            // 戻る時は右にスライドアウト
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }
                    ) {
                        // プロフィール閲覧画面の定義
                        composable<ProfileRoutes.View> {
                            ProfileViewScreen(
                                profileData = profileData,
                                profileImageUri = profileImageUri,
                                profileImageOriginalUri = profileImageOriginalUri,
                                currentCardDesign = currentCardDesign,
                                // 編集ボタンが押された時の処理
                                onEditClick = {
                                    // 編集画面に遷移
                                    navController.navigate(ProfileRoutes.Edit)
                                },
                                // 設定ボタンが押された時の処理
                                onSettingsClick = {
                                    // 設定画面に遷移
                                    navController.navigate(ProfileRoutes.Settings)
                                },
                                // QRコード表示ボタンが押された時の処理
                                onQRCodeClick = {
                                    navController.navigate(ProfileRoutes.QRCodeDisplay)
                                }
                            )
                        }

                        // プロフィール編集画面の定義
                        composable<ProfileRoutes.Edit> {
                            ProfileEditScreen(
                                profileData = profileData,
                                profileImageUri = profileImageUri,
                                headerImageUri = headerImageUri,
                                currentCardDesign = currentCardDesign,
                                // 戻るボタンが押された時の処理
                                onBackClick = {
                                    // 前の画面に戻る（変更は保存されない）
                                    navController.popBackStack()
                                },
                                // 保存ボタンが押された時の処理
                                onSaveClick = { updatedData, newProfileImageUri, newProfileImageOrigUri, newHeaderImageUri ->
                                    // アプリ内の状態を更新
                                    profileData = updatedData
                                    profileImageUri = newProfileImageUri
                                    profileImageOriginalUri = newProfileImageOrigUri
                                    headerImageUri = newHeaderImageUri
                                    
                                    // 変更したプロフィールデータをDataStoreに永続化
                                    lifecycleScope.launch {
                                        profileDataStore.saveProfileData(
                                            nickname = updatedData.nickname,
                                            bio = updatedData.bio,
                                            genderIndex = updatedData.genderIndex,
                                            birthDateMillis = updatedData.birthDateMillis,
                                            hobbies = updatedData.hobbies
                                        )
                                        // 画像URIも保存
                                        profileDataStore.saveProfileImageUri(newProfileImageUri)
                                        profileDataStore.saveProfileImageOriginalUri(newProfileImageOrigUri)
                                        profileDataStore.saveHeaderImageUri(newHeaderImageUri)
                                    }
                                    // 閲覧画面に戻る
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        // 設定画面の定義
                        composable<ProfileRoutes.Settings> {
                            SettingsScreen(
                                currentTheme = currentTheme,
                                currentFont = currentFont,
                                currentCardDesign = currentCardDesign,
                                onThemeChange = { newTheme ->
                                    currentTheme = newTheme
                                    lifecycleScope.launch {
                                        profileDataStore.saveTheme(newTheme.name)
                                    }
                                },
                                onFontChange = { newFont ->
                                    currentFont = newFont
                                    lifecycleScope.launch {
                                        profileDataStore.saveFont(newFont.name)
                                    }
                                },
                                onCardDesignChange = { newDesign ->
                                    currentCardDesign = newDesign
                                    lifecycleScope.launch {
                                        profileDataStore.saveCardDesign(newDesign.name)
                                    }
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        // QRコード表示画面
                        composable<ProfileRoutes.QRCodeDisplay> {
                            QRCodeDisplayScreen(
                                nickname = profileData.nickname,
                                bio = profileData.bio,
                                genderIndex = profileData.genderIndex,
                                birthDateMillis = profileData.birthDateMillis,
                                hobbies = profileData.hobbies,
                                cardDesign = currentCardDesign.name,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToScanner = {
                                    navController.navigate(ProfileRoutes.QRCodeScanner)
                                }
                            )
                        }
                        
                        // QRコードスキャン画面
                        composable<ProfileRoutes.QRCodeScanner> {
                            QRCodeScannerScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onQRCodeScanned = { qrContent ->
                                    // QRコードからBusinessCardDataをパース
                                    val scannedCard = BusinessCardData.fromJson(qrContent)
                                    if (scannedCard != null) {
                                        // 名刺を保存リストに追加
                                        val updatedCards = savedCards + scannedCard
                                        savedCards = updatedCards
                                        lifecycleScope.launch {
                                            profileDataStore.saveSavedCards(updatedCards)
                                        }
                                        // 名刺ホルダー画面に遷移
                                        navController.navigate(ProfileRoutes.CardHolder) {
                                            popUpTo(ProfileRoutes.QRCodeDisplay) { inclusive = false }
                                        }
                                    } else {
                                        // パース失敗時は戻る
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                        
                        // 名刺ホルダー画面
                        composable<ProfileRoutes.CardHolder> {
                            CardHolderScreen(
                                savedCards = savedCards,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onDeleteCard = { cardToDelete ->
                                    val updatedCards = savedCards.filter { it != cardToDelete }
                                    savedCards = updatedCards
                                    lifecycleScope.launch {
                                        profileDataStore.saveSavedCards(updatedCards)
                                    }
                                },
                                onCardClick = { card ->
                                    val index = savedCards.indexOf(card)
                                    if (index >= 0) {
                                        navController.navigate(ProfileRoutes.CardDetail(index))
                                    }
                                }
                            )
                        }
                        
                        // 名刺詳細画面
                        composable<ProfileRoutes.CardDetail> { backStackEntry ->
                            val cardDetail = backStackEntry.toRoute<ProfileRoutes.CardDetail>()
                            val card = savedCards.getOrNull(cardDetail.cardIndex)
                            if (card != null) {
                                CardDetailScreen(
                                    card = card,
                                    onNavigateBack = {
                                        navController.popBackStack()
                                    }
                                )
                            } else {
                                // カードが見つからない場合は戻る
                                LaunchedEffect(Unit) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}