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
 * ヘッダー画像とプロフィールアイコン、ニックネーム、IDを表示
 */
@Composable
fun ProfileHeader(nickname: String, id: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp), // アイコンの下半分のためのスペース
        contentAlignment = Alignment.TopCenter
    ) {
        // ヘッダー画像
        Image(
            painter = painterResource(id = R.drawable.ic_my_hedder),
            contentDescription = "ヘッダー画像",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
        
        // プロフィールアイコンとニックネーム・IDを配置
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 120.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_my_icon),
                contentDescription = "アイコン",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Text(
                    text = nickname.ifEmpty { "" },
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
