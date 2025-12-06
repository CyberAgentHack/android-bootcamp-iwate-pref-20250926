package com.example.androidbootcampiwatepref.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.androidbootcampiwatepref.data.ProfileDataStore
import com.example.androidbootcampiwatepref.domain.model.*
import com.example.androidbootcampiwatepref.navigation.ProfileRoutes
import com.example.androidbootcampiwatepref.ui.screen.*
import kotlinx.coroutines.launch

/**
 * アプリのメインナビゲーショングラフ
 * 
 * @param navController ナビゲーションコントローラー
 * @param profileDataStore データ永続化用DataStore
 * @param lifecycleScope CoroutineScope
 * @param profileData 現在のプロフィールデータ
 * @param profileImageUri プロフィール画像URI
 * @param profileImageOriginalUri 元のプロフィール画像URI
 * @param headerImageUri ヘッダー画像URI
 * @param currentTheme 現在のテーマ
 * @param currentFont 現在のフォント
 * @param currentCardDesign 現在のカードデザイン
 * @param savedCards 保存された名刺リスト
 * @param bottomBar ボトムバーのComposable
 */
@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    profileDataStore: ProfileDataStore,
    lifecycleScope: LifecycleCoroutineScope,
    profileData: ProfileData,
    profileImageUri: String,
    profileImageOriginalUri: String,
    headerImageUri: String,
    currentTheme: AppTheme,
    currentFont: AppFont,
    currentCardDesign: CardDesign,
    savedCards: List<BusinessCardData>,
    selectedTab: Int,
    previousTab: Int,
    bottomBar: @Composable () -> Unit
) {

    // Scaffoldで画面全体を構築（下部タブバー付き）
    Scaffold(
        bottomBar = bottomBar
    ) { paddingValues ->
        // ナビゲーショングラフを定義（開始画面はView画面）
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = ProfileRoutes.View,
            enterTransition = {
                // タブの位置関係でスライド方向を決定
                val direction = if (selectedTab > previousTab) {
                    AnimatedContentTransitionScope.SlideDirection.Left
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }
                slideIntoContainer(
                    towards = direction,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                val direction = if (selectedTab > previousTab) {
                    AnimatedContentTransitionScope.SlideDirection.Left
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }
                slideOutOfContainer(
                    towards = direction,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
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
                    onEditClick = { navController.navigate(ProfileRoutes.Edit) },
                    onSettingsClick = { navController.navigate(ProfileRoutes.Settings) },
                    onQRCodeClick = { navController.navigate(ProfileRoutes.QRCodeDisplay) }
                )
            }

            // プロフィール編集画面の定義
            composable<ProfileRoutes.Edit> {
                ProfileEditScreen(
                    profileData = profileData,
                    profileImageUri = profileImageUri,
                    headerImageUri = headerImageUri,
                    currentCardDesign = currentCardDesign,
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { updatedData, newProfileImageUri, newProfileImageOrigUri, newHeaderImageUri ->
                        lifecycleScope.launch {
                            profileDataStore.saveProfileData(
                                nickname = updatedData.nickname,
                                bio = updatedData.bio,
                                genderIndex = updatedData.genderIndex,
                                birthDateMillis = updatedData.birthDateMillis,
                                hobbies = updatedData.hobbies
                            )
                            profileDataStore.saveProfileImageUri(newProfileImageUri)
                            profileDataStore.saveProfileImageOriginalUri(newProfileImageOrigUri)
                            profileDataStore.saveHeaderImageUri(newHeaderImageUri)
                        }
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
                        lifecycleScope.launch {
                            profileDataStore.saveTheme(newTheme.name)
                        }
                    },
                    onFontChange = { newFont ->
                        lifecycleScope.launch {
                            profileDataStore.saveFont(newFont.name)
                        }
                    },
                    onCardDesignChange = { newDesign ->
                        lifecycleScope.launch {
                            profileDataStore.saveCardDesign(newDesign.name)
                        }
                    },
                    onBackClick = { navController.popBackStack() }
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
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToScanner = { navController.navigate(ProfileRoutes.QRCodeScanner) }
                )
            }
            
            // QRコードスキャン画面
            composable<ProfileRoutes.QRCodeScanner> {
                QRCodeScannerScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onQRCodeScanned = { qrContent ->
                        val scannedCard = BusinessCardData.fromJson(qrContent)
                        if (scannedCard != null) {
                            val updatedCards = savedCards + scannedCard
                            lifecycleScope.launch {
                                profileDataStore.saveSavedCards(updatedCards)
                            }
                            navController.navigate(ProfileRoutes.CardHolder) {
                                popUpTo(ProfileRoutes.QRCodeDisplay) { inclusive = false }
                            }
                        } else {
                            navController.popBackStack()
                        }
                    }
                )
            }
            
            // 名刺ホルダー画面
            composable<ProfileRoutes.CardHolder> {
                CardHolderScreen(
                    savedCards = savedCards,
                    onNavigateBack = { navController.popBackStack() },
                    onDeleteCard = { cardToDelete ->
                        val updatedCards = savedCards.filter { it != cardToDelete }
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
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        } // NavHost
    } // Scaffold
}
