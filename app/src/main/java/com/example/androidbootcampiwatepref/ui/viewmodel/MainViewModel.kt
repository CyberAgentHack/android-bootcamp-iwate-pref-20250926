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
    
    val profileImageUri = profileDataStore.profileImageUriFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    
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
    val profileData: StateFlow<ProfileData> = combine(
        nickname,
        bio,
        genderIndex,
        birthDateMillis,
        hobbies
    ) { nickname, bio, genderIndex, birthDateMillis, hobbies ->
        ProfileData(
            nickname = nickname,
            bio = bio,
            genderIndex = genderIndex,
            birthDateMillis = birthDateMillis,
            hobbies = hobbies
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
