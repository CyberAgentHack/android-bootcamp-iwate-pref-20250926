package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.domain.model.BusinessCardData
import com.example.androidbootcampiwatepref.domain.model.CardDesign

/**
 * 名刺ホルダー画面
 *
 * 受け取った名刺の一覧を表示
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardHolderScreen(
    savedCards: List<BusinessCardData>,
    onNavigateBack: () -> Unit,
    onDeleteCard: (BusinessCardData) -> Unit,
    onCardClick: (BusinessCardData) -> Unit
) {
    // Material3のScaffold（画面全体のレイアウト構造）
    Scaffold(
        topBar = {
            // トップバー：タイトル、名刺枚数、戻るボタンを表示
            TopAppBar(
                title = { 
                    Column {
                        // タイトル「名刺ホルダー」
                        Text("名刺ホルダー")
                        // サブタイトル：保存されている名刺の枚数を表示
                        Text(
                            text = "${savedCards.size}枚",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
        // 名刺の有無で表示を切り替え
        if (savedCards.isEmpty()) {
            // 空状態：名刺が1枚もない場合の表示
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center  // 中央揃え
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // メインメッセージ：名刺がないことを通知
                    Text(
                        text = "名刺がありません",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // サブメッセージ：ユーザーに次のアクションを促す
                    Text(
                        text = "QRコードをスキャンして名刺を交換しましょう",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // 名刺がある場合：スクロール可能なリストで表示
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),  // 外側の余白
                verticalArrangement = Arrangement.spacedBy(12.dp)  // アイテム間の間隔
            ) {
                // items()でリストの各要素を表示
                // keyを指定することで、リストの再構成時に効率的に更新
                items(savedCards, key = { it.nickname + it.bio }) { card ->
                    // 各名刺のアイテムを表示
                    BusinessCardItem(
                        card = card,
                        onCardClick = { onCardClick(card) },  // クリック時の処理
                        onDeleteClick = { onDeleteCard(card) }  // 削除時の処理
                    )
                }
            }
        }
    }
}

/**
 * 名刺一覧の各アイテム表示
 *
 * 名刺のサマリー情報を表示し、クリックで詳細画面へ遷移
 * 削除ボタンで確認ダイアログを表示
 */
@Composable
private fun BusinessCardItem(
    card: BusinessCardData,
    onCardClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // 削除確認ダイアログの表示状態
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // カードデザインを取得（不正な値の場合はCLASSICをデフォルト）
    val cardDesign = try {
        CardDesign.valueOf(card.cardDesign)
    } catch (e: Exception) {
        CardDesign.CLASSIC
    }
    
    // Material3のCardコンポーネント（影付きのカード）
    Card(
        modifier = Modifier
            .fillMaxWidth()  // 横幅いっぱい
            .height(120.dp)  // 高さ120dpに固定
            .clickable(onClick = onCardClick),  // クリックで詳細画面へ
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),  // 影の深さ
        shape = RoundedCornerShape(12.dp)  // 角丸
    ) {
        // Boxでカードデザインの背景グラデーションを適用
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = cardDesign.frontBrush)  // デザイン固有のグラデーション
        ) {
            // Row：左に名刺情報、右に削除ボタンを配置
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,  // 左右に分けて配置
                verticalAlignment = Alignment.CenterVertically  // 上下中央揃え
            ) {
                // 左側：名刺の主要情報（ニックネーム、自己紹介、性別、生年月日）
                Column(
                    modifier = Modifier.weight(1f),  // 残りのスペースを使う
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ニックネーム（大きめ、太字）
                    Text(
                        text = card.nickname,
                        style = MaterialTheme.typography.titleLarge,
                        color = cardDesign.frontTextColor,
                        maxLines = 1,  // 1行のみ表示
                        overflow = TextOverflow.Ellipsis  // 長い場合は「...」で省略
                    )
                    
                    // 自己紹介（中くらいのサイズ）
                    Text(
                        text = card.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cardDesign.frontTextColor.copy(alpha = 0.8f),  // 少し薄い色
                        maxLines = 2,  // 2行まで表示
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // 性別と生年月日を横並びで表示（小さめのテキスト）
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 性別表示
                        Text(
                            text = card.gender,
                            style = MaterialTheme.typography.bodySmall,
                            color = cardDesign.frontTextColor.copy(alpha = 0.7f)
                        )
                        // 生年月日がある場合のみ表示
                        card.birthDate?.let { date ->
                            // 区切り記号「•」
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodySmall,
                                color = cardDesign.frontTextColor.copy(alpha = 0.7f)
                            )
                            // 生年月日表示
                            Text(
                                text = date,
                                style = MaterialTheme.typography.bodySmall,
                                color = cardDesign.frontTextColor.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
                
                // 右側：削除ボタン
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "削除",
                        tint = cardDesign.frontTextColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
    
    // 削除ボタンが押されたら確認ダイアログを表示
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },  // 外側タップで閉じる
            title = { Text("名刺を削除") },  // ダイアログのタイトル
            text = { Text("「${card.nickname}」さんの名刺を削除しますか？") },  // 確認メッセージ
            confirmButton = {
                // 削除ボタン（赤い警告カラー）
                TextButton(
                    onClick = {
                        showDeleteDialog = false  // ダイアログを閉じる
                        onDeleteClick()  // 削除処理を実行
                    }
                ) {
                    Text("削除")
                }
            },
            dismissButton = {
                // キャンセルボタン
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }
}
