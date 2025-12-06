package com.example.androidbootcampiwatepref.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import java.io.File
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.R
import com.example.androidbootcampiwatepref.domain.model.ProfileData
import com.example.androidbootcampiwatepref.ui.component.ProfileHeader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * プロフィール編集画面
 * 
 * ユーザーがプロフィール情報を編集するための画面
 * 
 * 主な機能:
 * - 各プロフィール項目の入力・編集
 * - 性別の選択（ラジオボタン）
 * - 誕生日の選択（DatePicker）
 * - 趣味の追加・削除（動的リスト管理）
 * - 入力内容の保存
 * - テーマ切り替え
 * 
 * @param profileData 編集対象のプロフィールデータ（初期値として使用）
 * @param useDarkTheme ダークテーマを使用するかどうか
 * @param onThemeToggle テーマ切り替えボタンのクリック処理
 * @param onBackClick 戻るボタンのクリック処理（編集をキャンセル）
 * @param onSaveClick 保存ボタンのクリック処理（編集内容を保存）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    profileData: ProfileData,
    profileImageUri: String?,
    headerImageUri: String?,
    useDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: (ProfileData, String?, String?) -> Unit
) {
    // Scaffoldを使用して基本的な画面レイアウトを構築
    Scaffold(
        topBar = {
            // トップバー: タイトルと操作ボタンを配置
            TopAppBar(
                title = { Text("プロフィール編集") },
                navigationIcon = {
                    // 戻るボタン（編集をキャンセル）
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "キャンセル")
                    }
                },
                actions = {
                    // テーマ切り替えボタン
                    IconButton(onClick = onThemeToggle) {
                        Text(if (useDarkTheme) "☀️" else "🌙")
                    }
                }
            )
        }
    ) { innerPadding ->
        // メインコンテンツの表示
        ProfileEditContent(
            innerPadding = innerPadding,
            initialProfileData = profileData,
            initialProfileImageUri = profileImageUri,
            initialHeaderImageUri = headerImageUri,
            onSaveClick = onSaveClick
        )
    }
}

/**
 * プロフィール編集画面のコンテンツ
 * 
 * 各入力項目のUIと状態管理を担当
 * 複雑な状態管理が必要なため、多くのrememberを使用
 * 
 * @param innerPadding Scaffoldから渡されるPadding
 * @param initialProfileData 編集対象の初期データ
 * @param onSaveClick 保存ボタンのクリック処理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditContent(
    innerPadding: PaddingValues,
    initialProfileData: ProfileData,
    initialProfileImageUri: String?,
    initialHeaderImageUri: String?,
    onSaveClick: (ProfileData, String?, String?) -> Unit
) {
    val context = LocalContext.current
    // --- 状態変数の定義 ---
    // TextFieldValueを使用してカーソル位置なども管理
    var nickname by remember { mutableStateOf(TextFieldValue(initialProfileData.nickname)) }
    var id by remember { mutableStateOf(TextFieldValue(initialProfileData.id)) }
    var bio by remember { mutableStateOf(TextFieldValue(initialProfileData.bio)) }
    
    // 性別関連
    val genderOptions = listOf("男性", "女性", "回答しない")
    var selectedGenderIndex by remember { mutableStateOf(initialProfileData.genderIndex) }
    
    // 誕生日関連
    var selectedDateMillis by remember { mutableStateOf(initialProfileData.birthDateMillis) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val birthDateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN) }
    
    // 趣味関連
    var hobbies by remember { mutableStateOf(initialProfileData.hobbies.toMutableList()) }
    var hobbyInput by remember { mutableStateOf(TextFieldValue("")) }
    
    // 画像URI関連
    var currentProfileImageUri by remember { mutableStateOf(initialProfileImageUri) }
    var currentHeaderImageUri by remember { mutableStateOf(initialHeaderImageUri) }
    var isSelectingProfileImage by remember { mutableStateOf(false) }
    
    // UCrop結果を受け取るランチャー
    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.let { intent ->
                val resultUri = UCrop.getOutput(intent)
                resultUri?.let { uri ->
                    if (isSelectingProfileImage) {
                        currentProfileImageUri = uri.toString()
                    } else {
                        currentHeaderImageUri = uri.toString()
                    }
                }
            }
        }
    }
    
    // 画像選択ランチャー（プロフィール画像用）
    val profileImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { sourceUri ->
            isSelectingProfileImage = true
            
            // トリミング後の画像を保存するファイルを作成
            val destinationFile = File(context.cacheDir, "cropped_profile_${System.currentTimeMillis()}.jpg")
            val destinationUri = FileProvider.getUriForFile(
                context,
                "com.example.androidbootcampiwatepref.fileprovider",
                destinationFile
            )
            
            // UCropを使用してトリミング画面を起動
            val uCropIntent = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1f, 1f) // 正方形（1:1）
                .withMaxResultSize(500, 500) // 最大サイズ
                .getIntent(context)
            
            cropImageLauncher.launch(uCropIntent)
        }
    }
    
    // 画像選択ランチャー（ヘッダー画像用）
    val headerImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { sourceUri ->
            isSelectingProfileImage = false
            
            // トリミング後の画像を保存するファイルを作成
            val destinationFile = File(context.cacheDir, "cropped_header_${System.currentTimeMillis()}.jpg")
            val destinationUri = FileProvider.getUriForFile(
                context,
                "com.example.androidbootcampiwatepref.fileprovider",
                destinationFile
            )
            
            // UCropを使用してトリミング画面を起動（ヘッダーは横長）
            val uCropIntent = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(16f, 9f) // 横長（16:9）
                .withMaxResultSize(1200, 675) // 最大サイズ
                .getIntent(context)
            
            cropImageLauncher.launch(uCropIntent)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // プロフィールヘッダー（編集可能）
        ProfileHeader(
            nickname = nickname.text.ifEmpty { "ニックネーム" },
            id = id.text.ifEmpty { "ID" },
            profileImageUri = currentProfileImageUri,
            headerImageUri = currentHeaderImageUri,
            isEditable = true,
            onProfileImageClick = {
                profileImageLauncher.launch("image/*")
            },
            onHeaderImageClick = {
                headerImageLauncher.launch("image/*")
            }
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
                    onSaveClick(updatedData, currentProfileImageUri, currentHeaderImageUri)
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
