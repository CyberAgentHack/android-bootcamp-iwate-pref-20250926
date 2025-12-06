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

/**
 * Context拡張プロパティ
 * DataStoreのインスタンスをシングルトンとして定義
 * preferencesDataStoreデリゲートにより、アプリ全体で同じインスタンスが共有される
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_preferences")

/**
 * プロフィールデータを永続化するためのDataStoreクラス
 * 
 * このクラスは以下の責務を持つ:
 * - プロフィール情報（ニックネーム、ID、自己紹介など）の保存と読み込み
 * - テーマ設定の保存と読み込み
 * - 非同期処理によるデータアクセス（Kotlin Flow使用）
 * - データの型安全な管理
 * 
 * @param context アプリケーションコンテキスト
 */
class ProfileDataStore(private val context: Context) {
    
    /**
     * PreferencesのKey定義
     * 各データ項目に対応するキーを定義
     * キー名の重複を防ぐためcompanion objectで管理
     */
    companion object {
        // 各データ項目のキー定義
        // DataStoreはKey-Value形式でデータを保存するため、型安全なキーを定義
        private val NICKNAME_KEY = stringPreferencesKey("nickname")          // ユーザーのニックネーム
        private val ID_KEY = stringPreferencesKey("id")                      // ユーザーID
        private val BIO_KEY = stringPreferencesKey("bio")                    // 自己紹介文
        private val GENDER_INDEX_KEY = intPreferencesKey("gender_index")     // 性別インデックス（0:男性、1:女性、2:その他）
        private val BIRTH_DATE_KEY = longPreferencesKey("birth_date_millis") // 誕生日（エポックミリ秒）
        private val HOBBIES_KEY = stringPreferencesKey("hobbies")           // 趣味リスト（カンマ区切りで保存）
        private val THEME_KEY = stringPreferencesKey("theme")               // テーマ設定
    }
    
    // --- ニックネーム関連 ---
    
    /**
     * ニックネームを保存
     * 
     * @param nickname 保存するニックネーム
     * suspend関数としてコルーチン内で非同期実行される
     * DataStore.editを使用してPreferencesを更新
     */
    suspend fun saveNickname(nickname: String) {
        context.dataStore.edit { preferences ->
            preferences[NICKNAME_KEY] = nickname
        }
    }
    
    /**
     * ニックネームを取得（Flow）
     * 
     * Flowを返すことで、データの変更を監視可能
     * データが更新されると自動的に新しい値が流れてくる
     * mapオペレータでPreferencesから値を取り出す
     * キーが存在しない場合は空文字列を返す
     */
    val nicknameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[NICKNAME_KEY] ?: ""
    }
    
    // --- ID関連 ---
    
    /**
     * IDを保存
     * 
     * @param id 保存するユーザーID
     */
    suspend fun saveId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = id
        }
    }
    
    /**
     * IDを取得（Flow）
     * 
     * データ変更を監視可能なFlowとして返す
     */
    val idFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ID_KEY] ?: ""
    }
    
    // --- 自己紹介関連 ---
    
    /**
     * 自己紹介を保存
     * 
     * @param bio 保存する自己紹介文
     */
    suspend fun saveBio(bio: String) {
        context.dataStore.edit { preferences ->
            preferences[BIO_KEY] = bio
        }
    }
    
    /**
     * 自己紹介を取得（Flow）
     * 
     * データ変更を監視可能なFlowとして返す
     */
    val bioFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[BIO_KEY] ?: ""
    }
    
    // --- 性別関連 ---
    
    /**
     * 性別インデックスを保存
     * 
     * @param genderIndex 性別インデックス（0:男性、1:女性、2:その他）
     */
    suspend fun saveGenderIndex(genderIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[GENDER_INDEX_KEY] = genderIndex
        }
    }
    
    /**
     * 性別インデックスを取得（Flow）
     * 
     * データが未設定の場合は0（男性）を返す
     */
    val genderIndexFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[GENDER_INDEX_KEY] ?: 0
    }
    
    // --- 生年月日関連 ---
    
    /**
     * 生年月日を保存
     * 
     * @param millis 生年月日（エポックミリ秒）、nullの場合はキーを削除
     * エポックミリ秒: 1970年1月1日0時0分0秒からの経過ミリ秒
     */
    suspend fun saveBirthDate(millis: Long?) {
        context.dataStore.edit { preferences ->
            if (millis != null) {
                preferences[BIRTH_DATE_KEY] = millis
            } else {
                // nullの場合はキーを削除（データをクリア）
                preferences.remove(BIRTH_DATE_KEY)
            }
        }
    }
    
    /**
     * 生年月日を取得（Flow）
     * 
     * データが未設定の場合はnullを返す
     * nullable型を使用して未設定状態を表現
     */
    val birthDateFlow: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[BIRTH_DATE_KEY]
    }
    
    // --- 趣味関連 ---
    
    /**
     * 趣味リストを保存
     * 
     * @param hobbies 趣味のリスト
     * DataStoreはList型を直接保存できないため、カンマ区切りの文字列に変換して保存
     * 例: ["読書", "映画", "旅行"] → "読書,映画,旅行"
     */
    suspend fun saveHobbies(hobbies: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[HOBBIES_KEY] = hobbies.joinToString(",")
        }
    }
    
    /**
     * 趣味リストを取得（Flow）
     * 
     * カンマ区切りの文字列をList<String>に変換して返す
     * 空文字列の場合は空のリストを返す
     * 例: "読書,映画,旅行" → ["読書", "映画", "旅行"]
     */
    val hobbiesFlow: Flow<List<String>> = context.dataStore.data.map { preferences ->
        val hobbiesString = preferences[HOBBIES_KEY] ?: ""
        if (hobbiesString.isEmpty()) {
            emptyList()
        } else {
            hobbiesString.split(",")
        }
    }
    
    // --- テーマ設定関連 ---
    
    /**
     * テーマ設定を保存
     * 
     * @param theme テーマ名（"SYSTEM", "LIGHT", "DARK"）
     */
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }
    
    /**
     * テーマ設定を取得（Flow）
     * 
     * データが未設定の場合は"SYSTEM"（システム設定に従う）を返す
     */
    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "SYSTEM"
    }
    
    // --- 一括操作 ---
    
    /**
     * すべてのプロフィールデータを一度に保存
     * 
     * 複数の値を一度のedit操作で保存することで、パフォーマンスを向上
     * 個別に保存する代わりに、この関数を使うことでディスクI/O回数を削減
     * 
     * @param nickname ニックネーム
     * @param id ユーザーID
     * @param bio 自己紹介
     * @param genderIndex 性別インデックス
     * @param birthDateMillis 生年月日（エポックミリ秒）、nullの場合はキーを削除
     * @param hobbies 趣味リスト
     */
    suspend fun saveProfileData(
        nickname: String,
        id: String,
        bio: String,
        genderIndex: Int,
        birthDateMillis: Long?,
        hobbies: List<String>
    ) {
        // 単一のedit操作で全データを更新
        // これによりDataStoreへの書き込みが1回で完了し、効率的
        context.dataStore.edit { preferences ->
            preferences[NICKNAME_KEY] = nickname
            preferences[ID_KEY] = id
            preferences[BIO_KEY] = bio
            preferences[GENDER_INDEX_KEY] = genderIndex
            
            // 生年月日の処理
            if (birthDateMillis != null) {
                preferences[BIRTH_DATE_KEY] = birthDateMillis
            } else {
                preferences.remove(BIRTH_DATE_KEY)
            }
            
            // 趣味リストをカンマ区切り文字列に変換して保存
            preferences[HOBBIES_KEY] = hobbies.joinToString(",")
        }
    }
    
    /**
     * すべてのデータをクリア
     * 
     * DataStoreに保存されているすべての設定をリセット
     * ログアウトやデータリセット機能で使用
     */
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
