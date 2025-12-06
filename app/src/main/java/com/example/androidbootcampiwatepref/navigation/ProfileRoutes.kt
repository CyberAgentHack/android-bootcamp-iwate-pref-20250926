package com.example.androidbootcampiwatepref.navigation

import kotlinx.serialization.Serializable

/**
 * プロフィール画面のルート定義
 */
sealed interface ProfileRoutes {
    @Serializable
    data object View : ProfileRoutes

    @Serializable
    data object Edit : ProfileRoutes
}
