package com.example.androidbootcampiwatepref.domain.model

/**
 * プロフィール情報のデータクラス
 * 
 * ユーザーのプロフィール情報を保持する不変のデータモデル
 * UIレイヤーとデータレイヤー間でやり取りされるデータ構造
 * 
 * @property nickname ニックネーム（表示名）
 * @property id ユーザーID（@付きで表示される識別子）
 * @property bio 自己紹介文
 * @property genderIndex 性別インデックス（0:男性、1:女性、2:その他）
 * @property birthDateMillis 生年月日（エポックミリ秒、未設定の場合はnull）
 * @property hobbies 趣味のリスト（デフォルトは空リスト）
 */
data class ProfileData(
    val nickname: String,
    val id: String,
    val bio: String,
    val genderIndex: Int,
    val birthDateMillis: Long?,
    val hobbies: List<String> = emptyList()
)
