package com.example.androidbootcampiwatepref.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * 連絡先入力コンポーネント
 * 
 * 電話番号とメールアドレスを入力するフォーム
 * 
 * @param phoneNumber 電話番号
 * @param onPhoneNumberChange 電話番号変更時のコールバック
 * @param email メールアドレス
 * @param onEmailChange メールアドレス変更時のコールバック
 * @param textColor テキストカラー（カードデザインに応じて変更）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInfoEditor(
    phoneNumber: TextFieldValue,
    onPhoneNumberChange: (TextFieldValue) -> Unit,
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit,
    textColor: androidx.compose.ui.graphics.Color
) {
    Column {
        // セクションタイトル
        Text(
            "📞 連絡先",
            style = MaterialTheme.typography.labelMedium,
            color = textColor.copy(alpha = 0.8f)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 電話番号入力
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("電話番号", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("090-1234-5678", color = textColor.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // メールアドレス入力
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("メールアドレス", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("example@email.com", color = textColor.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
