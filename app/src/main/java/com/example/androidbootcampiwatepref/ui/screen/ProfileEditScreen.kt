package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.R
import com.example.androidbootcampiwatepref.domain.model.ProfileData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * プロフィール編集画面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    profileData: ProfileData,
    useDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: (ProfileData) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("プロフィール編集") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "キャンセル")
                    }
                },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Text(if (useDarkTheme) "☀️" else "🌙")
                    }
                }
            )
        }
    ) { innerPadding ->
        ProfileEditContent(
            innerPadding = innerPadding,
            initialProfileData = profileData,
            onSaveClick = onSaveClick
        )
    }
}

/**
 * プロフィール編集画面のコンテンツ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditContent(
    innerPadding: PaddingValues,
    initialProfileData: ProfileData,
    onSaveClick: (ProfileData) -> Unit
) {
    // 状態変数
    var nickname by remember { mutableStateOf(TextFieldValue(initialProfileData.nickname)) }
    var id by remember { mutableStateOf(TextFieldValue(initialProfileData.id)) }
    var bio by remember { mutableStateOf(TextFieldValue(initialProfileData.bio)) }
    val genderOptions = listOf("男性", "女性", "回答しない")
    var selectedGenderIndex by remember { mutableStateOf(initialProfileData.genderIndex) }
    var selectedDateMillis by remember { mutableStateOf(initialProfileData.birthDateMillis) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val birthDateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN) }
    var hobbies by remember { mutableStateOf(initialProfileData.hobbies.toMutableList()) }
    var hobbyInput by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // プロフィールアイコン
        Image(
            painter = painterResource(id = R.drawable.ic_my_icon),
            contentDescription = "アイコン",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ニックネーム
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("ニックネーム") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // ID
            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID") },
                prefix = { Text("@") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 自己紹介
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("自己紹介") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )

            // 性別選択
            Text(
                "性別",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genderOptions.forEachIndexed { index, label ->
                    Button(
                        onClick = { selectedGenderIndex = index },
                        modifier = Modifier.weight(1f),
                        colors = if (selectedGenderIndex == index) {
                            ButtonDefaults.buttonColors()
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        }
                    ) {
                        Text(label)
                    }
                }
            }

            // 生年月日
            Text(
                "生年月日",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.clickable { showDatePickerDialog = true }) {
                OutlinedTextField(
                    value = selectedDateMillis?.let {
                        birthDateFormatter.format(Date(it))
                    } ?: "",
                    onValueChange = {},
                    label = { Text("YYYY/MM/DD") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // DatePicker ダイアログ
            if (showDatePickerDialog) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDateMillis
                        ?: Calendar.getInstance().timeInMillis
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePickerDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            selectedDateMillis = datePickerState.selectedDateMillis
                            showDatePickerDialog = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDatePickerDialog = false }) {
                            Text("キャンセル")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // 趣味・興味入力セクション
            Text(
                "趣味・興味",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = hobbyInput,
                    onValueChange = { hobbyInput = it },
                    label = { Text("趣味を入力") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(
                    onClick = {
                        if (hobbyInput.text.isNotBlank() && !hobbies.contains(hobbyInput.text)) {
                            hobbies = hobbies.toMutableList().apply { add(hobbyInput.text) }
                            hobbyInput = TextFieldValue("")
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "追加")
                }
            }

            // 趣味タグ一覧
            if (hobbies.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(hobbies) { hobby ->
                        SuggestionChip(
                            onClick = {
                                hobbies = hobbies.toMutableList().apply { remove(hobby) }
                            },
                            label = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(hobby)
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "削除",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }

            // 保存ボタン
            Button(
                onClick = {
                    val updatedData = ProfileData(
                        nickname = nickname.text,
                        id = id.text,
                        bio = bio.text,
                        genderIndex = selectedGenderIndex,
                        birthDateMillis = selectedDateMillis,
                        hobbies = hobbies
                    )
                    onSaveClick(updatedData)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("保存")
            }
        }
    }
}
