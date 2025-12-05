package com.example.androidbootcampiwatepref.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(
    private val context: Context
) {
    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
        val KEY_NEWS_LIST_ORDER = intPreferencesKey("key_news_list_order")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    /**
     * 永続化されたニュースリストの並び順を取得するFlow。
     */
    val newsListOrder: Flow<NewsListOrder>
        get() = context.dataStore.data.map { preferences ->
            NewsListOrder.fromValueOrDefault(
                value = preferences[KEY_NEWS_LIST_ORDER]
            )
        }

    /**
     * ニュースリストの並び順を切り替える。
     */
    suspend fun toggleNewListOrder() {
        context.dataStore.edit { preferences ->
            // 現在の並び順を取得
            val currentOrder = NewsListOrder.fromValueOrDefault(
                value = preferences[KEY_NEWS_LIST_ORDER]
            )
            // 並び順を切り替える
            val newOrder = when (currentOrder) {
                NewsListOrder.ID_ASCENDING -> NewsListOrder.ID_DESCENDING
                NewsListOrder.ID_DESCENDING -> NewsListOrder.ID_ASCENDING
            }
            // 新しい並び順を保存
            preferences[KEY_NEWS_LIST_ORDER] = newOrder.value
        }
    }

    enum class NewsListOrder(val value: Int) {
        ID_ASCENDING(0),
        ID_DESCENDING(1),
        ;

        companion object {
            val DEFAULT = ID_ASCENDING

            fun fromValueOrDefault(value: Int?): NewsListOrder {
                return NewsListOrder.entries
                    .firstOrNull { it.value == value }
                    ?: DEFAULT
            }
        }
    }
}