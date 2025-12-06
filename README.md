# Android Bootcamp Iwate Pref - プロフィールアプリ

## 📱 機能

### 🎴 名刺機能
- **名刺風デザイン** - プロフィールを名刺風のカードで表示（フリップアニメーション）
- **6種類のカードデザイン** - Classic, Ocean, Sunset, Forest, Purple, Night から選択可能
- **テキスト色の自動調整** - 各デザインに最適化された読みやすい文字色
- **スライド編集** - カードをスワイプして表面・裏面を編集

### 📲 QRコード名刺交換
- **QRコード生成** - 自分の名刺をQRコードに変換して表示
- **QRコードスキャン** - カメラでQRコードを読み取って名刺を受信
- **名刺ホルダー** - 受け取った名刺を一覧で管理
- **名刺詳細表示** - 受け取った名刺をフリップアニメーションで閲覧
- **オフライン交換** - インターネット接続不要で名刺交換可能

### ✏️ プロフィール編集
- **プロフィール編集** - ニックネーム、自己紹介、性別、生年月日、趣味の編集
- **カスタム画像** - 端末から画像を選択してプロフィールアイコンに設定
- **画像トリミング** - UCropライブラリによる高度な画像切り抜き機能

### ⚙️ システム機能
- **データ永続化** - DataStoreによるローカルデータ保存（プロフィール、受け取った名刺、選択デザイン）
- **ダークモード対応** - ライト/ダーク/システム設定に対応
- **画面遷移** - Navigation Composeによる滑らかな画面遷移

## 🏗️ アーキテクチャ

このプロジェクトは、保守性と可読性を重視した構造になっています。

```
app/src/main/java/com/example/androidbootcampiwatepref/
├── MainActivity.kt                    # メインアクティビティ
├── data/
│   └── ProfileDataStore.kt           # データ永続化層（プロフィール、名刺ホルダー）
├── domain/
│   └── model/
│       ├── AppTheme.kt               # テーマEnum
│       ├── AppFont.kt                # フォントEnum
│       ├── CardDesign.kt             # カードデザインEnum（6種類）
│       ├── ProfileData.kt            # プロフィールデータモデル
│       └── BusinessCardData.kt       # 名刺データモデル（QRコード用）
├── navigation/
│   └── ProfileRoutes.kt              # ナビゲーションルート定義
├── ui/
│   ├── component/
│   │   ├── ProfileHeader.kt         # ヘッダーコンポーネント
│   │   ├── ProfileInfoRow.kt        # 情報行コンポーネント
│   │   └── ImageViewerDialog.kt     # 画像拡大表示ダイアログ
│   ├── screen/
│   │   ├── ProfileViewScreen.kt     # 閲覧画面（名刺風デザイン）
│   │   ├── ProfileEditScreen.kt     # 編集画面（スライド式）
│   │   ├── SettingsScreen.kt        # 設定画面（テーマ、フォント、カードデザイン）
│   │   ├── QRCodeDisplayScreen.kt   # QRコード表示画面
│   │   ├── QRCodeScannerScreen.kt   # QRコードスキャン画面
│   │   ├── CardHolderScreen.kt      # 名刺ホルダー画面
│   │   └── CardDetailScreen.kt      # 名刺詳細画面
│   └── theme/
│       └── AndroidBootcampIwatePrefTheme.kt
├── util/
│   └── QRCodeGenerator.kt            # QRコード生成ユーティリティ
```

## 🛠️ 技術スタック

- **言語**: Kotlin
- **UI**: Jetpack Compose
- **ナビゲーション**: Navigation Compose with Type-Safe Routes
- **データ永続化**: DataStore (Preferences)
- **非同期処理**: Kotlin Coroutines & Flow
- **画像処理**: Coil (画像読み込み), UCrop (トリミング)
- **QRコード**: ZXing (生成), MLKit Barcode Scanning (読み取り)
- **カメラ**: CameraX (プレビュー、画像解析)
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

// 画像処理
implementation("io.coil-kt:coil-compose:2.5.0")  // 画像読み込み
implementation("com.github.yalantis:ucrop:2.2.8")  // 画像トリミング

// QRコード
implementation("com.google.zxing:core:3.5.3")  // QRコード生成
implementation("com.google.mlkit:barcode-scanning:17.3.0")  // QRコード読み取り

// カメラ
implementation("androidx.camera:camera-camera2:1.3.4")  // CameraX
implementation("androidx.camera:camera-lifecycle:1.3.4")
implementation("androidx.camera:camera-view:1.3.4")

// UI拡張
implementation("androidx.compose.foundation:foundation:1.7.6")  // HorizontalPager
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

### プロフィール閲覧画面（名刺風デザイン）

- アプリ起動時に表示される画面
- **名刺をタップして反転** - 表面と裏面をフリップアニメーションで切り替え
- 右上の編集アイコンで編集画面へ遷移
- 右上の歯車アイコンで設定画面へ遷移

### プロフィール編集画面（スライド式）

1. **カードのスワイプ**
   - 左右にスワイプして表面・裏面を切り替え
   - ページインジケーターで現在の面を表示

2. **表面の編集**
   - プロフィール画像（タップして画像選択・トリミング）
   - ニックネーム
   - 自己紹介

3. **裏面の編集**
   - ヘッダー画像（タップして画像選択・トリミング）
   - 性別 (男性/女性/回答しない)
   - 生年月日 (DatePickerから選択)
   - 趣味・興味（「+」ボタンで追加、タグをタップして削除）

4. **保存**
   - 「保存」ボタンで保存して閲覧画面へ戻る
   - 左上の戻るボタンでキャンセル

### 設定画面

- **テーマ選択** - ライト/ダーク/システム設定
- **フォントサイズ** - 小/中/大
- **カードデザイン選択** - 6種類のデザインから選択
  - **Classic** - グレー/ホワイトのグラデーション
  - **Ocean** - ブルー系のグラデーション
  - **Sunset** - オレンジ/レッド系のグラデーション
  - **Forest** - グリーン系のグラデーション
  - **Purple** - パープル系のグラデーション
  - **Night** - ダークグレーのグラデーション

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
- `BusinessCardFront`/`BusinessCardBack`: 名刺の表面・裏面コンポーネント
- `EditCardFront`/`EditCardBack`: 編集用カードコンポーネント

再利用可能なコンポーネントを作成することで、コードの重複を削減し、メンテナンス性を向上させています。

### カードデザインシステム

各カードデザインは `CardDesign` Enumで定義され、以下の要素を持ちます：

- **グラデーション背景** - 表面・裏面それぞれに最適化された `Brush.verticalGradient`
- **テキストカラー** - 各デザインの背景色に対して最適なコントラストを持つ文字色
- **一貫性** - 閲覧画面と編集画面で同じデザインシステムを使用

```kotlin
enum class CardDesign(
    val displayName: String,
    val frontBrush: Brush,
    val backBrush: Brush,
    val frontTextColor: Color,
    val backTextColor: Color
)
```

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

---

**Happy Coding! 🚀**
