package com.example.androidbootcampiwatepref.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStoreのインスタンスを拡張プロパティとして定義
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_preferences")

/**
 * プロフィールデータを永続化するためのDataStoreクラス
 */
class ProfileDataStore(private val context: Context) {
    
    // PreferencesのKey定義
    companion object {
        private val NICKNAME_KEY = stringPreferencesKey("nickname")
        private val ID_KEY = stringPreferencesKey("id")
        private val BIO_KEY = stringPreferencesKey("bio")
        private val GENDER_INDEX_KEY = intPreferencesKey("gender_index")
        private val BIRTH_DATE_KEY = longPreferencesKey("birth_date_millis")
        private val HOBBIES_KEY = stringPreferencesKey("hobbies") // カンマ区切りで保存
        private val THEME_KEY = stringPreferencesKey("theme")
    }
    
    /**
     * ニックネームを保存
     */
    suspend fun saveNickname(nickname: String) {
        context.dataStore.edit { preferences ->
            preferences[NICKNAME_KEY] = nickname
        }
    }
    
    /**
     * ニックネームを取得
     */
    val nicknameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[NICKNAME_KEY] ?: ""
    }
    
    /**
     * IDを保存
     */
    suspend fun saveId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = id
        }
    }
    
    /**
     * IDを取得
     */
    val idFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ID_KEY] ?: ""
    }
    
    /**
     * 自己紹介を保存
     */
    suspend fun saveBio(bio: String) {
        context.dataStore.edit { preferences ->
            preferences[BIO_KEY] = bio
        }
    }
    
    /**
     * 自己紹介を取得
     */
    val bioFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[BIO_KEY] ?: ""
    }
    
    /**
     * 性別インデックスを保存
     */
    suspend fun saveGenderIndex(genderIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[GENDER_INDEX_KEY] = genderIndex
        }
    }
    
    /**
     * 性別インデックスを取得
     */
    val genderIndexFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[GENDER_INDEX_KEY] ?: 0
    }
    
    /**
     * 生年月日を保存
     */
    suspend fun saveBirthDate(millis: Long?) {
        context.dataStore.edit { preferences ->
            if (millis != null) {
                preferences[BIRTH_DATE_KEY] = millis
            } else {
                preferences.remove(BIRTH_DATE_KEY)
            }
        }
    }
    
    /**
     * 生年月日を取得
     */
    val birthDateFlow: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[BIRTH_DATE_KEY]
    }
    
    /**
     * 趣味リストを保存
     */
    suspend fun saveHobbies(hobbies: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[HOBBIES_KEY] = hobbies.joinToString(",")
        }
    }
    
    /**
     * 趣味リストを取得
     */
    val hobbiesFlow: Flow<List<String>> = context.dataStore.data.map { preferences ->
        val hobbiesString = preferences[HOBBIES_KEY] ?: ""
        if (hobbiesString.isEmpty()) {
            emptyList()
        } else {
            hobbiesString.split(",")
        }
    }
    
    /**
     * テーマ設定を保存
     */
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }
    
    /**
     * テーマ設定を取得
     */
    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "SYSTEM"
    }
    
    /**
     * すべてのプロフィールデータを一度に保存
     */
    suspend fun saveProfileData(
        nickname: String,
        id: String,
        bio: String,
        genderIndex: Int,
        birthDateMillis: Long?,
        hobbies: List<String>
    ) {
        context.dataStore.edit { preferences ->
            preferences[NICKNAME_KEY] = nickname
            preferences[ID_KEY] = id
            preferences[BIO_KEY] = bio
            preferences[GENDER_INDEX_KEY] = genderIndex
            if (birthDateMillis != null) {
                preferences[BIRTH_DATE_KEY] = birthDateMillis
            } else {
                preferences.remove(BIRTH_DATE_KEY)
            }
            preferences[HOBBIES_KEY] = hobbies.joinToString(",")
        }
    }
    
    /**
     * すべてのデータをクリア
     */
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
