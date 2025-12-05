package com.example.androidbootcampiwatepref.viewmodel

import androidx.lifecycle.ViewModel
import com.example.androidbootcampiwatepref.ui.uistate.NewsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampiwatepref.domain.domainobject.News
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import com.example.androidbootcampiwatepref.data.datastore.AppDataStore
import kotlinx.coroutines.flow.combine

class NewsViewModel(
    private val dataStore: AppDataStore,
) : ViewModel() {
    /**
     * 変更可能なNewsUiStateを保持するStateFlow。
     * 初期値では、空のニュースリストを保持している。
     */
    private val mutableUiState =
        MutableStateFlow(NewsUiState(newsList = emptyList()))

    /**
     * UI側にUI状態を公開する。
     */
    val uiState = mutableUiState.asStateFlow()

    private val dummyDataSource = flow<List<News>> {
        val news1 = News(
            id = "news_1",
            title = "ニュースタイトル1",
            body = "ニュース内容1",
        )
        val news2 = News(
            id = "news_2",
            title = "ニュースタイトル2",
            body = "ニュース内容2",
        )

        // 最初は1件だけデータを流す
        emit(listOf(news1))

        // 5秒後に2件目のデータを追加で流す
        delay(5.seconds)
        emit(listOf(news1, news2))
    }

    init {
        viewModelScope.launch {
            // dummyDataSourceとdataStoreのニュースリストの並び順を組み合わせて購読する
            combine(
                dummyDataSource,
                dataStore.newsListOrder,
                ::Pair
            ).collect { (newsList, newsListOrder) ->
                // 新しいニュースリストを受け取ったら、UI状態を更新する
                val sortedNewsList = when (newsListOrder) {
                    // 並び順に応じてニュースリストをソートする
                    AppDataStore.NewsListOrder.ID_ASCENDING -> newsList.sortedBy { it.id }
                    AppDataStore.NewsListOrder.ID_DESCENDING -> newsList.sortedByDescending { it.id }
                }
                // 並び替えたニュースリストでUI状態を更新する
                mutableUiState.value = NewsUiState(newsList = sortedNewsList)
            }
        }
    }

//    init {
//        viewModelScope.launch {
//            dummyDataSource.collect { newsList ->
//                // 新しいニュースリストを受け取ったら、UI状態を更新する
//                mutableUiState.value = NewsUiState(newsList = newsList)
//            }
//        }
//    }



    fun toggleNewsListOrder() {
        viewModelScope.launch {
            // 並び替えを切り替える
            dataStore.toggleNewListOrder()
        }
    }
}