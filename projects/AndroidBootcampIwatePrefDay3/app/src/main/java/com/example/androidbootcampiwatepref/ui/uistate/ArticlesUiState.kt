package com.example.androidbootcampiwatepref.ui.uistate

import com.example.androidbootcampiwatepref.domain.domainobject.Article

data class ArticlesUiState(
    val articles: List<Article> = emptyList(),
)
