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
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.domain.model.BusinessCardData
import com.example.androidbootcampiwatepref.domain.model.CardDesign

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
                    .aspectRatio(1.6f)  // 縦横比1.6（名刺の一般的な比率）
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
            // プロフィール画像（アイコン） - デフォルトアイコンのみ
            if (!card.profileImageUri.isNullOrEmpty()) {
                // 機能拡張用: 将来的に画像表示を追加可能
                // 現在はデフォルトアイコンを表示
                // デフォルトアイコン（画像がない場合）
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(cardDesign.frontTextColor.copy(alpha = 0.2f))
                        .border(3.dp, cardDesign.frontTextColor.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "デフォルトアイコン",
                        modifier = Modifier.size(60.dp),
                        tint = cardDesign.frontTextColor.copy(alpha = 0.7f)
                    )
                }
            }
            
            // 間隔
            Spacer(modifier = Modifier.height(24.dp))
            
            // ニックネーム（大きめの表示）
            Text(
                text = card.nickname,
                style = MaterialTheme.typography.displaySmall,  // 大きい見出しスタイル
                color = cardDesign.frontTextColor
            )
            
            // 間隔
            Spacer(modifier = Modifier.height(16.dp))
            
            // 自己紹介
            Text(
                text = card.bio,
                style = MaterialTheme.typography.bodyLarge,
                color = cardDesign.frontTextColor.copy(alpha = 0.9f)  // 少し薄い色
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
            .padding(32.dp)
    ) {
        // 詳細情報を縦に並べて表示
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)  // 各項目間の間隔
        ) {
            // 性別情報（必須項目）
            InfoItem(
                label = "性別",
                value = card.gender,
                textColor = cardDesign.backTextColor
            )
            
            // 生年月日（ある場合のみ表示）
            card.birthDate?.let { date ->
                InfoItem(
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
                    // 趣味セクションのタイトル
                    Text(
                        text = "趣味・興味",
                        style = MaterialTheme.typography.titleSmall,
                        color = cardDesign.backTextColor.copy(alpha = 0.7f)
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
