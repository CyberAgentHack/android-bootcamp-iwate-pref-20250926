package com.example.androidbootcampiwatepref.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage

/**
 * 画像拡大表示ダイアログ
 * 
 * 画像をフルスクリーンで表示するダイアログコンポーネント
 * ピンチズーム、ドラッグ移動などのジェスチャー操作をサポート
 * 
 * @param imageUri 表示する画像のURI（nullの場合はデフォルト画像ID使用）
 * @param defaultImageRes デフォルト画像のリソースID（imageUriがnullの場合に使用）
 * @param contentDescription 画像の説明（アクセシビリティ用）
 * @param onDismiss ダイアログを閉じる処理
 */
@Composable
fun ImageViewerDialog(
    imageUri: String?,
    defaultImageRes: Int,
    contentDescription: String,
    onDismiss: () -> Unit
) {
    // ズームとオフセットの状態管理
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // ダイアログ表示（フルスクリーン）
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // 画面全体を使用
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        // 黒背景の全画面表示
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable { onDismiss() }, // 背景タップで閉じる
            contentAlignment = Alignment.Center
        ) {
            // 画像表示（ピンチズーム・ドラッグ対応）
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            // ズーム処理
                            scale = (scale * zoom).coerceIn(1f, 5f)
                            
                            // ドラッグ処理（ズーム時のみ有効）
                            if (scale > 1f) {
                                offsetX += pan.x
                                offsetY += pan.y
                            }
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    // カスタム画像を表示
                    AsyncImage(
                        model = imageUri,
                        contentDescription = contentDescription,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // デフォルト画像を表示
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = defaultImageRes),
                        contentDescription = contentDescription,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // 閉じるボタン（右上）
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "閉じる",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
