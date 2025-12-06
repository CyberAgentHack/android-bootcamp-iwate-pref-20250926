package com.example.androidbootcampiwatepref.domain.model

import kotlinx.serialization.Serializable
import com.example.androidbootcampiwatepref.domain.model.GenderOption

/**
 * QRコードで交換する名刺データ
 * 
 * JSONにシリアライズしてQRコードに埋め込む
 */
@Serializable
data class BusinessCardData(
    val nickname: String,
    val bio: String,
    val gender: String,
    val birthDate: String?, // "yyyy-MM-dd" 形式
    val hobbies: List<String> = emptyList(),
    val cardDesign: String = "CLASSIC"
) {
    /**
     * JSONにシリアライズ
     */
    fun toJson(): String {
        return kotlinx.serialization.json.Json.encodeToString(serializer(), this)
    }
    
    companion object {
        /**
         * JSONからデシリアライズ
         */
        fun fromJson(json: String): BusinessCardData? {
            return try {
                kotlinx.serialization.json.Json.decodeFromString<BusinessCardData>(json)
            } catch (e: Exception) {
                null
            }
        }
        
        /**
         * ProfileDataから変換
         */
        fun fromProfileData(
            nickname: String,
            bio: String,
            genderIndex: Int,
            birthDateMillis: Long?,
            hobbies: List<String>,
            cardDesign: String
        ): BusinessCardData {
            // 性別インデックスを文字列に変換（共通定数を使用）
            val genderText = GenderOption.fromIndex(genderIndex).label
            
            // 生年月日をミリ秒から"yyyy-MM-dd"形式の文字列に変換
            val birthDateText = birthDateMillis?.let {
                val date = java.util.Date(it)
                val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                formatter.format(date)
            }
            
            // BusinessCardDataインスタンスを生成して返す
            return BusinessCardData(
                nickname = nickname,
                bio = bio,
                gender = genderText,
                birthDate = birthDateText,
                hobbies = hobbies,
                cardDesign = cardDesign
            )
        }
    }
}
