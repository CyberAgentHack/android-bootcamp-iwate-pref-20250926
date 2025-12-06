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
- **画像付き名刺共有** - Android Sharesheet経由でプロフィール画像も含めて送信
- **名刺受信機能** - 共有されたJSONファイルを自動的に名刺ホルダーに追加
- **名刺ホルダー** - 受け取った名刺を一覧で管理
- **名刺詳細表示** - 受け取った名刺をフリップアニメーションで閲覧
- **オフライン交換** - インターネット接続不要で名刺交換可能（QRコード）

### ✏️ プロフィール編集
- **プロフィール編集** - ニックネーム、自己紹介、性別、生年月日、趣味の編集
- **カスタム画像** - 端末から画像を選択してプロフィールアイコンに設定
- **画像トリミング** - UCropライブラリによる高度な画像切り抜き機能
- **保存ボタン改善** - TopAppBarに配置され、常に表示

### ⚙️ システム機能
- **スプラッシュ画面** - 名刺交換をイメージした2枚カードのアニメーション
- **データ永続化** - DataStoreによるローカルデータ保存（プロフィール、受け取った名刺、選択デザイン）
- **MVVM設計** - ViewModelによる状態管理とビジネスロジック分離
- **ダークモード対応** - ライト/ダーク/システム設定に対応
- **画面遷移** - Navigation Composeによる滑らかな画面遷移（タブ切替時の方向制御）

## 🏗️ アーキテクチャ

このプロジェクトは、保守性と可読性を重視したMVVM（Model-View-ViewModel）アーキテクチャを採用しています。

```
app/src/main/java/com/example/androidbootcampiwatepref/
├── MainActivity.kt                    # メインアクティビティ（エントリーポイント）
├── data/
│   └── ProfileDataStore.kt           # データ永続化層（プロフィール、名刺ホルダー）
├── domain/
│   └── model/
│       ├── AppTheme.kt               # テーマEnum
│       ├── AppFont.kt                # フォントEnum（FontFamilyプロパティ付き）
│       ├── CardDesign.kt             # カードデザインEnum（6種類）
│       ├── ProfileData.kt            # プロフィールデータモデル
│       └── BusinessCardData.kt       # 名刺データモデル（QRコード用）
├── navigation/
│   └── ProfileRoutes.kt              # ナビゲーションルート定義
├── ui/
│   ├── component/
│   │   ├── ProfileHeader.kt         # プロフィールヘッダーコンポーネント（アイコン+背景画像）
│   │   ├── ProfileInfoRow.kt        # 情報行コンポーネント
│   │   └── ImageViewerDialog.kt     # 画像拡大表示ダイアログ
│   ├── navigation/
│   │   └── AppNavigationGraph.kt    # ナビゲーショングラフ（画面遷移・アニメーション）
│   ├── screen/
│   │   ├── SplashScreen.kt          # スプラッシュ画面（カードアニメーション）
│   │   ├── ProfileViewScreen.kt     # 閲覧画面（名刺風デザイン）
│   │   ├── ProfileEditScreen.kt     # 編集画面（スライド式）
│   │   ├── SettingsScreen.kt        # 設定画面（テーマ、フォント、カードデザイン）
│   │   ├── QRCodeDisplayScreen.kt   # QRコード表示画面
│   │   ├── QRCodeScannerScreen.kt   # QRコードスキャン画面
│   │   ├── CardHolderScreen.kt      # 名刺ホルダー画面
│   │   └── CardDetailScreen.kt      # 名刺詳細画面
│   ├── viewmodel/
│   │   └── MainViewModel.kt         # メインViewModel（状態管理・ビジネスロジック）
│   └── theme/
│       └── AndroidBootcampIwatePrefTheme.kt
├── util/
│   ├── QRCodeGenerator.kt            # QRコード生成ユーティリティ
│   └── BusinessCardSharer.kt         # 名刺共有ユーティリティ（Android Sharesheet）
```

### アーキテクチャの特徴

- **MVVM採用**: ViewModelによる状態管理とビジネスロジックの分離
- **レイヤー分離**: UI、ViewModel、Data層の明確な責務分離
- **リアクティブプログラミング**: StateFlowによる状態監視とUI自動更新
- **スプラッシュ画面**: DataStore読み込み中の遅延を隠蔽する名刺風アニメーション

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
- 右上のQRコードアイコンで自分のQRコード表示画面へ遷移

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

### QRコード表示画面

- 自分の名刺情報をQRコードとして表示
- **QRコードの表示** - テキストデータ（ニックネーム、自己紹介、趣味）をQRコード化
- **画像付き共有ボタン** - Android Sharesheet経由でプロフィール画像も含めて送信
  - 送信内容: JSONファイル（名刺データ） + 画像ファイル（プロフィール画像）
  - Nearby Share、LINE、Gmail等、任意の共有対応アプリで送信可能
- **説明文** - 「Android共有機能を使うと、画像も一緒に送信できます」

### QRコードスキャン画面

- カメラでQRコードを読み取って名刺を受信
- **リアルタイムスキャン** - CameraX + MLKit Barcode Scanningによる高速読み取り
- **自動権限管理** - カメラ権限の自動チェックと設定画面への誘導
- **受信後の処理** - 読み取った名刺データを自動的に名刺ホルダーに追加

### 名刺ホルダー画面

- 受け取った名刺を一覧表示
- **カードデザイン** - 送信者が選択したカードデザインで表示
- **プロフィールアイコン** - デフォルトアイコン表示（QRコード経由では画像なし）
- **タップして詳細** - 名刺カードをタップして詳細画面へ遷移
- **削除機能** - 左上の削除ボタンで名刺を削除

### 名刺詳細画面

- 受け取った名刺の詳細をフリップアニメーションで表示
- **表面** - ニックネーム、自己紹介、プロフィールアイコン
- **裏面** - 性別、生年月日、趣味・興味
- **削除ボタン** - 右上のゴミ箱アイコンで削除

### 名刺の受信（Android Sharesheet）

1. **他のアプリから名刺を受信**
   - Nearby Share、LINE、Gmail等で名刺ファイル（JSON）を受信
   - 「AndroidBootcampIwatePrefで開く」を選択

2. **自動インポート**
   - アプリが自動的に起動
   - 名刺データを解析して名刺ホルダーに追加
   - 「名刺を受信しました: ○○」とToastで通知

3. **確認**
   - 名刺ホルダータブで受信した名刺を確認

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

## 🎨 DataStore + ViewModel の活用

### DataStoreによる永続化

このアプリでは、AndroidのDataStore (Preferences)を使用してデータを永続化しています。

**DataStoreでできること:**
- ✅ **キー・バリューペアの保存** - シンプルなデータの保存
- ✅ **非同期処理** - UIをブロックしない安全な読み書き
- ✅ **型安全** - コンパイル時の型チェック
- ✅ **トランザクション** - データの整合性を保証

### ViewModelによる状態管理

`MainViewModel`がDataStoreからのデータ読み込みと状態管理を担当:

```kotlin
class MainViewModel(private val profileDataStore: ProfileDataStore) : ViewModel() {
    // DataStoreから各種データを購読
    val nickname = profileDataStore.nicknameFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "未設定"
    )
    
    // 計算プロパティによる状態の派生
    val profileData: StateFlow<ProfileData> = combine(
        nickname, bio, genderIndex, birthDateMillis, hobbies
    ) { ... }.stateIn(...)
}
```

**利点:**
- UIから状態管理ロジックを分離
- StateFlowによる自動的なUI更新
- ライフサイクル対応のデータ購読
- テスタビリティの向上

## 🏛️ 設計パターン

### MVVMアーキテクチャ

このアプリは **MVVM (Model-View-ViewModel)** パターンを採用しています:

- **View** (`ui/screen/`): Jetpack Composeによる宣言的UI
- **ViewModel** (`ui/viewmodel/`): 状態管理とビジネスロジック
  - `MainViewModel`: DataStoreからのデータ読み込み、StateFlowによるリアクティブな状態管理
- **Model** (`domain/model/`, `data/`): データモデルと永続化層

### レイヤー分離

- **UI層** (`ui/`): Jetpack Composeによる画面構築
- **ViewModel層** (`ui/viewmodel/`): 状態管理とビジネスロジック
- **ドメイン層** (`domain/`): ビジネスルールとモデル
- **データ層** (`data/`): データの永続化と取得
- **ナビゲーション層** (`ui/navigation/`): 画面遷移の管理とアニメーション設定

### コンポーネントの再利用

- `ProfileHeader`: プロフィールアイコンとヘッダー背景画像の表示（編集モード対応）
- `ProfileInfoRow`: ラベルと値のペア表示（性別、生年月日、趣味など）
- `BusinessCardFront`/`BusinessCardBack`: 名刺の表面・裏面コンポーネント（フリップアニメーション対応）
- `EditCardFront`/`EditCardBack`: 編集用カードコンポーネント（スワイプ対応）
- `ImageViewerDialog`: 画像の拡大表示ダイアログ

再利用可能なコンポーネントを作成することで、コードの重複を削減し、メンテナンス性を向上させています。

### 名刺共有システム

名刺の共有には2つの方式を組み合わせたハイブリッドアプローチを採用：

#### 1. QRコード方式（テキストのみ）
- **用途**: 対面での素早い名刺交換
- **データ**: ニックネーム、自己紹介、趣味（テキスト情報のみ）
- **利点**: 
  - インターネット接続不要
  - 即座にスキャン・登録可能
  - デバイス間の互換性が高い
- **制限**: プロフィール画像は含まれない

#### 2. Android Sharesheet方式（画像付き）
- **用途**: リモート交換、完全な名刺情報の共有
- **データ**: JSONファイル（全データ） + 画像ファイル（プロフィール画像）
- **利点**:
  - プロフィール画像も共有可能
  - Nearby Share、LINE、Gmail等、既存の共有インフラを活用
  - 複数ファイルの一括送信
- **実装**: 
  - `BusinessCardSharer.kt`: FileProviderを使用した安全なファイル共有
  - `MainActivity.kt`: Intent FilterによるJSONファイルの自動受信
  - DataStore: 受信した名刺の永続化

この設計により、状況に応じた最適な名刺交換方法を選択できます。

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
