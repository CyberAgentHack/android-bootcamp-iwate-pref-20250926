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
 * Twitter/X、Instagram、Facebook、GitHub、LinkedInに対応
 * 
 * @param twitterUrl Twitter/X URL
 * @param onTwitterUrlChange Twitter/X URL変更時のコールバック
 * @param instagramUrl Instagram URL
 * @param onInstagramUrlChange Instagram URL変更時のコールバック
 * @param facebookUrl Facebook URL
 * @param onFacebookUrlChange Facebook URL変更時のコールバック
 * @param githubUrl GitHub URL
 * @param onGithubUrlChange GitHub URL変更時のコールバック
 * @param linkedinUrl LinkedIn URL
 * @param onLinkedinUrlChange LinkedIn URL変更時のコールバック
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
    githubUrl: TextFieldValue,
    onGithubUrlChange: (TextFieldValue) -> Unit,
    linkedinUrl: TextFieldValue,
    onLinkedinUrlChange: (TextFieldValue) -> Unit,
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
        
        // GitHub URL入力
        OutlinedTextField(
            value = githubUrl,
            onValueChange = onGithubUrlChange,
            label = { Text("GitHub", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("https://github.com/username", color = textColor.copy(alpha = 0.5f)) },
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
        
        // LinkedIn URL入力
        OutlinedTextField(
            value = linkedinUrl,
            onValueChange = onLinkedinUrlChange,
            label = { Text("LinkedIn", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("https://linkedin.com/in/username", color = textColor.copy(alpha = 0.5f)) },
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
