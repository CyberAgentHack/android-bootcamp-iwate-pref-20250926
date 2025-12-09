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
        private val BIO_KEY = stringPreferencesKey("bio")                    // 自己紹介文
        private val GENDER_INDEX_KEY = intPreferencesKey("gender_index")     // 性別インデックス（0:男性、1:女性、2:その他）
        private val BIRTH_DATE_KEY = longPreferencesKey("birth_date_millis") // 誕生日（エポックミリ秒）
        private val HOBBIES_KEY = stringPreferencesKey("hobbies")           // 趣味リスト（カンマ区切りで保存）
        private val TWITTER_URL_KEY = stringPreferencesKey("twitter_url")   // Twitter/X URL
        private val INSTAGRAM_URL_KEY = stringPreferencesKey("instagram_url") // Instagram URL
        private val FACEBOOK_URL_KEY = stringPreferencesKey("facebook_url")   // Facebook URL
        private val LINE_URL_KEY = stringPreferencesKey("line_url")           // LINE URL
        private val PHONE_NUMBER_KEY = stringPreferencesKey("phone_number")   // 電話番号
        private val EMAIL_KEY = stringPreferencesKey("email")                 // メールアドレス
        private val THEME_KEY = stringPreferencesKey("theme")               // テーマ設定
        private val FONT_KEY = stringPreferencesKey("font")                 // フォント設定
        private val CARD_DESIGN_KEY = stringPreferencesKey("card_design")   // 名刺デザイン設定
        private val PROFILE_IMAGE_URI_KEY = stringPreferencesKey("profile_image_uri") // プロフィール画像URI（トリミング済み）
        private val PROFILE_IMAGE_ORIGINAL_URI_KEY = stringPreferencesKey("profile_image_original_uri") // プロフィール画像の元画像URI
        private val HEADER_IMAGE_URI_KEY = stringPreferencesKey("header_image_uri")   // ヘッダー画像URI
        private val SAVED_CARDS_KEY = stringPreferencesKey("saved_business_cards")    // 受け取った名刺リスト（JSON配列）
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
    
    // --- SNS URL関連 ---
    // 各SNSプラットフォームのプロフィールURLを保存・取得する機能
    // ユーザーが入力したURLをDataStoreに永続化し、名刺に表示する
    
    /**
     * Twitter/X URLを保存
     * 
     * @param url Twitter/XのプロフィールURL（例: https://x.com/username）
     */
    suspend fun saveTwitterUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[TWITTER_URL_KEY] = url
        }
    }
    
    /**
     * Twitter/X URLを取得（Flow）
     * 
     * @return Twitter/XのURL、未設定の場合は空文字列
     */
    val twitterUrlFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TWITTER_URL_KEY] ?: ""
    }
    
    /**
     * Instagram URLを保存
     * 
     * @param url InstagramのプロフィールURL（例: https://instagram.com/username）
     */
    suspend fun saveInstagramUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[INSTAGRAM_URL_KEY] = url
        }
    }
    
    /**
     * Instagram URLを取得（Flow）
     * 
     * @return InstagramのURL、未設定の場合は空文字列
     */
    val instagramUrlFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[INSTAGRAM_URL_KEY] ?: ""
    }
    
    /**
     * Facebook URLを保存
     * 
     * @param url FacebookのプロフィールURL（例: https://facebook.com/username）
     */
    suspend fun saveFacebookUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[FACEBOOK_URL_KEY] = url
        }
    }
    
    /**
     * Facebook URLを取得（Flow）
     * 
     * @return FacebookのURL、未設定の場合は空文字列
     */
    val facebookUrlFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[FACEBOOK_URL_KEY] ?: ""
    }
    
    /**
     * LINE URLを保存
     * 
     * @param url 保存するURL
     */
    suspend fun saveLineUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[LINE_URL_KEY] = url
        }
    }
    
    /**
     * LINE URLを取得（Flow）
     * 
     * @return LINEのURL、未設定の場合は空文字列
     */
    val lineUrlFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LINE_URL_KEY] ?: ""
    }
    
    /**
     * 電話番号を保存
     * 
     * @param phoneNumber 保存する電話番号
     */
    suspend fun savePhoneNumber(phoneNumber: String) {
        context.dataStore.edit { preferences ->
            preferences[PHONE_NUMBER_KEY] = phoneNumber
        }
    }
    
    /**
     * 電話番号を取得（Flow）
     * 
     * @return 電話番号、未設定の場合は空文字列
     */
    val phoneNumberFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PHONE_NUMBER_KEY] ?: ""
    }
    
    /**
     * メールアドレスを保存
     * 
     * @param email 保存するメールアドレス
     */
    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }
    
    /**
     * メールアドレスを取得（Flow）
     * 
     * @return メールアドレス、未設定の場合は空文字列
     */
    val emailFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[EMAIL_KEY] ?: ""
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
    
    // --- フォント設定関連 ---
    
    /**
     * フォント設定を保存
     * 
     * @param font フォント設定の文字列（AppFont.nameを渡す）
     */
    suspend fun saveFont(font: String) {
        context.dataStore.edit { preferences ->
            preferences[FONT_KEY] = font
        }
    }
    
    /**
     * フォント設定を取得（Flow）
     * 
     * データが未設定の場合は"DEFAULT"を返す
     */
    val fontFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[FONT_KEY] ?: "DEFAULT"
    }
    
    // --- 一括操作 ---
    
    /**
     * すべてのプロフィールデータを一度に保存
     * 
     * 複数の値を一度のedit操作で保存することで、パフォーマンスを向上
     * 個別に保存する代わりに、この関数を使うことでディスクI/O回数を削減
     * 
     * @param nickname ニックネーム
     * @param bio 自己紹介
     * @param genderIndex 性別インデックス
     * @param birthDateMillis 生年月日（エポックミリ秒）、nullの場合はキーを削除
     * @param hobbies 趣味リスト
     * @param twitterUrl Twitter/X URL
     * @param instagramUrl Instagram URL
     * @param facebookUrl Facebook URL
     * @param lineUrl LINE URL
     * @param phoneNumber 電話番号
     * @param email メールアドレス
     */
    suspend fun saveProfileData(
        nickname: String,
        bio: String,
        genderIndex: Int,
        birthDateMillis: Long?,
        hobbies: List<String>,
        twitterUrl: String = "",
        instagramUrl: String = "",
        facebookUrl: String = "",
        lineUrl: String = "",
        phoneNumber: String = "",
        email: String = ""
    ) {
        // 単一のedit操作で全データを更新
        // これによりDataStoreへの書き込みが1回で完了し、効率的
        context.dataStore.edit { preferences ->
            preferences[NICKNAME_KEY] = nickname
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
            
            // SNS URLを保存
            preferences[TWITTER_URL_KEY] = twitterUrl
            preferences[INSTAGRAM_URL_KEY] = instagramUrl
            preferences[FACEBOOK_URL_KEY] = facebookUrl
            preferences[LINE_URL_KEY] = lineUrl
            
            // 連絡先を保存
            preferences[PHONE_NUMBER_KEY] = phoneNumber
            preferences[EMAIL_KEY] = email
        }
    }
    
    // --- 画像URI関連 ---
    
    /**
     * プロフィール画像のURIを保存
     * 
     * @param uri 画像のURI文字列、nullの場合はキーを削除
     */
    suspend fun saveProfileImageUri(uri: String?) {
        context.dataStore.edit { preferences ->
            if (uri != null) {
                preferences[PROFILE_IMAGE_URI_KEY] = uri
            } else {
                preferences.remove(PROFILE_IMAGE_URI_KEY)
            }
        }
    }
    
    /**
     * プロフィール画像のURIを取得（Flow）
     */
    val profileImageUriFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PROFILE_IMAGE_URI_KEY]
    }
    
    /**
     * プロフィール画像の元画像URIを保存
     * 
     * @param uri 元画像のURI文字列、nullの場合はキーを削除
     */
    suspend fun saveProfileImageOriginalUri(uri: String?) {
        context.dataStore.edit { preferences ->
            if (uri != null) {
                preferences[PROFILE_IMAGE_ORIGINAL_URI_KEY] = uri
            } else {
                preferences.remove(PROFILE_IMAGE_ORIGINAL_URI_KEY)
            }
        }
    }
    
    /**
     * プロフィール画像の元画像URIを取得（Flow）
     */
    val profileImageOriginalUriFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PROFILE_IMAGE_ORIGINAL_URI_KEY]
    }
    
    /**
     * ヘッダー画像のURIを保存
     * 
     * @param uri 画像のURI文字列、nullの場合はキーを削除
     */
    suspend fun saveHeaderImageUri(uri: String?) {
        context.dataStore.edit { preferences ->
            if (uri != null) {
                preferences[HEADER_IMAGE_URI_KEY] = uri
            } else {
                preferences.remove(HEADER_IMAGE_URI_KEY)
            }
        }
    }
    
    /**
     * ヘッダー画像のURIを取得（Flow）
     */
    val headerImageUriFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[HEADER_IMAGE_URI_KEY]
    }
    
    /**
     * カードデザインを保存
     * 
     * @param cardDesign 名刺デザインの名前
     */
    suspend fun saveCardDesign(cardDesign: String) {
        context.dataStore.edit { preferences ->
            preferences[CARD_DESIGN_KEY] = cardDesign
        }
    }
    
    /**
     * カードデザインを取得（Flow）
     */
    val cardDesignFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CARD_DESIGN_KEY] ?: "CLASSIC"
    }
    
    // --- 名刺ホルダー関連 ---
    
    /**
     * 受け取った名刺を保存
     * 
     * @param cards 名刺データのリスト
     */
    suspend fun saveSavedCards(cards: List<com.example.androidbootcampiwatepref.domain.model.BusinessCardData>) {
        val json = kotlinx.serialization.json.Json.encodeToString(
            kotlinx.serialization.builtins.ListSerializer(
                com.example.androidbootcampiwatepref.domain.model.BusinessCardData.serializer()
            ),
            cards
        )
        context.dataStore.edit { preferences ->
            preferences[SAVED_CARDS_KEY] = json
        }
    }
    
    /**
     * 受け取った名刺のリストを取得（Flow）
     */
    val savedCardsFlow: Flow<List<com.example.androidbootcampiwatepref.domain.model.BusinessCardData>> = 
        context.dataStore.data.map { preferences ->
            val json = preferences[SAVED_CARDS_KEY] ?: "[]"
            try {
                kotlinx.serialization.json.Json.decodeFromString(
                    kotlinx.serialization.builtins.ListSerializer(
                        com.example.androidbootcampiwatepref.domain.model.BusinessCardData.serializer()
                    ),
                    json
                )
            } catch (e: Exception) {
                emptyList()
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
