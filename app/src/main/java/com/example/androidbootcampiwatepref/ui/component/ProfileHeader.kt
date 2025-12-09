package com.example.androidbootcampiwatepref.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.androidbootcampiwatepref.R

/**
 * プロフィールヘッダーコンポーネント
 * 
 * プロフィール画面の上部に表示されるヘッダー部分
 * ヘッダー背景画像の上にプロフィールアイコンを重ねて表示
 * 
 * レイアウト構造:
 * - ヘッダー背景画像（横幅いっぱい）
 * - プロフィールアイコン（円形、ヘッダー画像の下部に重なる）
 * - ニックネーム（太字、大きめのテキスト）
 * 
 * @param nickname ユーザーのニックネーム
 * @param profileImageUri プロフィール画像のURI（nullの場合はデフォルト画像）
 * @param headerImageUri ヘッダー画像のURI（nullの場合はデフォルト画像）
 * @param isEditable 編集可能かどうか（trueの場合、画像編集ボタンを表示）
 * @param onProfileImageClick プロフィール画像クリック時の処理
 * @param onHeaderImageClick ヘッダー画像クリック時の処理
 * @param modifier 外部から適用される修飾子
 */
@Composable
fun ProfileHeader(
    nickname: String,
    profileImageUri: String? = null,
    headerImageUri: String? = null,
    isEditable: Boolean = false,
    onProfileImageClick: () -> Unit = {},
    onHeaderImageClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Boxを使用してレイヤーを重ねる
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp), // アイコンの下半分が次のコンテンツと重ならないようにスペースを確保
        contentAlignment = Alignment.TopCenter
    ) {
        // 背景のヘッダー画像
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onHeaderImageClick() } // 常にクリック可能
        ) {
            if (headerImageUri != null) {
                // カスタム画像を表示（Coilで非同期読み込み）
                AsyncImage(
                    model = headerImageUri,
                    contentDescription = "ヘッダー画像",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // デフォルト画像を表示
                Image(
                    painter = painterResource(id = R.drawable.ic_my_hedder),
                    contentDescription = "ヘッダー画像",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            // 編集可能な場合、編集アイコンを表示
            if (isEditable) {
                IconButton(
                    onClick = onHeaderImageClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "ヘッダー画像を変更",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    )
                }
            }
        }
        
        // プロフィールアイコンとテキスト情報を横並びで配置
        Row(
            modifier = Modifier
                .align(Alignment.TopStart) // Box内の左上に配置
                .padding(top = 120.dp, start = 16.dp, end = 16.dp), // ヘッダー画像の下部に配置
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp) // アイコンとテキスト間のスペース
        ) {
            // プロフィールアイコン（円形）
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { onProfileImageClick() } // 常にクリック可能
            ) {
                if (profileImageUri != null) {
                    // カスタム画像を表示（Coilで非同期読み込み）
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = "プロフィールアイコン",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // デフォルトアイコンを表示
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "プロフィールアイコン",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // 編集可能な場合、編集アイコンを表示
                if (isEditable) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "プロフィール画像を変更",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                shape = CircleShape
                            )
                            .padding(4.dp)
                            .size(24.dp)
                    )
                }
            }
            
            // ニックネーム
            Column(
                modifier = Modifier.padding(top = 50.dp) // アイコンの中央に揃えるための調整
            ) {
                // ニックネーム（太字・大きめ）
                Text(
                    text = nickname.ifEmpty { "" }, // 空の場合は空文字列を表示
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
