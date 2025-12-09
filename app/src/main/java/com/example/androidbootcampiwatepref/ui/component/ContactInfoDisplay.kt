package com.example.androidbootcampiwatepref.ui.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 連絡先表示コンポーネント（コピーボタン付き）
 * 
 * 電話番号とメールアドレスをテキスト表示し、
 * 各項目の右側にコピーボタンを配置
 * 
 * @param phoneNumber 電話番号
 * @param email メールアドレス
 * @param textColor テキストカラー
 */
@Composable
fun ContactInfoDisplay(
    phoneNumber: String,
    email: String,
    textColor: androidx.compose.ui.graphics.Color
) {
    val context = LocalContext.current
    
    // いずれかの連絡先が設定されているかチェック
    val hasContact = phoneNumber.isNotEmpty() || email.isNotEmpty()
    
    if (hasContact) {
        Column {
            Text(
                text = "📞 連絡先",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor.copy(alpha = 0.9f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 電話番号
            if (phoneNumber.isNotEmpty()) {
                ContactInfoItem(
                    icon = Icons.Default.Phone,
                    label = "電話番号",
                    value = phoneNumber,
                    textColor = textColor,
                    onCopyClick = {
                        copyToClipboard(context, "電話番号", phoneNumber)
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // メールアドレス
            if (email.isNotEmpty()) {
                ContactInfoItem(
                    icon = Icons.Default.Email,
                    label = "メールアドレス",
                    value = email,
                    textColor = textColor,
                    onCopyClick = {
                        copyToClipboard(context, "メールアドレス", email)
                    }
                )
            }
        }
    }
}

/**
 * 連絡先項目（アイコン + テキスト + コピーボタン）
 */
@Composable
private fun ContactInfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    textColor: androidx.compose.ui.graphics.Color,
    onCopyClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // アイコン + テキスト
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = textColor.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
        
        // コピーボタン
        IconButton(
            onClick = onCopyClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "コピー",
                tint = textColor.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * クリップボードにコピー
 */
private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    
    // TODO: トーストやスナックバーで「コピーしました」と表示するとより親切
}
