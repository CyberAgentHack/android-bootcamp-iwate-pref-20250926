package com.example.androidbootcampiwatepref.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * SNSリンク入力コンポーネント
 * 
 * 各種SNSプラットフォームのプロフィールURLを入力するフォーム
 * Twitter/X、Instagram、Facebook、LINEに対応
 * 
 * @param twitterUrl Twitter/X URL
 * @param onTwitterUrlChange Twitter/X URL変更時のコールバック
 * @param instagramUrl Instagram URL
 * @param onInstagramUrlChange Instagram URL変更時のコールバック
 * @param facebookUrl Facebook URL
 * @param onFacebookUrlChange Facebook URL変更時のコールバック
 * @param lineUrl LINE URL
 * @param onLineUrlChange LINE URL変更時のコールバック
 * @param textColor テキストカラー（カードデザインに応じて変更）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnsLinksEditor(
    twitterUrl: TextFieldValue,
    onTwitterUrlChange: (TextFieldValue) -> Unit,
    instagramUrl: TextFieldValue,
    onInstagramUrlChange: (TextFieldValue) -> Unit,
    facebookUrl: TextFieldValue,
    onFacebookUrlChange: (TextFieldValue) -> Unit,
    lineUrl: TextFieldValue,
    onLineUrlChange: (TextFieldValue) -> Unit,
    textColor: androidx.compose.ui.graphics.Color
) {
    Column {
        // セクションタイトル
        Text(
            "🔗 SNS・リンク",
            style = MaterialTheme.typography.labelMedium,
            color = textColor.copy(alpha = 0.8f)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Twitter/X URL入力
        OutlinedTextField(
            value = twitterUrl,
            onValueChange = onTwitterUrlChange,
            label = { Text("Twitter/X", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("https://x.com/username", color = textColor.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Instagram URL入力
        OutlinedTextField(
            value = instagramUrl,
            onValueChange = onInstagramUrlChange,
            label = { Text("Instagram", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("https://instagram.com/username", color = textColor.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Facebook URL入力
        OutlinedTextField(
            value = facebookUrl,
            onValueChange = onFacebookUrlChange,
            label = { Text("Facebook", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("https://facebook.com/username", color = textColor.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // LINE URL入力
        OutlinedTextField(
            value = lineUrl,
            onValueChange = onLineUrlChange,
            label = { Text("LINE", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("https://line.me/ti/p/username", color = textColor.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.5f)
            )
        )
    }
}
