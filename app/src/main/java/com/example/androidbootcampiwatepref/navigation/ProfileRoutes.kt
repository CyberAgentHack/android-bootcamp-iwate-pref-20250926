package com.example.androidbootcampiwatepref.navigation

import kotlinx.serialization.Serializable

/**
 * プロフィール画面のナビゲーションルート定義
 * 
 * Type-Safe Navigation（型安全なナビゲーション）を実現するためのクラス
 * Kotlin Serializationを使用してルートを定義
 * 
 * sealed interfaceを使用することで、ルートの種類を限定し、コンパイル時に型チェックを行う
 * これにより、存在しないルートへの遷移をコンパイル時に検出できる
 */
sealed interface ProfileRoutes {
    /**
     * プロフィール閲覧画面
     * 
     * @Serializableアノテーションにより、Navigation Composeが自動的にルートを処理
     */
    @Serializable
    data object View : ProfileRoutes

    /**
     * プロフィール編集画面
     * 
     * @Serializableアノテーションにより、Navigation Composeが自動的にルートを処理
     */
    @Serializable
    data object Edit : ProfileRoutes
}
