# Kotlinの基礎

Kotlinは2017年[^1]にAndroidに公式サポートされたプログラミング言語です。
それ以前はJavaでアプリ開発をしていましたが、現在ではKotlinで開発されるのが一般的です[^2]。

このブートキャンプはKotlinでの実装を想定しているので、ここでKotlinの基本的なコーディングを学びましょう。

なお、`MainActivity`ファイルにコードを書いて、実行しながら学べる内容になっています。

[^1]: [AndroidによるKotlinのサポート](https://developers-jp.googleblog.com/2017/06/android-announces-support-for-kotlin.html)
[^2]: [なぜKotlinが選ばれるのか - Android Developers](https://developer.android.com/kotlin/first?hl=ja)

## 関数の定義と呼び出し

Kotlinの関数を定義する時は`fun`を仕様します。  
続けて`fun <関数名>: <戻り値の型>`と続けて書きます。

下のサンプルコードにおいては、`greetUser`が関数名、`String`が戻り値の型になります。

```kotlin
fun greetUser(
    name: String,
    message: String = "Nice to meet you!",
): String {
    return "$name\n$message"
}
```

また関数には引数を取ることができます。  
引数とは関数内の処理の入力のことです。それにより出力結果(戻り値)を変えることができます。

サンプルコードでは`name`と`message`が引数になります。  
また`message`ではデフォルトの引数を与えることもできます。

定義した関数は`<関数名>(...)`で呼び出すことができます。

`Greeting`関数から呼び出す場合は次のようになります。

```diff
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
-       text = "Hello $name!",
+       text = greetUser(name),
        modifier = modifier
    )
}

fun greetUser(
    name: String,
    message: String = "Nice to meet you!",
): String {
    return "$name\n$message"
}
```

## 変数

プログラム内の値の入れ物を変数と呼びます。

Kotlinでは`val`または`var`というキーワードを使って定義します。
違いは変更できるか否かです。

```kotlin
val a: Int = 1  // 変更不可能（イミュータブル）
var b = 2       // 変更可能（ミュータブル）
b = 3           // OK: varは再代入可能
// a = 2        // エラー: valは再代入不可
```

### 型推論

Kotlinは静的型付け言語ですが、型推論により型名を省略できます。

```kotlin
val languageName = "Kotlin"     // 型推論でString型と判断
val count = 42                   // 型推論でInt型と判断
val pi = 3.14159                 // 型推論でDouble型と判断
```

では、先ほど追加した`greetUser`関数内で変数を利用してみましょう。

```diff
fun greetUser(
    name: String,
    message: String = "Nice to meet you!",
): String {
+   val date = "2024-01-01"  // val: 変更不可の変数
+   var counter = 0          // var: 変更可能な変数
+   counter++                // counterを1増やす
+   return "$name\n$message at $date (message #$counter)"
}
```

ビルドして、テキストに日付とカウンターが追加されていることを確認してみましょう。

## クラスとインスタンス

クラスとはオブジェクト(物体)を型として定義したものです。

以下は、車のクラスの例です。

```kotlin
/**
 * 車のクラス
 * @property model 車種
 * @property manufacturer メーカー名
 */
class Car(
    val model: String,
    val manufacturer: String,
) {
    /**
     * @property fuel 燃料[L]
     */
    private var fuel: Int = 100

    /**
     * 燃料を追加する。
     * @param fuel 追加する燃料[L]
     */
    fun addFuel(fuel: Int) {
        this.fuel += fuel
    }
}
```

クラスは金型のようなものであり、同じ型のものをいくつも作ることができます。  
そのようにクラスから作られたものをインスタンスと呼びます。

インスタンスはコンストラクタを利用することで作成できます。  
コンストラクタはインスタンスを作成(初期化)するための処理で、初期化に必要なデータを引数で渡すこともできます。

下記のコードは、`Car`のインスタンスを作成する例です。

```kotlin
val nBox = Car(model = "N-BOX", manufacturer = "HONDA")
val note = Car(model = "NOTE", manufacturer = "NISSAN")
```

また、クラスはプロパティとメソッドを定義することができます。

プロパティは変数と同じく`var`または`val`を使って定義します。  
メソッドは関数と同じく`fun`を使って定義します。

以下のコードでは、`.`を使ってプロパティへのアクセスとメソッドの呼び出しをしています。

```kotlin
val nBox = Car(model = "N-BOX", manufacturer = "HONDA")
println(nBox.model) // modelプロパティにアクセス
nBox.addFuel(10) // addFuelメソッドを呼び出し
```

## 条件分岐

### if-else文

Kotlinでは`if-else`を使って条件分岐を行います。

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val hour = 14  // 現在の時刻（14時）

    val greeting = if (hour < 12) {
        "おはよう"
    } else if (hour < 18) {
        "こんにちは"
    } else {
        "こんばんは"
    }

    Text(
        text = "$greeting $name!",
        modifier = modifier
    )
}
```

### when式

複数の条件分岐には`when`式が便利です。

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val dayOfWeek = 3  // 水曜日

    val dayName = when (dayOfWeek) {
        1 -> "月曜日"
        2 -> "火曜日"
        3 -> "水曜日"
        4 -> "木曜日"
        5 -> "金曜日"
        6, 7 -> "週末"  // 複数の条件をまとめる
        else -> "不明"
    }

    Text(
        text = "今日は$dayName、$name!",
        modifier = modifier
    )
}
```

## data class

`data class`はデータを保持するために使われるクラスです。

通常インスタンス同士が等価かどうかを比較(`==`)すると、「等価ではない(`false`)」と判断されます。

下記のコードでは`User`クラスが等価かどうかを出力しています。

```kotlin
class User(val name: String, val age: Int)

val user1 = User(name = "YOUR_NAME", age = 20)
val user2 = User(name = "YOUR_NAME", age = 20)

println(user1 == user2)  // false
```

`User`クラスを`data class`に変更すると出力結果が変わります。

```kotlin
data class User(val name: String, val age: Int)

val user1 = User(name = "YOUR_NAME", age = 20)
val user2 = User(name = "YOUR_NAME", age = 20)

println(user1 == user2)  // true
```

このように同じデータを持つ場合に`data class`を使うと、別インスタンスでも等価として判断してくれます。

> [!TIP]
> `data class`の場合でも同じインスタンスかを確認するためには`===`で比較してください。

## さらにKotlinを学ぶ

ここまで紹介したKotlinコードはブートキャンプをする上で最低限のものです。
さらにKotlinについて学びたい場合は、下記のWebページでの学習がおすすめです。

- [Kotlin プログラミング言語を学ぶ](https://developer.android.com/kotlin/learn?hl=ja#properties)