package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.androidbootcampiwatepref.R
import com.example.androidbootcampiwatepref.domain.model.BusinessCardData
import com.example.androidbootcampiwatepref.domain.model.CardDesign
import com.example.androidbootcampiwatepref.ui.component.ContactInfoDisplay

/**
 * 受け取った名刺の詳細表示画面
 *
 * 名刺をタップすると表裏が反転するアニメーション付き
 * 表面：ニックネームと自己紹介
 * 裏面：性別、生年月日、趣味などの詳細情報
 *
 * @param card 表示する名刺データ
 * @param onNavigateBack 戻るボタンが押されたときのコールバック
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    card: BusinessCardData,
    onNavigateBack: () -> Unit
) {
    // カードの反転状態を管理（false: 表面, true: 裏面）
    var isFlipped by remember { mutableStateOf(false) }
    
    // 反転アニメーション用の回転角度（0度 → 180度）
    // animateFloatAsStateで滑らかにアニメーション
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,  // 裏面なら180度
        animationSpec = tween(durationMillis = 600),  // 600ミリ秒（0.6秒）かけてアニメーション
        label = "cardRotation"
    )
    
    // カードデザインを取得（不正な値の場合はCLASSICをデフォルト）
    val cardDesign = try {
        CardDesign.valueOf(card.cardDesign)
    } catch (e: Exception) {
        CardDesign.CLASSIC
    }
    
    // Material3のScaffold（画面全体のレイアウト構造）
    Scaffold(
        topBar = {
            // トップバー：タイトルと戻るボタン
            TopAppBar(
                title = { Text("名刺詳細") },
                navigationIcon = {
                    // 左上の戻るボタン
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // メインコンテンツを縦方向に配置
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())  // スクロール可能にする
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,  // 水平方向中央揃え
            verticalArrangement = Arrangement.spacedBy(16.dp)  // 要素間の間隔16dp
        ) {
            // 操作説明テキスト
            Text(
                text = "タップして反転",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // 名刺カード（反転アニメーション付き）
            Card(
                modifier = Modifier
                    .fillMaxWidth()  // 横幅いっぱい
                    .aspectRatio(0.63f)  // 縦横比0.63（プロフィール画面と同じ名刺比率）
                    .graphicsLayer {
                        // Y軸回転で反転アニメーション
                        rotationY = rotation
                        // カメラの距離（遠近感を出す）
                        cameraDistance = 12f * density
                    }
                    .clickable { isFlipped = !isFlipped },  // タップで表裏切り替え
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),  // 影の深さ
                shape = RoundedCornerShape(16.dp)  // 角丸
            ) {
                // 回転角度に応じて表面/裏面を切り替え
                // 90度以下なら表面、90度超えたら裏面を表示
                if (rotation <= 90f) {
                    ReceivedCardFront(
                        card = card,
                        cardDesign = cardDesign
                    )
                } else {
                    ReceivedCardBack(
                        card = card,
                        cardDesign = cardDesign
                    )
                }
            }
        }
    }
}

/**
 * 受け取った名刺の表面表示
 *
 * プロフィール画像、ニックネーム、自己紹介を中央に大きく表示
 * シンプルで読みやすいレイアウト
 */
@Composable
private fun ReceivedCardFront(
    card: BusinessCardData,
    cardDesign: CardDesign
) {
    // Boxで背景グラデーションを適用
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = cardDesign.frontBrush)  // カードデザインの表面グラデーション
            .padding(32.dp)
    ) {
        // 縦方向に中央配置
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,  // 縦方向中央
            horizontalAlignment = Alignment.CenterHorizontally  // 横方向中央
        ) {
            // プロフィール画像 - プロフィール画面と同じ140dp
            if (!card.profileImageUri.isNullOrEmpty() && card.profileImageUri.isNotBlank()) {
                AsyncImage(
                    model = card.profileImageUri,
                    contentDescription = "プロフィール画像",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // デフォルトアイコン（画像がない場合）
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(cardDesign.frontTextColor.copy(alpha = 0.1f))
                        .border(3.dp, cardDesign.frontTextColor.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "デフォルトアイコン",
                        modifier = Modifier.size(80.dp),
                        tint = cardDesign.frontTextColor.copy(alpha = 0.5f)
                    )
                }
            }
            
            // 間隔
            Spacer(modifier = Modifier.height(20.dp))
            
            // ニックネーム - プロフィール画面と同じスタイル
            Text(
                text = card.nickname,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = cardDesign.frontTextColor,
                letterSpacing = 0.5.sp
            )
            
            // 間隔
            Spacer(modifier = Modifier.height(16.dp))
            
            // 自己紹介 - プロフィール画面と同じスタイル
            Text(
                text = card.bio,
                style = MaterialTheme.typography.bodyLarge,
                color = cardDesign.frontTextColor.copy(alpha = 0.9f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

/**
 * 受け取った名刺の裏面表示
 * 
 * 性別、生年月日、趣味などの詳細情報を表示
 * graphicsLayer { rotationY = 180f }で裏面として正しく表示
 */
@Composable
private fun ReceivedCardBack(
    card: BusinessCardData,
    cardDesign: CardDesign
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // 裏面は180度回転させて正しい向きにする
            .graphicsLayer { rotationY = 180f }
            .background(brush = cardDesign.backBrush)
            .padding(16.dp)
    ) {
        // 詳細情報を縦に並べて表示
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)  // 各項目間の間隔
        ) {
            // 性別情報（必須項目）
            InfoItemWithIcon(
                icon = "⚥",
                label = "性別",
                value = card.gender,
                textColor = cardDesign.backTextColor
            )
            
            // 生年月日（ある場合のみ表示）
            card.birthDate?.let { date ->
                InfoItemWithIcon(
                    icon = "🎂",
                    label = "生年月日",
                    value = date,
                    textColor = cardDesign.backTextColor
                )
            }
            
            // 趣味リスト（ある場合のみ表示）
            if (card.hobbies.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 趣味セクションのタイトル - プロフィールと統一
                    Text(
                        text = "🎨 趣味・興味",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = cardDesign.backTextColor.copy(alpha = 0.9f)
                    )
                    
                    // 各趣味をタグのように表示
                    card.hobbies.forEach { hobby ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),  // 角丸の背景
                            color = cardDesign.backTextColor.copy(alpha = 0.2f)  // 半透明の背景
                        ) {
                            Text(
                                text = hobby,
                                style = MaterialTheme.typography.bodyMedium,
                                color = cardDesign.backTextColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
            
            // Contact Info
            val context = LocalContext.current
            val hasContactInfo = card.phoneNumber.isNotEmpty() || card.email.isNotEmpty()
            
            if (hasContactInfo) {
                ContactInfoDisplay(
                    phoneNumber = card.phoneNumber,
                    email = card.email,
                    textColor = cardDesign.backTextColor
                )
            }
            
            // SNS Links
            val hasSnsLinks = card.twitterUrl.isNotEmpty() || 
                             card.instagramUrl.isNotEmpty() || 
                             card.facebookUrl.isNotEmpty() || 
                             card.lineUrl.isNotEmpty()
            
            if (hasSnsLinks) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "SNS・リンク",
                        style = MaterialTheme.typography.titleSmall,
                        color = cardDesign.backTextColor.copy(alpha = 0.7f)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (card.twitterUrl.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(card.twitterUrl))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tag,
                                    contentDescription = "Twitter/X",
                                    tint = cardDesign.backTextColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        if (card.instagramUrl.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(card.instagramUrl))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Camera,
                                    contentDescription = "Instagram",
                                    tint = cardDesign.backTextColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        if (card.facebookUrl.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(card.facebookUrl))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Facebook",
                                    tint = cardDesign.backTextColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        if (card.lineUrl.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(card.lineUrl))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "LINE",
                                    tint = cardDesign.backTextColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 情報アイテム表示用のコンポーネント（アイコン付き）
 * 
 * プロフィール画面と同じスタイルで表示
 * 
 * @param icon 絵文字アイコン
 * @param label ラベル文字列
 * @param value 値文字列
 * @param textColor テキストの基本色
 */
@Composable
private fun InfoItemWithIcon(
    icon: String,
    label: String,
    value: String,
    textColor: androidx.compose.ui.graphics.Color
) {
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
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
        }
    }
}

/**
 * 情報アイテム表示用のコンポーネント
 * 
 * ラベル（小さめ、薄い色）と値（大きめ、濃い色）を縦に並べて表示
 * 例：「性別」（ラベル） → 「男性」（値）
 * 
 * @param label ラベル文字列（「性別」「生年月日」など）
 * @param value 値文字列（「男性」「2000-01-01」など）
 * @param textColor テキストの基本色
 */
@Composable
private fun InfoItem(
    label: String,
    value: String,
    textColor: androidx.compose.ui.graphics.Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)  // ラベルと値の間隔
    ) {
        // ラベル（小さめ、薄い色）
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = textColor.copy(alpha = 0.7f)  // 70%の不透明度
        )
        // 値（大きめ、濃い色）
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor  // 100%の不透明度
        )
    }
}
