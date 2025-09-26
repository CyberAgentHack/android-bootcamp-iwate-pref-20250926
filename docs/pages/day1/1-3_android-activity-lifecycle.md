# Activityとライフサイクル

![Android Activity](./images/android-activity.png)

## Activityとは

**Activity**は、Androidアプリの画面を提供するコンポーネントです。
アプリのUIを配置するための土台となり、ユーザーとアプリが対話するための窓口として機能します。

## Activityのライフサイクル

Activityには**ライフサイクル**と呼ばれる、作成から破棄までの一連の状態遷移があります。
システムはActivityの状態に応じて、特定のコールバックメソッドを呼び出します。

主要なライフサイクルメソッド：
- `onCreate()` - Activityが作成されるとき
- `onStart()` - Activityがユーザーに表示されるとき
- `onResume()` - Activityがユーザーと対話可能になるとき
- `onPause()` - Activityが一時停止するとき
- `onStop()` - Activityが表示されなくなるとき
- `onDestroy()` - Activityが破棄されるとき

## 詳しく学ぶ

Activityのライフサイクルの詳細な動作や、各メソッドの使い分けについては、Android公式ドキュメントを参照してください：

📖 [アクティビティのライフサイクル | Android Developers](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ja)

このドキュメントでは、ライフサイクルの図解や具体的な使用例が詳しく説明されています。