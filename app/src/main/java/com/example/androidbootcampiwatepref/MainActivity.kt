package com.example.androidbootcampiwatepref

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidbootcampiwatepref.ui.theme.AndroidBootcampIwatePrefTheme
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

//ルート定義
sealed interface ProfileRoutes {
    @Serializable
    data object View : ProfileRoutes

    @Serializable
    data object Edit : ProfileRoutes
}

//テーマの状態
enum class AppTheme {
    SYSTEM, LIGHT, DARK
}

//プロフィール情報のデータクラス
data class ProfileData(
    val nickname: String,
    val id: String,
    val bio: String,
    val genderIndex: Int,
    val birthDateMillis: Long?,
    val hobbies: List<String> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            //状態管理
            var currentTheme by remember { mutableStateOf(AppTheme.SYSTEM) }

            //プロフィールデータ全体を管理
            var profileData by remember {
                mutableStateOf(
                    ProfileData(
                        nickname = "",
                        id ="",
                        bio = "",
                        genderIndex = 0,
                        birthDateMillis = Calendar.getInstance().apply{ set(2000,0,1) }.timeInMillis,
                        hobbies = emptyList()
                    )
                )
            }
            //システムがダークモードかどうかを取得
            val systemIsDark = isSystemInDarkTheme()
            //実際にテーマに渡す
            val useDarkTheme = when (currentTheme) {
                AppTheme.SYSTEM -> systemIsDark
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }
            AndroidBootcampIwatePrefTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = ProfileRoutes.View
                    ) {
                        composable<ProfileRoutes.View> {
                            ProfileViewScreen(
                                profileData = profileData,
                                useDarkTheme = useDarkTheme,
                                onThemeToggle = {
                                    currentTheme = if (useDarkTheme) AppTheme.LIGHT else AppTheme.DARK
                                },
                                onEditClick = {
                                    navController.navigate(ProfileRoutes.Edit)
                                }
                            )
                        }

                        composable<ProfileRoutes.Edit> {
                            ProfileEditScreen(
                                profileData = profileData,
                                useDarkTheme = useDarkTheme,
                                onThemeToggle = {
                                    currentTheme = if (useDarkTheme) AppTheme.LIGHT else AppTheme.DARK
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onSaveClick = { updatedData ->
                                    profileData = updatedData
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// プロフィール閲覧画面
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

// プロフィール編集画面
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

@Composable
fun ProfileHeader(nickname: String, id: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp), // アイコンの下半分のためのスペース (アイコンサイズ100dpの半分)
        contentAlignment = Alignment.TopCenter
    ) {
        // ヘッダー画像
        Image(
            painter = painterResource(id = R.drawable.ic_my_hedder),
            contentDescription = "ヘッダー画像",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
        // プロフィールアイコンとニックネーム・IDを配置
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 120.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_my_icon),
                contentDescription = "アイコン",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Text(
                    text = nickname.ifEmpty { "" },
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (id.isEmpty()) "@" else "@$id",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}


//閲覧画面のコンポーザブル
@Composable
fun ProfileViewContent(innerPadding: PaddingValues,profileData: ProfileData){
    val genderOptions = listOf("男性","女性","回答しない")
    val birthDateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ){
        ProfileHeader(nickname = profileData.nickname, id = profileData.id)

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            //各項目をラベルと値で表示（ニックネームとIDは上で表示したので除外）
            ProfileInfoRow(label = "自己紹介",value = profileData.bio)
            ProfileInfoRow(label = "性別",value = genderOptions[profileData.genderIndex])
            ProfileInfoRow(label = "生年月日",value = profileData.birthDateMillis?.let { birthDateFormatter.format(Date(it)
            ) } ?: "未設定")

            //趣味・興味リスト
            Column(modifier = Modifier.fillMaxWidth()){
                Text(
                    text = "趣味・興味",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if(profileData.hobbies.isEmpty()){
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }else{
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ){
                        items(profileData.hobbies){ hobby ->
                            SuggestionChip(
                                onClick ={},
                                label = { Text(hobby) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditContent(
    innerPadding: PaddingValues,
    initialProfileData: ProfileData,
    onSaveClick: (ProfileData)-> Unit
) {
    // 状態変数を Column の外で定義
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
            modifier = Modifier
                .padding(16.dp),
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

            //ID
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
                    initialSelectedDateMillis = selectedDateMillis ?: Calendar.getInstance().timeInMillis
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

            //趣味・興味入力セクション
            Text(
                "趣味・興味",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                OutlinedTextField(
                    value = hobbyInput,
                    onValueChange = { hobbyInput = it },
                    label = { Text("趣味を入力") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(
                    onClick = {
                        if(hobbyInput.text.isNotBlank() && !hobbies.contains(hobbyInput.text)){
                            hobbies = hobbies.toMutableList().apply { add(hobbyInput.text) }
                            hobbyInput = TextFieldValue("")
                        }
                    }
                ){
                    Icon(Icons.Default.Add, contentDescription = "追加")
                }
            }

            //趣味タグ一覧
            if(hobbies.isNotEmpty()){
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ){
                    items(hobbies) { hobby ->
                        SuggestionChip(
                            onClick = {
                                hobbies = hobbies.toMutableList().apply{ remove(hobby) }
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

            //保存ボタン
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
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ){
                Text("保存")
            }
        }
    }
}