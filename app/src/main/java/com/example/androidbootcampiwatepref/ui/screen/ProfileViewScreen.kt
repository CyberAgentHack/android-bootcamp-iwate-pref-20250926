package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.androidbootcampiwatepref.R
import com.example.androidbootcampiwatepref.domain.model.ProfileData
import com.example.androidbootcampiwatepref.ui.component.ProfileHeader
import com.example.androidbootcampiwatepref.ui.component.ProfileInfoRow
import com.example.androidbootcampiwatepref.ui.component.ImageViewerDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * プロフィール閲覧画面
 * 
 * ユーザーのプロフィール情報を表示する読み取り専用の画面
 * 
 * 主な機能:
 * - プロフィール情報（ニックネーム、ID、性別、誕生日、自己紹介、趣味）の表示
 * - 編集画面への遷移
 * - テーマ切り替え（ライトモード/ダークモード）
 * 
 * @param profileData 表示するプロフィールデータ
 * @param useDarkTheme ダークテーマを使用するかどうか
 * @param onThemeToggle テーマ切り替えボタンのクリック処理
 * @param onEditClick 編集ボタンのクリック処理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewScreen(
    profileData: ProfileData,
    profileImageUri: String?,
    profileImageOriginalUri: String?,
    currentCardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onQRCodeClick: () -> Unit,
    onCardHolderClick: () -> Unit
) {
    // Scaffoldを使用して基本的な画面レイアウトを構築
    Scaffold(
        topBar = {
            // トップバー: タイトルと操作ボタンを配置
            TopAppBar(
                title = { Text("プロフィール") },
                actions = {
                    // 編集ボタン
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "編集")
                    }
                    // 設定ボタン
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
            )
        }
    ) { innerPadding ->
        // メインコンテンツの表示
        ProfileViewContent(
            innerPadding = innerPadding,
            profileData = profileData,
            profileImageUri = profileImageUri,
            profileImageOriginalUri = profileImageOriginalUri,
            cardDesign = currentCardDesign,
            onQRCodeClick = onQRCodeClick,
            onCardHolderClick = onCardHolderClick
        )
    }
}

/**
 * プロフィール閲覧画面のコンテンツ（名刺風デザイン）
 * 
 * 名刺のような表裏デザインで情報を表示
 * 表面: アイコン、ニックネーム、ID
 * 裏面: 詳細情報（性別、誕生日、自己紹介、趣味）
 * 
 * @param innerPadding Scaffoldから渡されるPadding
 * @param profileData 表示するプロフィールデータ
 */
@Composable
fun ProfileViewContent(
    innerPadding: PaddingValues, 
    profileData: ProfileData,
    profileImageUri: String?,
    profileImageOriginalUri: String?,
    cardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onQRCodeClick: () -> Unit,
    onCardHolderClick: () -> Unit
) {
    // カードの表裏状態を管理
    var isFlipped by remember { mutableStateOf(false) }
    
    // 回転アニメーション（0度→180度）
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "card_flip"
    )
    
    // 画像拡大表示の状態管理
    var showProfileImageViewer by remember { mutableStateOf(false) }
    
    // 性別の選択肢リスト
    val genderOptions = listOf("男性", "女性", "回答しない")
    
    // 日付フォーマッター
    val birthDateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN) }

    // 中央に配置
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // QRコードと名刺ホルダーボタン
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onQRCodeClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("QRコード表示")
            }
            
            Button(
                onClick = onCardHolderClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("名刺ホルダー")
            }
        }
        
        // 名刺カード（表裏反転）
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.63f) // 名刺の比率（横:縦 = 91:57mm ≈ 1.6:1の逆）
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clickable { isFlipped = !isFlipped },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            // 前半90度は表面、後半90度は裏面を表示
            if (rotation <= 90f) {
                // 表面（シンプル）
                BusinessCardFront(
                    profileData = profileData,
                    profileImageUri = profileImageUri,
                    cardDesign = cardDesign,
                    onImageClick = { showProfileImageViewer = true }
                )
            } else {
                // 裏面（詳細情報）- 鏡像反転を修正
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        }
                ) {
                    BusinessCardBack(
                        profileData = profileData,
                        genderOptions = genderOptions,
                        birthDateFormatter = birthDateFormatter,
                        cardDesign = cardDesign
                    )
                }
            }
        }
    }
    
    // プロフィール画像の拡大表示ダイアログ
    if (showProfileImageViewer) {
        ImageViewerDialog(
            imageUri = profileImageOriginalUri ?: profileImageUri,
            defaultImageRes = R.drawable.ic_my_icon,
            contentDescription = "プロフィール画像",
            onDismiss = { showProfileImageViewer = false }
        )
    }
}

/**
 * 名刺の表面デザイン
 */
@Composable
fun BusinessCardFront(
    profileData: ProfileData,
    profileImageUri: String?,
    cardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cardDesign.frontBrush)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // アイコン
        if (profileImageUri != null) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = "プロフィール画像",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onImageClick),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_my_icon),
                contentDescription = "プロフィール画像",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onImageClick),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // ニックネーム（大きく）
        Text(
            text = profileData.nickname.ifEmpty { "名前未設定" },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = cardDesign.frontTextColor
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 裏面への案内
        Text(
            text = "タップして詳細を表示 →",
            style = MaterialTheme.typography.bodySmall,
            color = cardDesign.frontTextColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 名刺の裏面デザイン
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BusinessCardBack(
    profileData: ProfileData,
    genderOptions: List<String>,
    birthDateFormatter: SimpleDateFormat,
    cardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cardDesign.backBrush)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 戻る案内
        Text(
            text = "← タップして表面へ",
            style = MaterialTheme.typography.bodySmall,
            color = cardDesign.backTextColor.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 基本情報
        InfoItem(
            icon = "🎂",
            label = "生年月日",
            value = profileData.birthDateMillis?.let {
                birthDateFormatter.format(Date(it))
            } ?: "未設定",
            textColor = cardDesign.backTextColor
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        InfoItem(
            icon = "⚥",
            label = "性別",
            value = genderOptions[profileData.genderIndex],
            textColor = cardDesign.backTextColor
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 自己紹介
        Column {
            Text(
                text = "📝 自己紹介",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = cardDesign.backTextColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = profileData.bio.ifEmpty { "未設定" },
                style = MaterialTheme.typography.bodyMedium,
                color = cardDesign.backTextColor
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 趣味
        Column {
            Text(
                text = "🎨 趣味・興味",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = cardDesign.backTextColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            if (profileData.hobbies.isEmpty()) {
                Text(
                    text = "未設定",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cardDesign.backTextColor
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    profileData.hobbies.forEach { hobby ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(hobby) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 情報項目表示用コンポーネント
 */
@Composable
fun InfoItem(icon: String, label: String, value: String, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.width(32.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}
