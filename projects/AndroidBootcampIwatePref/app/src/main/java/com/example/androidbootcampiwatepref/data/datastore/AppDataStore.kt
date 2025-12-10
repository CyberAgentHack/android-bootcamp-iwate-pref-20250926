package com.example.androidbootcampiwatepref.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppDataStore(private val context: Context) {

    // 動画IDごとのいいね数を取得
    fun getLikeCount(videoId: Int): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[intPreferencesKey("like_count_$videoId")] ?: 0
        }

    // 動画IDごとのいいね数を保存
    suspend fun saveLikeCount(videoId: Int, count: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey("like_count_$videoId")] = count
        }
    }

    // 動画IDごとのいいね数をインクリメント
    suspend fun incrementLikeCount(videoId: Int) {
        context.dataStore.edit { preferences ->
            val key = intPreferencesKey("like_count_$videoId")
            val currentCount = preferences[key] ?: 0
            preferences[key] = currentCount + 1
        }
    }
}
