package com.example.androidbootcampiwatepref.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * プロフィール情報の行コンポーネント
 * 
 * ラベルと値のペアを表示する再利用可能なコンポーネント
 * プロフィール閲覧画面で各項目（性別、誕生日、自己紹介など）を統一的に表示
 * 
 * レイアウト構造:
 * - 上部: ラベル（小さめのテキスト、グレー）
 * - 下部: 値（枠線付きのボックス内に表示）
 * 
 * @param label 項目名（例: "性別", "生年月日"）
 * @param value 値（例: "男性", "2000/01/01"）
 */
@Composable
fun ProfileInfoRow(label: String, value: String) {
    // 縦方向にラベルと値を配置
    Column(modifier = Modifier.fillMaxWidth()) {
        // ラベル部分
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        // 値を表示するボックス（枠線付き）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
