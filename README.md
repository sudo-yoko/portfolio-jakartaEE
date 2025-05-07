# ポートフォリオ
jakartaEE 実装集

## JAX-RS を使った REST API の実装例

:open_file_folder: コード：[project/](project)  

開発時には Embedded GlassFish（組み込み型 GlassFish）を使用してアプリケーションを起動できる構成にしています。通常の GlassFish を使うよりも起動が速いため、開発効率がアップします。

[起動用ランチャー](project/src/test/java/com/example/development/Launcher.java)   を使用して起動します。データベースは組み込みDerbyを使用する構成になっています。  

また、起動用ランチャーでは、バックエンドAPIのモックを一緒に起動します。例えば、マイクロサービスのように、APIから別のAPIを呼ぶ構成になっている場合、バックエンドのAPIをモック化して開発を進めることができます。

ドメイン駆動設計(DDD)を採用しています。CRUD操作のみで業務ロジックの無い、シンプルで基本的な構成になっています。

## Properties.java
#### プロパティ取得ユーティリティ
型を指定したプロパティ取得が可能です。型パラメータと関数インターフェースを用いた実装になっています。

:open_file_folder: コード：[Properties.java](demo/src/main/java/com/example/Properties.java)  
:open_file_folder: 使用例：[PropertiesTest.java](demo/src/test/java/com/example/PropertiesTest.java)

## ExtractingJsonSerializer.java
Javaオブジェクトから指定プロパティのみを抽出してJSON化するカスタムシリアライザ。  
APIのレスポンスを、クライアントからの要求に応じて、返却するプロパティを制限したい場合に活用できます。

同じことができる既存ライブラリもありますが、依存ライブラリを増やしたくない場合、このように JakartaEE と Java の標準機能だけで作成することもできます。

:open_file_folder: コード：[ExtractingJsonSerializer.java](demo/src/main/java/com/example/ExtractingJsonSerializer.java)  
:open_file_folder: 使用例：[ExtractingJsonSerializerTest.java](demo/src/test/java/com/example/ExtractingJsonSerializerTest.java)


## Cryptor.java
#### 暗号化／複合化ユーティリティ
Java 標準の javax.crypto パッケージを用いた暗号化／複合化の実装例

:open_file_folder: コード：[Cryptor.java](demo/src/main/java/com/example/Cryptor.java)  
:open_file_folder: 使用例：[CryptorTest.java](demo/src/test/java/com/example/CryptorTest.java)

