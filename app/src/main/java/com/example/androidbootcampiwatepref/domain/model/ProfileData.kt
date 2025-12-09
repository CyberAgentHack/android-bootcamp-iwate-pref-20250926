package com.example.androidbootcampiwatepref.domain.model

/**
 * プロフィール情報のデータクラス
 * 
 * ユーザーのプロフィール情報を保持する不変のデータモデル
 * UIレイヤーとデータレイヤー間でやり取りされるデータ構造
 * 
 * @property nickname ニックネーム（表示名）
 * @property bio 自己紹介文
 * @property genderIndex 性別インデックス（0:男性、1:女性、2:その他）
 * @property birthDateMillis 生年月日（エポックミリ秒、未設定の場合はnull）
 * @property hobbies 趣味のリスト（デフォルトは空リスト）
 * @property twitterUrl Twitter/XのURL
 * @property instagramUrl InstagramのURL
 * @property facebookUrl FacebookのURL
 * @property lineUrl LINEのURL
 * @property phoneNumber 電話番号
 * @property email メールアドレス
 */
data class ProfileData(
    val nickname: String = "未設定",
    val bio: String = "",
    val genderIndex: Int = 0,
    val birthDateMillis: Long? = DEFAULT_BIRTH_DATE_MILLIS,
    val hobbies: List<String> = emptyList(),
    val twitterUrl: String = "",
    val instagramUrl: String = "",
    val facebookUrl: String = "",
    val lineUrl: String = "",
    val phoneNumber: String = "",
    val email: String = ""
)
