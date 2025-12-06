package com.example.androidbootcampiwatepref.domain.model

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * プロフィール関連の共通定数
 * 
 * 複数の画面で共通して使用される定数を集約して管理する
 * 定義を一箇所にまとめることで、変更時の修正漏れを防ぐ
 */

/**
 * 性別の選択肢（Enum版）
 * 
 * 型安全に性別を扱うためのEnum定義
 * ProfileViewScreen、ProfileEditScreen、QRコード機能など
 * 複数の画面で使用される
 * 
 * @property label 画面表示用のラベル
 */
enum class GenderOption(val label: String) {
    /** 男性 */
    MAN("男性"),
    
    /** 女性 */
    WOMAN("女性"),
    
    /** 回答しない */
    UNKNOWN("回答しない");
    
    companion object {
        /**
         * インデックスから性別を取得
         * 
         * @param index 性別のインデックス（0:男性、1:女性、2:回答しない）
         * @return 対応する性別、範囲外の場合はUNKNOWN
         */
        fun fromIndex(index: Int): GenderOption {
            return entries.getOrNull(index) ?: UNKNOWN
        }
        
        /**
         * 全ての性別ラベルをリストで取得
         * 
         * @return 性別ラベルのリスト（UI表示用）
         */
        fun labels(): List<String> = entries.map { it.label }
    }
}

/**
 * 性別の選択肢リスト（下位互換性のため残す）
 * 
 * 既存コードとの互換性を保つため、GenderOption.labels()を返す
 */
val GENDER_OPTIONS: List<String> = GenderOption.labels()

/**
 * 生年月日のフォーマッター
 * 
 * 生年月日を「yyyy/MM/dd」形式（例: 2000/01/01）で表示するための
 * SimpleDateFormatインスタンス
 * ロケールは日本（JAPAN）に設定
 */
val BIRTH_DATE_FORMATTER = SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN)

/**
 * デフォルトの生年月日（2000年1月1日）
 * 
 * プロフィールデータの初期値として使用されるデフォルトの誕生日
 * Calendar.getInstance()で現在の日時を取得し、2000/01/01に設定
 */
val DEFAULT_BIRTH_DATE_MILLIS: Long = java.util.Calendar.getInstance().apply { 
    set(2000, 0, 1) // 月は0始まり（0=1月）
}.timeInMillis

/**
 * 未設定を表す文字列
 * 
 * プロフィール項目が未入力の場合に表示する文字列
 */
const val UNSET_TEXT = "未設定"
