package com.example.androidbootcampiwatepref.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
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
 * - ID（@付き、小さめのテキスト）
 * 
 * @param nickname ユーザーのニックネーム
 * @param id ユーザーID
 * @param modifier 外部から適用される修飾子
 */
@Composable
fun ProfileHeader(nickname: String, id: String, modifier: Modifier = Modifier) {
    // Boxを使用してレイヤーを重ねる
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp), // アイコンの下半分が次のコンテンツと重ならないようにスペースを確保
        contentAlignment = Alignment.TopCenter
    ) {
        // 背景のヘッダー画像
        Image(
            painter = painterResource(id = R.drawable.ic_my_hedder),
            contentDescription = "ヘッダー画像",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop // 画像を切り抜いてフィット
        )
        
        // プロフィールアイコンとテキスト情報を横並びで配置
        Row(
            modifier = Modifier
                .align(Alignment.TopStart) // Box内の左上に配置
                .padding(top = 120.dp, start = 16.dp, end = 16.dp), // ヘッダー画像の下部に配置
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp) // アイコンとテキスト間のスペース
        ) {
            // プロフィールアイコン（円形）
            Image(
                painter = painterResource(id = R.drawable.ic_my_icon),
                contentDescription = "アイコン",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape) // 円形にクリップ
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape), // 外枠を追加
                contentScale = ContentScale.Crop // 画像を切り抜いてフィット
            )
            
            // ニックネームとIDを縦に並べる
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
                Text(
                    text = if (id.isEmpty()) "@" else "@$id",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
