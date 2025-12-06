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
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewScreen(
    profileData: ProfileData,
    useDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onEditClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("プロフィール") },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "編集")
                    }
                    IconButton(onClick = onThemeToggle) {
                        Text(if (useDarkTheme) "☀️" else "🌙")
                    }
                }
            )
        }
    ) { innerPadding ->
        ProfileViewContent(
            innerPadding = innerPadding,
            profileData = profileData
        )
    }
}

/**
 * プロフィール閲覧画面のコンテンツ
 */
@Composable
fun ProfileViewContent(innerPadding: PaddingValues, profileData: ProfileData) {
    val genderOptions = listOf("男性", "女性", "回答しない")
    val birthDateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        ProfileHeader(nickname = profileData.nickname, id = profileData.id)

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 各項目をラベルと値で表示
            ProfileInfoRow(label = "自己紹介", value = profileData.bio)
            ProfileInfoRow(label = "性別", value = genderOptions[profileData.genderIndex])
            ProfileInfoRow(
                label = "生年月日",
                value = profileData.birthDateMillis?.let {
                    birthDateFormatter.format(Date(it))
                } ?: "未設定"
            )

            // 趣味・興味リスト
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "趣味・興味",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (profileData.hobbies.isEmpty()) {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
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
