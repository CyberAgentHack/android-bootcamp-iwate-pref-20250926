package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import coil.compose.AsyncImage
import com.example.androidbootcampiwatepref.R
import com.example.androidbootcampiwatepref.domain.model.ProfileData
import com.example.androidbootcampiwatepref.domain.model.GENDER_OPTIONS
import com.example.androidbootcampiwatepref.domain.model.BIRTH_DATE_FORMATTER
import com.example.androidbootcampiwatepref.ui.component.ContactInfoDisplay
import com.example.androidbootcampiwatepref.domain.model.UNSET_TEXT
import com.example.androidbootcampiwatepref.domain.model.GenderOption
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
    onQRCodeClick: () -> Unit
) {
    // Scaffoldを使用して基本的な画面レイアウトを構築
    Scaffold(
        topBar = {
            // トップバー: タイトルと操作ボタンを配置
            TopAppBar(
                title = { Text("プロフィール") },
                actions = {
                    // QRコード表示ボタン
                    IconButton(onClick = onQRCodeClick) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "QRコード表示")
                    }
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
            onQRCodeClick = onQRCodeClick
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
    onQRCodeClick: () -> Unit
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
    
    // 共通定数を使用（ProfileConstants.ktで定義）
    val genderOptions = GENDER_OPTIONS
    val birthDateFormatter = BIRTH_DATE_FORMATTER

    // 中央に配置
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
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
            defaultImageRes = null,
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // アイコン - 視認性向上のため140dpに拡大
        if (profileImageUri != null) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = "プロフィール画像",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onImageClick),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(cardDesign.frontTextColor.copy(alpha = 0.1f))
                    .border(3.dp, cardDesign.frontTextColor.copy(alpha = 0.3f), CircleShape)
                    .clickable(onClick = onImageClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "プロフィール画像",
                    modifier = Modifier.size(80.dp),
                    tint = cardDesign.frontTextColor.copy(alpha = 0.5f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // ニックネーム - 視覚的階層の最上位
        Text(
            text = profileData.nickname.ifEmpty { "名前未設定" },
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = cardDesign.frontTextColor,
            letterSpacing = 0.5.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 自己紹介 - 表面に表示
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = profileData.bio.ifEmpty { "自己紹介未設定" },
                style = MaterialTheme.typography.bodyLarge,
                color = cardDesign.frontTextColor.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 戻る案内
        Text(
            text = "← タップして表面へ",
            style = MaterialTheme.typography.bodySmall,
            color = cardDesign.backTextColor.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 基本情報
        InfoItem(
            icon = "⚥",
            label = "性別",
            value = genderOptions[profileData.genderIndex],
            textColor = cardDesign.backTextColor
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        InfoItem(
            icon = "🎂",
            label = "生年月日",
            value = profileData.birthDateMillis?.let {
                birthDateFormatter.format(Date(it))
            } ?: UNSET_TEXT,
            textColor = cardDesign.backTextColor
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 趣味 - セクション階層の明確化
        Column {
            Text(
                text = "🎨 趣味・興味",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = cardDesign.backTextColor.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            if (profileData.hobbies.isEmpty()) {
                Text(
                    text = UNSET_TEXT,
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
        
        // 連絡先表示セクション
        // 電話番号またはメールアドレスが設定されている場合のみ表示
        val context = LocalContext.current
        val hasContactInfo = profileData.phoneNumber.isNotEmpty() || profileData.email.isNotEmpty()
        
        if (hasContactInfo) {
            Spacer(modifier = Modifier.height(16.dp))
            
            ContactInfoDisplay(
                phoneNumber = profileData.phoneNumber,
                email = profileData.email,
                textColor = cardDesign.backTextColor
            )
        }
        
        // SNSリンク表示セクション
        // 登録されているSNS URLがある場合のみ表示
        // いずれかのSNS URLが設定されているかチェック
        val hasSnsLinks = profileData.twitterUrl.isNotEmpty() || 
                         profileData.instagramUrl.isNotEmpty() || 
                         profileData.facebookUrl.isNotEmpty() || 
                         profileData.lineUrl.isNotEmpty()
        
        if (hasSnsLinks) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Column {
                Text(
                    text = "🔗 SNS・リンク",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = cardDesign.backTextColor.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                // SNSアイコンを横並びで表示 - タップターゲット最適化
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Twitter/X アイコン(URLが設定されている場合のみ表示)
                    if (profileData.twitterUrl.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                // ブラウザでTwitter/XのプロフィールURLを開く
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(profileData.twitterUrl))
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_twitter),
                                contentDescription = "Twitter/X",
                                tint = cardDesign.backTextColor,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    if (profileData.instagramUrl.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(profileData.instagramUrl))
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_instagram),
                                contentDescription = "Instagram",
                                tint = cardDesign.backTextColor,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    if (profileData.facebookUrl.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(profileData.facebookUrl))
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_facebook),
                                contentDescription = "Facebook",
                                tint = cardDesign.backTextColor,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    if (profileData.lineUrl.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(profileData.lineUrl))
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "LINE",
                                tint = cardDesign.backTextColor,
                                modifier = Modifier.size(40.dp)
                            )
                        }
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
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.width(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor.copy(alpha = 0.7f),
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
