# ポートフォリオ
jakartaEE 実装集

## Cryptor.java
#### 暗号化／複合化ユーティリティ
Java 標準の javax.crypto パッケージを用いた暗号化／複合化の実装例

:open_file_folder: コード：[Cryptor.java](demo/src/main/java/com/example/Cryptor.java)  
:open_file_folder: 使用例：[CryptorTest.java](demo/src/test/java/com/example/CryptorTest.java)

## Properties.java
#### プロパティ取得ユーティリティ
型を指定したプロパティ取得が可能です。型パラメータと関数インターフェースを用いた実装となっています。

:open_file_folder: コード：[Properties.java](demo/src/main/java/com/example/Properties.java)  
:open_file_folder: 使用例：[PropertiesTest.java](demo/src/test/java/com/example/PropertiesTest.java)

## ExtractingJsonSerializer.java
Javaオブジェクトから指定プロパティのみを抽出してJSON化するカスタムシリアライザ。  
APIのレスポンスを、クライアントからの要求に応じて、返却するプロパティを制限したい場合に活用できます。

:open_file_folder: コード：[ExtractingJsonSerializer.java](demo/src/main/java/com/example/ExtractingJsonSerializer.java)  
:open_file_folder: 使用例：[ExtractingJsonSerializerTest.java](demo/src/test/java/com/example/ExtractingJsonSerializerTest.java)




依存ライブラリをなるべく使用しない構成
