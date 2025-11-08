package com.example.androidbootcampiwatepref

import android.content.ClipData
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
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

/*
//テーマの状態
enum class AppTheme {
    SYSTEM, LIGHT, DARK
}

//画面の状態
enum class ProfileScreenState {
    VIEW, EDIT
}
//プロフィール情報のデータクラス
data class ProfileData(
    val nickname: String,
    val id: String,
    val bio: String,
    val genderIndex: Int,
    val birthDateMillis: Long?
)
*/
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*
            //CountUp()
            MyLayout()
            HomeScreen()
            */
            AndroidBootcampIwatePrefTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Home,
                    ){
                        composable<Routes.Home>{
                            HomeScreen(
                                modifier = Modifier.fillMaxSize(),
                                navigateTo = { route ->
                                    navController.navigate(route)
                                }
                            )
                        }
                        composable<Routes.Search>{
                            SearchScreen(
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
        /*
        setContent{
            //状態管理
            var currentTheme by remember { mutableStateOf(AppTheme.SYSTEM) }
            var screenState by remember { mutableStateOf(ProfileScreenState.VIEW) }

            //プロフィールデータ全体を管理
            var profileData by remember {
                mutableStateOf(
                    ProfileData(
                        nickname = "",
                        id ="",
                        bio = "",
                        genderIndex = 0,
                        birthDateMillis = Calendar.getInstance().apply{ set(2000,0,1) }.timeInMillis
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
            AndroidBootcampIwatePrefTheme(darkTheme = useDarkTheme) { // darkTheme を渡す
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(if(screenState == ProfileScreenState.VIEW)"プロフィール" else "プロフィール編集") },
                            navigationIcon = {
                                if(screenState == ProfileScreenState.EDIT){
                                   IconButton(onClick = { screenState = ProfileScreenState.VIEW }) {
                                       Icon(Icons.Default.ArrowBack, contentDescription = "キャンセル")
                                   }
                                }
                            },
                            actions = {
                                //閲覧画面のときに編集ボタンを表示
                                if(screenState == ProfileScreenState.VIEW){
                                    IconButton(onClick = { screenState = ProfileScreenState.EDIT }) {
                                        Icon(Icons.Default.Edit, contentDescription = "編集")
                                    }
                                }
                                //テーマ切り替えボタン
                                IconButton(onClick = {
                                    currentTheme = if (useDarkTheme) AppTheme.LIGHT else AppTheme.DARK
                                }) {
                                    Text(if (useDarkTheme) "☀️" else "🌙")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    //状態に応じて表示画面切り替え
                    when (screenState) {
                        ProfileScreenState.VIEW -> {
                            ProfileViewContent(
                                innerPadding = innerPadding,
                                profileData = profileData
                            )
                        }
                        ProfileScreenState.EDIT -> {
                        ProfileEditContent(
                            innerPadding = innerPadding,
                            initialProfileData = profileData,
                            onSaveClick = { updateData ->
                                profileData = updateData
                                screenState = ProfileScreenState.VIEW
                            }
                        )
                    }
                }
            }
        }*/
    }
}

//@Composable
        /*
        fun CountUp(
            modifier: Modifier = Modifier,
        ){
            var count = remember { 0}
            Column(
                modifier = modifier
            ){
                Text("count:$count")
                Button(
                    onClick = {
                    count++
                }
                ) {
                    Text("count up!")
                }
            }
        }*/

private const val contentTypeOfItem = "CONTENT_TYPE_ITEM"


@Composable
fun MyLayout() {
    Column {
        /*
        Row{
            Text("Row1")
            Spacer(modifier = Modifier.width(12.dp))
            Text("Row1の説明")
        }
        Row{
            Text("Row2")
            Spacer(modifier = Modifier.width(12.dp))
            Text("Row2の説明")
        }
        Row{
            Text("Row3")
            Spacer(modifier = Modifier.width(12.dp))
            Text("Row3の説明")
        }*/
        /*
        (0..100000).forEach {
            Item(it)
        }
        */
        LazyColumn {
            items(
                count = 1000,
                key = { index -> index },
                contentType = { contentTypeOfItem },
            ){  count ->
                Item(count)
            }
        }

    }
}
/*
@Composable
fun HomeScreen(
    modifier:  Modifier = Modifier
) {
    Box(
        modifier = modifier    ){
        MyLayout()
    }
}
*/
@Composable
fun Item(count: Int){
    Row{
        Text("Row$count")
        Spacer(modifier = Modifier.width(12.dp))
        Text("Row${count}の説明")
    }
}
/*
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

            //保存ボタン
            Button(
                onClick = {
                    val updatedData = ProfileData(
                        nickname = nickname.text,
                        id = id.text,
                        bio = bio.text,
                        genderIndex = selectedGenderIndex,
                        birthDateMillis = selectedDateMillis
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidBootcampIwatePrefTheme {
        Greeting("Android")
    }
}
}
*/
sealed interface Routes {
    @kotlinx.serialization.Serializable
    data object Home : Routes

    @Serializable
    data object Search : Routes
}
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Search Screen")
    }
}
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateTo: (route: Routes) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text ="Home Screen")
            Button(onClick = { navigateTo(Routes.Search) }) {
                Text(text = "Go to Search Screen")
            }
        }
    }
}