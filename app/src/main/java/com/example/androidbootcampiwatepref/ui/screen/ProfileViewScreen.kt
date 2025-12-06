package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.R
import com.example.androidbootcampiwatepref.domain.model.ProfileData
import com.example.androidbootcampiwatepref.ui.component.ProfileHeader
import com.example.androidbootcampiwatepref.ui.component.ProfileInfoRow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * プロフィール閲覧画面
 * 
 * ユーザーのプロフィール情報を表示する読み取り専用の画面
 * 
 * 主な機能:
 * - プロフィール情報（ニックネーム、ID、性別、誕生日、自己紹介、趣味）の表示
 * - 編集画面への遷移
 * - テーマ切り替え（ライトモード/ダークモード）
 * 
 * @param profileData 表示するプロフィールデータ
 * @param useDarkTheme ダークテーマを使用するかどうか
 * @param onThemeToggle テーマ切り替えボタンのクリック処理
 * @param onEditClick 編集ボタンのクリック処理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewScreen(
    profileData: ProfileData,
    profileImageUri: String?,
    headerImageUri: String?,
    useDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onEditClick: () -> Unit
) {
    // Scaffoldを使用して基本的な画面レイアウトを構築
    Scaffold(
        topBar = {
            // トップバー: タイトルと操作ボタンを配置
            TopAppBar(
                title = { Text("プロフィール") },
                actions = {
                    // 編集ボタン
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "編集")
                    }
                    // テーマ切り替えボタン（現在のテーマに応じて絵文字を変更）
                    IconButton(onClick = onThemeToggle) {
                        Text(if (useDarkTheme) "☀️" else "🌙")
                    }
                }
            )
        }
    ) { innerPadding ->
        // メインコンテンツの表示
        ProfileViewContent(
            innerPadding = innerPadding,
            profileData = profileData,
            profileImageUri = profileImageUri,
            headerImageUri = headerImageUri
        )
    }
}

/**
 * プロフィール閲覧画面のコンテンツ
 * 
 * プロフィール情報を表示する実際のUI部分
 * スクロール可能なレイアウトで各項目を縦に並べて表示
 * 
 * @param innerPadding Scaffoldから渡されるPadding（システムバーを避けるため）
 * @param profileData 表示するプロフィールデータ
 */
@Composable
fun ProfileViewContent(
    innerPadding: PaddingValues, 
    profileData: ProfileData,
    profileImageUri: String?,
    headerImageUri: String?
) {
    // 性別の選択肢リスト
    val genderOptions = listOf("男性", "女性", "回答しない")
    
    // 日付フォーマッター（yyyy/MM/dd形式）
    // rememberを使用してリコンポジション時に再生成されないようにする
    val birthDateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN) }

    // 縦スクロール可能なレイアウト
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()), // スクロール可能にする
    ) {
        // プロフィールヘッダー（プロフィール画像、ニックネーム、ID）
        ProfileHeader(
            nickname = profileData.nickname, 
            id = profileData.id,
            profileImageUri = profileImageUri,
            headerImageUri = headerImageUri,
            isEditable = false
        )

        // プロフィール詳細情報
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp) // 各項目間のスペース
        ) {
            // 各項目をProfileInfoRowコンポーネントで表示
            ProfileInfoRow(label = "自己紹介", value = profileData.bio)
            ProfileInfoRow(label = "性別", value = genderOptions[profileData.genderIndex])
            
            // 生年月日（エポックミリ秒を日付文字列に変換）
            ProfileInfoRow(
                label = "生年月日",
                value = profileData.birthDateMillis?.let {
                    // エポックミリ秒をDate型に変換してフォーマット
                    birthDateFormatter.format(Date(it))
                } ?: "未設定" // nullの場合は"未設定"と表示
            )

            // 趣味・興味リスト
            Column(modifier = Modifier.fillMaxWidth()) {
                // ラベル
                Text(
                    text = "趣味・興味",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // 趣味リストが空の場合の処理
                if (profileData.hobbies.isEmpty()) {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } else {
                    // 趣味を横スクロール可能なリストで表示
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp), // 各チップ間のスペース
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        // 各趣味をチップ形式で表示
                        items(profileData.hobbies) { hobby ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(hobby) }
                            )
                        }
                    }
                }
            }
        }
    }
}
