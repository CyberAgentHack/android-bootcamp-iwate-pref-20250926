package com.example.androidbootcampiwatepref.domain.model

/**
 * プロフィール情報のデータクラス
 */
data class ProfileData(
    val nickname: String,
    val id: String,
    val bio: String,
    val genderIndex: Int,
    val birthDateMillis: Long?,
    val hobbies: List<String> = emptyList()
)
