package com.example.androidbootcampiwatepref.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * 趣味入力コンポーネント
 * 
 * 趣味・興味を追加・削除するためのインターフェース
 * - 入力フィールドで新しい趣味を追加
 * - チップをタップして削除
 * 
 * @param hobbies 現在の趣味リスト
 * @param hobbyInput 入力中の趣味テキスト
 * @param onHobbyInputChange 趣味入力変更時のコールバック
 * @param onAddHobby 趣味追加時のコールバック
 * @param onRemoveHobby 趣味削除時のコールバック（インデックス指定）
 * @param textColor テキストカラー（カードデザインに応じて変更）
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HobbiesEditor(
    hobbies: List<String>,
    hobbyInput: TextFieldValue,
    onHobbyInputChange: (TextFieldValue) -> Unit,
    onAddHobby: () -> Unit,
    onRemoveHobby: (Int) -> Unit,
    textColor: androidx.compose.ui.graphics.Color
) {
    Column {
        // セクションタイトル
        Text(
            "🎨 趣味・興味",
            style = MaterialTheme.typography.labelMedium,
            color = textColor.copy(alpha = 0.8f)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 趣味入力フィールド
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = hobbyInput,
                onValueChange = onHobbyInputChange,
                label = { Text("追加", color = textColor.copy(alpha = 0.7f)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor.copy(alpha = 0.5f)
                )
            )
            IconButton(onClick = onAddHobby) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "追加",
                    modifier = Modifier.size(20.dp),
                    tint = textColor
                )
            }
        }
        
        // 趣味リスト（チップで表示）
        if (hobbies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                hobbies.forEachIndexed { index, hobby ->
                    SuggestionChip(
                        onClick = { onRemoveHobby(index) },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(hobby, style = MaterialTheme.typography.bodySmall)
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "削除",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
