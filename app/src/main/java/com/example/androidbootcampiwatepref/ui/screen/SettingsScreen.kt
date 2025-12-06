package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.domain.model.AppFont
import com.example.androidbootcampiwatepref.domain.model.AppTheme

/**
 * 設定画面
 * 
 * アプリの各種設定を行う画面
 * テーマやフォントなどをドロップダウンメニューで選択可能
 * 
 * @param currentTheme 現在のテーマ設定
 * @param currentFont 現在のフォント設定
 * @param onThemeChange テーマ変更時のコールバック
 * @param onFontChange フォント変更時のコールバック
 * @param onBackClick 戻るボタンのクリック処理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: AppTheme,
    currentFont: AppFont,
    currentCardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onThemeChange: (AppTheme) -> Unit,
    onFontChange: (AppFont) -> Unit,
    onCardDesignChange: (com.example.androidbootcampiwatepref.domain.model.CardDesign) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // テーマ設定セクション
            SettingSection(
                title = "テーマ",
                description = "アプリの外観を変更"
            ) {
                ThemeDropdown(
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange
                )
            }
            
            // フォント設定セクション
            SettingSection(
                title = "フォント",
                description = "アプリ全体のフォントを変更"
            ) {
                FontDropdown(
                    currentFont = currentFont,
                    onFontChange = onFontChange
                )
            }
            
            // カードデザイン設定セクション
            SettingSection(
                title = "名刺デザイン",
                description = "プロフィール名刺の背景デザインを変更"
            ) {
                CardDesignDropdown(
                    currentCardDesign = currentCardDesign,
                    onCardDesignChange = onCardDesignChange
                )
            }
        }
    }
}

/**
 * 設定セクションコンポーネント
 * 
 * @param title セクションのタイトル
 * @param description セクションの説明
 * @param content セクションの内容（ドロップダウンなど）
 */
@Composable
fun SettingSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}

/**
 * テーマ選択ドロップダウン
 * 
 * @param currentTheme 現在選択されているテーマ
 * @param onThemeChange テーマ変更時のコールバック
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeDropdown(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    val themeLabels = mapOf(
        AppTheme.SYSTEM to "システム設定に従う",
        AppTheme.LIGHT to "ライトモード",
        AppTheme.DARK to "ダークモード"
    )
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = themeLabels[currentTheme] ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AppTheme.entries.forEach { theme ->
                DropdownMenuItem(
                    text = { Text(themeLabels[theme] ?: "") },
                    onClick = {
                        onThemeChange(theme)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

/**
 * フォント選択ドロップダウン
 * 
 * @param currentFont 現在選択されているフォント
 * @param onFontChange フォント変更時のコールバック
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontDropdown(
    currentFont: AppFont,
    onFontChange: (AppFont) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    val fontLabels = mapOf(
        AppFont.DEFAULT to "標準（ゴシック体）",
        AppFont.SERIF to "明朝体",
        AppFont.MONOSPACE to "等幅フォント",
        AppFont.CURSIVE to "筆記体"
    )
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = fontLabels[currentFont] ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AppFont.entries.forEach { font ->
                DropdownMenuItem(
                    text = { Text(fontLabels[font] ?: "") },
                    onClick = {
                        onFontChange(font)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

/**
 * カードデザイン選択ドロップダウン
 * 
 * @param currentCardDesign 現在選択されているカードデザイン
 * @param onCardDesignChange カードデザイン変更時のコールバック
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDesignDropdown(
    currentCardDesign: com.example.androidbootcampiwatepref.domain.model.CardDesign,
    onCardDesignChange: (com.example.androidbootcampiwatepref.domain.model.CardDesign) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = currentCardDesign.displayName,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            com.example.androidbootcampiwatepref.domain.model.CardDesign.entries.forEach { design ->
                DropdownMenuItem(
                    text = { Text(design.displayName) },
                    onClick = {
                        onCardDesignChange(design)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

