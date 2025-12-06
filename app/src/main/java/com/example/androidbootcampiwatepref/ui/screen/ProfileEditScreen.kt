package com.example.androidbootcampiwatepref.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
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
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
 * @param onBackClick 戻るボタンのクリック処理（編集をキャンセル）
 * @param onSaveClick 保存ボタンのクリック処理（編集内容を保存）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    profileData: ProfileData,
    profileImageUri: String?,
    headerImageUri: String?,
    currentCardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onBackClick: () -> Unit,
    onSaveClick: (ProfileData, String?, String?, String?) -> Unit
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
            cardDesign = currentCardDesign,
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
    cardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onSaveClick: (ProfileData, String?, String?, String?) -> Unit
) {
    val context = LocalContext.current
    // --- 状態変数の定義 ---
    // TextFieldValueを使用してカーソル位置なども管理
    var nickname by remember { mutableStateOf(TextFieldValue(initialProfileData.nickname)) }
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
    var currentProfileImageOriginalUri by remember { mutableStateOf<String?>(null) }
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
            // 元画像を保存
            currentProfileImageOriginalUri = sourceUri.toString()
            
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

    // ページ管理（0: 基本情報、1: 詳細情報）
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 名刺カード（スライド切り替え）
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.63f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        // 表面（アイコンとニックネーム編集）
                        EditCardFront(
                            nickname = nickname,
                            onNicknameChange = { nickname = it },
                            profileImageUri = currentProfileImageUri,
                            cardDesign = cardDesign,
                            onProfileImageClick = {
                                profileImageLauncher.launch("image/*")
                            }
                        )
                    }
                    1 -> {
                        // 裏面（詳細情報編集）
                        EditCardBack(
                            bio = bio,
                            onBioChange = { bio = it },
                            selectedGenderIndex = selectedGenderIndex,
                            genderOptions = genderOptions,
                            onGenderChange = { selectedGenderIndex = it },
                            selectedDateMillis = selectedDateMillis,
                            birthDateFormatter = birthDateFormatter,
                            onDateClick = { showDatePickerDialog = true },
                            hobbies = hobbies,
                            hobbyInput = hobbyInput,
                            onHobbyInputChange = { hobbyInput = it },
                            cardDesign = cardDesign,
                            onAddHobby = {
                                if (hobbyInput.text.isNotBlank()) {
                                    hobbies.add(hobbyInput.text)
                                    hobbyInput = TextFieldValue("")
                                }
                            },
                            onRemoveHobby = { index ->
                                hobbies.removeAt(index)
                            }
                        )
                    }
                }
            }
        }
        
        // ページインジケーター
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) { index ->
                val coroutineScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                        .background(
                            if (pagerState.currentPage == index) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            CircleShape
                        )
                        .clickable { 
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                )
            }
        }
        
        // 保存ボタン（名刺の外側）
        Button(
            onClick = {
                val updatedData = ProfileData(
                    nickname = nickname.text,
                    bio = bio.text,
                    genderIndex = selectedGenderIndex,
                    birthDateMillis = selectedDateMillis,
                    hobbies = hobbies
                )
                onSaveClick(updatedData, currentProfileImageUri, currentProfileImageOriginalUri, currentHeaderImageUri)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("保存")
        }
    }
    
    // 日付選択ダイアログ
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePickerDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("キャンセル")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * 編集カードの表面（アイコンとニックネーム）
 */
@Composable
fun EditCardFront(
    nickname: TextFieldValue,
    onNicknameChange: (TextFieldValue) -> Unit,
    profileImageUri: String?,
    cardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onProfileImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cardDesign.frontBrush)
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // アイコン（タップで変更）
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable(onClick = onProfileImageClick)
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "プロフィール画像",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_my_icon),
                    contentDescription = "プロフィール画像",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            
            // 編集アイコン
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "画像を変更",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // ニックネーム編集
        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = { Text("ニックネーム", color = cardDesign.frontTextColor.copy(alpha = 0.7f)) },
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = cardDesign.frontTextColor
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = cardDesign.frontTextColor,
                unfocusedTextColor = cardDesign.frontTextColor,
                focusedBorderColor = cardDesign.frontTextColor,
                unfocusedBorderColor = cardDesign.frontTextColor.copy(alpha = 0.5f)
            )
        )
    }
}

/**
 * 編集カードの裏面（詳細情報）
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditCardBack(
    bio: TextFieldValue,
    onBioChange: (TextFieldValue) -> Unit,
    selectedGenderIndex: Int,
    genderOptions: List<String>,
    onGenderChange: (Int) -> Unit,
    selectedDateMillis: Long?,
    birthDateFormatter: SimpleDateFormat,
    onDateClick: () -> Unit,
    hobbies: MutableList<String>,
    hobbyInput: TextFieldValue,
    onHobbyInputChange: (TextFieldValue) -> Unit,
    cardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onAddHobby: () -> Unit,
    onRemoveHobby: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cardDesign.backBrush)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 自己紹介
        OutlinedTextField(
            value = bio,
            onValueChange = onBioChange,
            label = { Text("📝 自己紹介", color = cardDesign.backTextColor.copy(alpha = 0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = cardDesign.backTextColor,
                unfocusedTextColor = cardDesign.backTextColor,
                focusedBorderColor = cardDesign.backTextColor,
                unfocusedBorderColor = cardDesign.backTextColor.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 生年月日
        OutlinedTextField(
            value = selectedDateMillis?.let {
                birthDateFormatter.format(Date(it))
            } ?: "",
            onValueChange = {},
            label = { Text("🎂 生年月日", color = cardDesign.backTextColor.copy(alpha = 0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDateClick),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = cardDesign.backTextColor,
                disabledBorderColor = cardDesign.backTextColor.copy(alpha = 0.5f),
                disabledLabelColor = cardDesign.backTextColor.copy(alpha = 0.7f)
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 性別選択
        Text(
            "⚥ 性別",
            style = MaterialTheme.typography.labelMedium,
            color = cardDesign.backTextColor.copy(alpha = 0.8f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            genderOptions.forEachIndexed { index, label ->
                FilterChip(
                    selected = selectedGenderIndex == index,
                    onClick = { onGenderChange(index) },
                    label = { Text(label, style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 趣味入力
        Text(
            "🎨 趣味・興味",
            style = MaterialTheme.typography.labelMedium,
            color = cardDesign.backTextColor.copy(alpha = 0.8f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = hobbyInput,
                onValueChange = onHobbyInputChange,
                label = { Text("追加", color = cardDesign.backTextColor.copy(alpha = 0.7f)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = cardDesign.backTextColor),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = cardDesign.backTextColor,
                    unfocusedTextColor = cardDesign.backTextColor,
                    focusedBorderColor = cardDesign.backTextColor,
                    unfocusedBorderColor = cardDesign.backTextColor.copy(alpha = 0.5f)
                )
            )
            IconButton(onClick = onAddHobby) {
                Icon(Icons.Default.Add, contentDescription = "追加", modifier = Modifier.size(20.dp))
            }
        }
        
        // 趣味リスト
        if (hobbies.isNotEmpty()) {
            @OptIn(ExperimentalLayoutApi::class)
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
