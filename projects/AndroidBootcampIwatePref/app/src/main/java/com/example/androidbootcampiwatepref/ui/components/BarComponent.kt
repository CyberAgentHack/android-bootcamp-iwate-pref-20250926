package com.example.androidbootcampiwatepref.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidbootcampiwatepref.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "Premium",
                color = Color.Black,
                fontWeight = FontWeight.W700
            )
        },
        navigationIcon = {
            // 2. Imageコンポーザブルを配置
            Image(
                painter = painterResource(id = R.drawable.youtube_icon), // 表示したい画像リソースを指定
                contentDescription = "アプリのロゴ",
                modifier = Modifier
                    .padding(start = 16.dp) // 左側に余白を追加して見た目を調整
                    .size(45.dp)         // アイコンのサイズを指定
            )
        },
        actions = {
            IconButton(onClick = { /* その他メニュー処理 */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "通知"
                )
            }
            IconButton(onClick = { /* 検索処理 */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "検索"
                )
            }

        },
        // アプリバーの背景色などをテーマに合わせる
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

/// データとUIを分離した
/// ナビゲーションの構造が複雑になったらmodel層に移動することを考える

// 各ナビゲーションアイテムの情報をまとめるためのデータクラス
private data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

// 表示するナビゲーションアイテムのリスト
private val bottomNavItems = listOf(
    NavigationItem("ホーム", Icons.Default.Home, "home"),
    NavigationItem("ショート", Icons.Default.PlayArrow, "shorts"),
    NavigationItem("作成", Icons.Default.Add, "create"),
    NavigationItem("登録チャンネル", Icons.Default.PlayArrow, "RegisteredChannels"),
    NavigationItem("マイページ", Icons.Default.AccountCircle, "library")
)

/// ボトムナビゲーションバーの実装
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = modifier
    ) {

        // アイテムのリストをループ処理してNavigationBarItemを動的に生成
        bottomNavItems.forEach { item ->

            // 「作成」ボタンだけはラベルがないので特別扱い
            val hasLabel = item.route != "create"

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                // ラベルを持つアイテムの場合のみTextを表示
                label = if (hasLabel) {
                    {
                        Text(
                            text = item.label,
                            fontSize = 9.sp // フォントサイズを統一
                        )
                    }
                } else {
                    null
                },

                // 現在選択されているアイテムかどうかを判定
                selected = (currentRoute == item.route),
                // クリックされたときの処理（将来的には画面遷移を実装）
                onClick = {
                    onNavigate(item.route)
                }
            )
        }
    }
}