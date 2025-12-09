package com.example.androidbootcampiwatepref.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.domain.model.BusinessCardData
import com.example.androidbootcampiwatepref.domain.model.GenderOption
import com.example.androidbootcampiwatepref.util.BusinessCardSharer
import com.example.androidbootcampiwatepref.util.QRCodeGenerator

/**
 * QRコード表示画面
 * 
 * 自分の名刺をQRコードとして表示する
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeDisplayScreen(
    nickname: String,
    bio: String,
    genderIndex: Int,
    birthDateMillis: Long?,
    hobbies: List<String>,
    cardDesign: String,
    profileImageUri: String?,
    twitterUrl: String,
    instagramUrl: String,
    facebookUrl: String,
    lineUrl: String,
    phoneNumber: String,
    email: String,
    onNavigateBack: () -> Unit,
    onNavigateToScanner: () -> Unit
) {
    val context = LocalContext.current
    
    // プロフィールデータから名刺データを生成
    // JSONにシリアライズしてQRコードに埋め込む準備
    // 注意: 画像はQRコードに含めない（容量制限のため）
    // 受信側ではデフォルトアイコンを表示
    val businessCardData = BusinessCardData.fromProfileData(
        nickname = nickname,
        bio = bio,
        genderIndex = genderIndex,
        birthDateMillis = birthDateMillis,
        hobbies = hobbies,
        cardDesign = cardDesign,
        profileImageUri = null,  // 画像は含めない
        twitterUrl = twitterUrl,
        instagramUrl = instagramUrl,
        facebookUrl = facebookUrl,
        lineUrl = lineUrl,
        phoneNumber = phoneNumber,
        email = email
    )
    
    // 名刺データをJSON化してQRコード画像を生成
    // remember()により、businessCardDataが変更されない限り再生成されない
    val qrCodeBitmap = remember(businessCardData) {
        QRCodeGenerator.generateQRCode(businessCardData.toJson(), 512)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("マイQRコード") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToScanner) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "QRスキャン"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // 説明テキスト
            Text(
                text = "このQRコードを相手に読み取ってもらうと\n名刺を交換できます",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            // QRコード表示
            Card(
                modifier = Modifier
                    .size(320.dp)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = qrCodeBitmap.asImageBitmap(),
                        contentDescription = "マイQRコード",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            
            // プロフィール情報プレビュー
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "交換される情報",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    InfoRow(label = "ニックネーム", value = nickname)
                    InfoRow(label = "自己紹介", value = bio)
                    
                    // 性別インデックスを文字列に変換（共通定数を使用）
                    val genderText = GenderOption.fromIndex(genderIndex).label
                    InfoRow(label = "性別", value = genderText)
                    
                    if (birthDateMillis != null) {
                        val date = java.util.Date(birthDateMillis)
                        val formatter = java.text.SimpleDateFormat("yyyy年MM月dd日", java.util.Locale.JAPAN)
                        InfoRow(label = "生年月日", value = formatter.format(date))
                    }
                    
                    if (hobbies.isNotEmpty()) {
                        InfoRow(label = "趣味", value = hobbies.joinToString(", "))
                    }
                    
                    InfoRow(label = "デザイン", value = cardDesign)
                }
            }
            
            // 共有ボタン
            Button(
                onClick = {
                    BusinessCardSharer.shareBusinessCard(
                        context = context,
                        businessCardData = businessCardData,
                        profileImageUri = profileImageUri
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("画像付きで共有（Android共有）")
            }
            
            Text(
                text = "※Android共有機能を使うと、画像も一緒に送信できます",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.weight(1f)
        )
    }
}
