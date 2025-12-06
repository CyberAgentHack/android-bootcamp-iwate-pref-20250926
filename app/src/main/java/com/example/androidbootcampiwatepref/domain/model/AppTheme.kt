package com.example.androidbootcampiwatepref.domain.model

/**
 * アプリのテーマ設定を表すEnum
 * 
 * ライトモード/ダークモードの選択を管理
 * 
 * @property SYSTEM システムの設定に従う（Android OSの設定に連動）
 * @property LIGHT ライトモード（明るいテーマ）を強制
 * @property DARK ダークモード（暗いテーマ）を強制
 */
enum class AppTheme {
    SYSTEM, LIGHT, DARK
}
