# Android Bootcamp Iwate Pref - プロフィールアプリ

岩手県向けAndroidブートキャンプで作成したプロフィール管理アプリケーションです。

## 📱 機能

- **プロフィール閲覧** - ニックネーム、ID、自己紹介、性別、生年月日、趣味を表示
- **プロフィール編集** - 各項目の編集と保存
- **データ永続化** - DataStoreによるローカルデータ保存
- **ダークモード対応** - ライト/ダーク/システム設定に対応
- **画面遷移** - Navigation Composeによる滑らかな画面遷移

## 🏗️ アーキテクチャ

このプロジェクトは、保守性と可読性を重視した構造になっています。

```
app/src/main/java/com/example/androidbootcampiwatepref/
├── MainActivity.kt                    # メインアクティビティ
├── data/
│   └── ProfileDataStore.kt           # データ永続化層
├── domain/
│   └── model/
│       ├── AppTheme.kt               # テーマEnum
│       └── ProfileData.kt            # プロフィールデータモデル
├── navigation/
│   └── ProfileRoutes.kt              # ナビゲーションルート定義
├── ui/
│   ├── component/
│   │   ├── ProfileHeader.kt         # ヘッダーコンポーネント
│   │   └── ProfileInfoRow.kt        # 情報行コンポーネント
│   ├── screen/
│   │   ├── ProfileViewScreen.kt     # 閲覧画面
│   │   └── ProfileEditScreen.kt     # 編集画面
│   └── theme/
│       └── AndroidBootcampIwatePrefTheme.kt
```

## 🛠️ 技術スタック

- **言語**: Kotlin
- **UI**: Jetpack Compose
- **ナビゲーション**: Navigation Compose with Type-Safe Routes
- **データ永続化**: DataStore (Preferences)
- **非同期処理**: Kotlin Coroutines & Flow
- **ビルドツール**: Gradle (Kotlin DSL)

### 主要な依存関係

```kotlin
// Jetpack Compose
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.9")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Serialization (Type-Safe Navigation)
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
```

## 🚀 セットアップ

### 必要要件

- Android Studio Hedgehog (2023.1.1) 以降
- JDK 8 以上
- Android SDK API 24 (Android 7.0) 以上
- Gradle 8.13

### インストール手順

1. **リポジトリのクローン**
   ```bash
   git clone https://github.com/urushi-saku/Boot-Camp2025.git
   cd Boot-Camp2025/projects/AndroidBootcampIwatePref
   ```

2. **Android Studioで開く**
   - Android Studioを起動
   - "Open an Existing Project" を選択
   - プロジェクトフォルダを選択

3. **依存関係の同期**
   - Android Studioが自動的にGradle同期を開始
   - または `File > Sync Project with Gradle Files`

4. **アプリの実行**
   - エミュレーターまたは実機を接続
   - Run ボタン (▶️) をクリック

### コマンドラインでのビルド

```bash
# デバッグビルド
./gradlew assembleDebug

# リリースビルド
./gradlew assembleRelease

# テストの実行
./gradlew test
```

## 📝 使い方

### プロフィール閲覧画面

- アプリ起動時に表示される画面
- 右上の編集アイコンで編集画面へ遷移
- 右上の月/太陽アイコンでテーマ切り替え

### プロフィール編集画面

1. **基本情報の入力**
   - ニックネーム
   - ID (@付き)
   - 自己紹介

2. **詳細情報の選択**
   - 性別 (男性/女性/回答しない)
   - 生年月日 (DatePickerから選択)

3. **趣味・興味の追加**
   - テキストフィールドに入力
   - 「+」ボタンで追加
   - タグをタップして削除

4. **保存**
   - 「保存」ボタンで保存して閲覧画面へ戻る
   - 左上の戻るボタンでキャンセル

## 🎨 DataStoreの活用

このアプリでは、AndroidのDataStore (Preferences)を使用してデータを永続化しています。

### DataStoreでできること

- ✅ **キー・バリューペアの保存** - シンプルなデータの保存
- ✅ **非同期処理** - UIをブロックしない安全な読み書き
- ✅ **型安全** - コンパイル時の型チェック
- ✅ **トランザクション** - データの整合性を保証

### 実装例

```kotlin
// データの保存
suspend fun saveNickname(nickname: String) {
    context.dataStore.edit { preferences ->
        preferences[NICKNAME_KEY] = nickname
    }
}

// データの読み込み
val nicknameFlow: Flow<String> = context.dataStore.data.map { preferences ->
    preferences[NICKNAME_KEY] ?: ""
}
```

## 🏛️ 設計パターン

### レイヤー分離

- **UI層** (`ui/`): Jetpack Composeによる画面構築
- **ドメイン層** (`domain/`): ビジネスロジックとモデル
- **データ層** (`data/`): データの永続化と取得
- **ナビゲーション層** (`navigation/`): 画面遷移の管理

### コンポーネントの再利用

- `ProfileHeader`: ヘッダー画像とアイコンの表示
- `ProfileInfoRow`: ラベルと値のペア表示

再利用可能なコンポーネントを作成することで、コードの重複を削減し、メンテナンス性を向上させています。

## 🧪 テスト

```bash
# ユニットテストの実行
./gradlew test

# UIテストの実行
./gradlew connectedAndroidTest
```

## 📦 ビルドバリアント

- **debug**: 開発用ビルド
- **release**: リリース用ビルド（ProGuard有効）

## 🔧 開発のヒント

### import文の整理

このプロジェクトでは、可読性向上のためワイルドカード(`*`)を使用しています:

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
```

### コードフォーマット

Android Studioの自動フォーマット機能を使用:
- `Ctrl + Alt + L` (Windows/Linux)
- `Cmd + Option + L` (Mac)

## 📄 ライセンス

このプロジェクトは教育目的で作成されました。

## 👥 作成者

岩手県Androidブートキャンプ 2025

## 🙏 謝辞

Jetpack Composeと最新のAndroid開発技術を学ぶ機会を提供してくださった講師の方々に感謝します。

---

**Happy Coding! 🚀**
