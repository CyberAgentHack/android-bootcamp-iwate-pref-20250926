package com.example.androidbootcampiwatepref.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampiwatepref.data.ProfileDataStore
import com.example.androidbootcampiwatepref.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * メイン画面のViewModel
 * 
 * DataStoreからのデータ読み込みと状態管理を担当
 */
class MainViewModel(
    private val profileDataStore: ProfileDataStore
) : ViewModel() {
    
    // DataStoreから各種データを購読
    val nickname = profileDataStore.nicknameFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "未設定"
    )
    
    val bio = profileDataStore.bioFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    val genderIndex = profileDataStore.genderIndexFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )
    
    val birthDateMillis = profileDataStore.birthDateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DEFAULT_BIRTH_DATE_MILLIS
    )
    
    val hobbies = profileDataStore.hobbiesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    // SNS URL関連のStateFlow（DataStoreから取得し、UI層で購読可能な状態に変換）
    // WhileSubscribed(5000): 購読者がいなくなってから5秒後にFlowを停止（リソース最適化）
    
    // Twitter/X URL
    val twitterUrl = profileDataStore.twitterUrlFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    // Instagram URL
    val instagramUrl = profileDataStore.instagramUrlFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    // Facebook URL
    val facebookUrl = profileDataStore.facebookUrlFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    // LINE URL
    val lineUrl = profileDataStore.lineUrlFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    // 電話番号
    val phoneNumber = profileDataStore.phoneNumberFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    // メールアドレス
    val email = profileDataStore.emailFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    // プロフィール画像URI（トリミング済み）
    val profileImageUri = profileDataStore.profileImageUriFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    // プロフィール画像の元画像URI（トリミング前の高解像度画像、拡大表示用）
    val profileImageOriginalUri = profileDataStore.profileImageOriginalUriFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    val headerImageUri = profileDataStore.headerImageUriFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
    val themeString = profileDataStore.themeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppTheme.SYSTEM.name
    )
    
    val fontString = profileDataStore.fontFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppFont.DEFAULT.name
    )
    
    val cardDesignString = profileDataStore.cardDesignFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CardDesign.CLASSIC.name
    )
    
    val savedCards = profileDataStore.savedCardsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    // 計算プロパティ: ProfileData
    // 複数のFlowを結合して単一のProfileDataオブジェクトを生成
    // combine演算子: いずれかのFlowが更新されると新しいProfileDataが生成される
    // SNS URL（twitterUrl, instagramUrl, facebookUrl, lineUrl）、連絡先（phoneNumber, email）も含めて管理
    val profileData: StateFlow<ProfileData> = combine(
        nickname,
        bio,
        genderIndex,
        birthDateMillis,
        hobbies,
        twitterUrl,        // Twitter/X URL
        instagramUrl,     // Instagram URL
        facebookUrl,      // Facebook URL
        lineUrl,          // LINE URL
        phoneNumber,      // 電話番号
        email             // メールアドレス
    ) { flows ->
        ProfileData(
            nickname = flows[0] as String,
            bio = flows[1] as String,
            genderIndex = flows[2] as Int,
            birthDateMillis = flows[3] as Long?,
            hobbies = flows[4] as List<String>,
            twitterUrl = flows[5] as String,      // SNS URLをProfileDataに含める
            instagramUrl = flows[6] as String,
            facebookUrl = flows[7] as String,
            lineUrl = flows[8] as String,
            phoneNumber = flows[9] as String,     // 電話番号を含める
            email = flows[10] as String           // メールアドレスを含める
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileData()
    )
    
    // 計算プロパティ: AppTheme
    val currentTheme: StateFlow<AppTheme> = themeString.map { themeStr ->
        try {
            AppTheme.valueOf(themeStr)
        } catch (e: IllegalArgumentException) {
            AppTheme.SYSTEM
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppTheme.SYSTEM
    )
    
    // 計算プロパティ: AppFont
    val currentFont: StateFlow<AppFont> = fontString.map { fontStr ->
        try {
            AppFont.valueOf(fontStr)
        } catch (e: IllegalArgumentException) {
            AppFont.DEFAULT
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppFont.DEFAULT
    )
    
    // 計算プロパティ: CardDesign
    val currentCardDesign: StateFlow<CardDesign> = cardDesignString.map { designStr ->
        try {
            CardDesign.valueOf(designStr)
        } catch (e: IllegalArgumentException) {
            CardDesign.CLASSIC
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CardDesign.CLASSIC
    )
    
    // スプラッシュ画面の表示状態
    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded.asStateFlow()
    
    init {
        // データ読み込み完了を監視（2秒遅延後にスプラッシュ終了）
        viewModelScope.launch {
            combine(
                nickname,
                cardDesignString
            ) { _, _ -> Unit }
                .collect {
                    kotlinx.coroutines.delay(2000)
                    _isDataLoaded.value = true
                }
        }
    }
}
