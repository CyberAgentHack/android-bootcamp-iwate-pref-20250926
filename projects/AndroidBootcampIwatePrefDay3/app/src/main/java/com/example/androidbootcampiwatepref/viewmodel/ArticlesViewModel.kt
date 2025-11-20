package com.example.androidbootcampiwatepref.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampiwatepref.data.api.ArticlesApiFactory
import com.example.androidbootcampiwatepref.domain.domainobject.Article
import com.example.androidbootcampiwatepref.ui.uistate.ArticlesUiState
import com.example.androidbootcampiwatepref.usecase.GetArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArticlesViewModel : ViewModel() {

    private val getArticles: GetArticlesUseCase by lazy {
        GetArticlesUseCase(
            articlesApi = ArticlesApiFactory.create()
        )
    }

    private val articles = MutableStateFlow(emptyList<Article>())

    val uiState: StateFlow<ArticlesUiState> = articles
        .map { articles -> ArticlesUiState(articles) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArticlesUiState(),
        )

    init {
        initScreen()
    }

    private fun initScreen() {
        viewModelScope.launch {
            getArticles().fold(
                onSuccess = { articles ->
                    this@ArticlesViewModel.articles.value = articles
                },
                onFailure = {
                    // ここでエラーハンドリングが可能
                }
            )
        }
    }
}