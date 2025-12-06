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
    
    /**
     * 設定画面
     * 
     * @Serializableアノテーションにより、Navigation Composeが自動的にルートを処理
     */
    @Serializable
    data object Settings : ProfileRoutes
    
    /**
     * QRコード表示画面
     */
    @Serializable
    data object QRCodeDisplay : ProfileRoutes
    
    /**
     * QRコードスキャン画面
     */
    @Serializable
    data object QRCodeScanner : ProfileRoutes
    
    /**
     * 名刺ホルダー画面（受け取った名刺一覧）
     */
    @Serializable
    data object CardHolder : ProfileRoutes
    
    /**
     * 名刺詳細画面
     * 
     * @param cardIndex 名刺ホルダー内のインデックス
     */
    @Serializable
    data class CardDetail(val cardIndex: Int) : ProfileRoutes
}
