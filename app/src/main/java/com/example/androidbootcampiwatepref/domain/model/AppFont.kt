package com.example.androidbootcampiwatepref.domain.model

import androidx.compose.ui.text.font.FontFamily

/**
 * アプリのフォント設定を表すEnum
 * 
 * アプリ全体で使用するフォントファミリーを管理
 * 
 * @property DEFAULT システムデフォルト（Sans Serif）
 * @property SERIF 明朝体風（Serif）
 * @property MONOSPACE 等幅フォント（Monospace）
 * @property CURSIVE 筆記体風（Cursive）
 */
enum class AppFont(val fontFamily: FontFamily) {
    DEFAULT(FontFamily.Default),
    SERIF(FontFamily.Serif),
    MONOSPACE(FontFamily.Monospace),
    CURSIVE(FontFamily.Cursive)
}
