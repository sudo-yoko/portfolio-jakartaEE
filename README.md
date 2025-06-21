# ポートフォリオ
Java 実装集

## JakartaEE: JAX-RS による REST API アプリケーションの実装例

:open_file_folder: コード：[jakartaee/](jakartaee)  

開発時には Embedded GlassFish（組み込み型 GlassFish）を使用してアプリケーションを起動できる構成にしています。通常の GlassFish を使うよりも起動が速いため、開発効率がアップします。

[起動用ランチャー](jakartaee/src/test/java/com/example/development/Launcher.java)   を使用して起動します。データベースは組み込みDerbyを使用する構成になっています。  

起動用ランチャーでは、バックエンドAPIのモックを一緒に起動します。例えば、マイクロサービスのように、APIから別のAPIを呼ぶ構成になっている場合、バックエンドのAPIをモック化して開発を進めることができます。

その他の特徴として、
* ドメイン駆動設計(DDD)を採用しています。CRUD操作のみで業務ロジックの無い、シンプルで基本的な構成になっています。
* CDIとEJBを併用しています。データベース操作を行う層にはEJBを使用するようにし、トランザクション制御はEJBのデフォルト動作を利用します。

## JakartaEE: JAX-RS アプリミドル基盤
#### クライアント認証
JAX-RSアプリケーションを利用するクライアントを認証します。リクエストフィルタを用いて認証および認可を行います。  

認証情報の定義は簡易実装になっています。将来的にJSONファイルから読みんだり、外部のキャッシュサービスを利用する実装に差し替えられるようインターフェースを用いた設計にしています。

:open_file_folder: コード：[AuthenticationFilter.java](jakartaee/src/main/java/com/example/application/AuthenticationFilter.java)、[AuthorizationFilter.java](jakartaee/src/main/java/com/example/application/AuthorizationFilter.java)、[/infrastructure/auth/](jakartaee/src/main/java/com/example/infrastructure/auth)  
:open_file_folder: JUnit：[/application/](jakartaee/src/test/java/com/example/application)

#### 例外ハンドリング
ExceptionMapperを用いて未処理の例外を一か所で集中的に処理し、アプリケーション全体で一貫した形式のエラーレスポンスを生成します。

:open_file_folder: コード：[GlobalExceptionMapper.java](jakartaee/src/main/java/com/example/application/GlobalExceptionMapper.java)、[ErrorResponseFactory.java](jakartaee/src/main/java/com/example/application/ErrorResponseFactory.java)、[ErrorResponse.java](jakartaee/src/main/java/com/example/application/ErrorResponse.java)


## Properties.java
#### プロパティ取得ユーティリティ
型を指定したプロパティ取得が可能です。型パラメータと関数インターフェースを用いた実装になっています。

:open_file_folder: コード：[Properties.java](jakartaee/src/main/java/com/example/Properties.java)  
:open_file_folder: 使用例：[PropertiesTest.java](jakartaee/src/test/java/com/example/PropertiesTest.java)

## ExtractingJsonSerializer.java
Javaオブジェクトから指定プロパティのみを抽出してJSON化するカスタムシリアライザ。  
APIのレスポンスを、クライアントからの要求に応じて、返却するプロパティを制限したい場合に活用できます。

同じことができる既存ライブラリもありますが、依存ライブラリを増やしたくない場合、このように JakartaEE と Java の標準機能だけで作成することもできます。

:open_file_folder: コード：[ExtractingJsonSerializer.java](jakartaee/src/main/java/com/example/application/ExtractingJsonSerializer.java)  
:open_file_folder: 使用例：[UsersResource.java#L44](jakartaee/src/main/java/com/example/application/users/UsersResource.java#L44)


## Cryptor.java
#### 暗号化／複合化ユーティリティ
Java 標準の javax.crypto パッケージを用いた暗号化／複合化の実装例

:open_file_folder: コード：[Cryptor.java](jakartaee/src/main/java/com/example/Cryptor.java)  
:open_file_folder: 使用例：[CryptorTest.java](jakartaee/src/test/java/com/example/CryptorTest.java)



## Slack通知クライアント
Slack の Webhook エンドポイントに通知を POST します。プロキシ経由、非同期に送信する実装例です。　

:open_file_folder: コード：[clients/](jakartaee/src/main/java/com/example/infrastructure/clients)  
:open_file_folder: 使用例：[UsersInteractor.java#L36](jakartaee/src/main/java/com/example/application/users/UsersInteractor.java#L36)  


## JEP330 (Launch Single-File Source-Code Programs)
Java11で導入された JEP330 (Launch Single-File Source-Code Programs)を使用して、便利なツールをいくつか作成しました。  
Javaソースを事前コンパイルしないで直接実行するため、スクリプトのように手軽に実行できます。Java標準APIのみを使ったシンプルな利用であれば非常に簡単です。

#### デスクトップ時計
Windows タスクバーの時計には分(minutes)までしか表示されません。秒まで表示できるデスクトップ時計です。Swingを使ってGUIを作成しています。

:open_file_folder: コード：[DesktopClocker.java](JEP330/desctop-clocker/DesktopClocker.java)  

#### APIモック
ローカル環境で起動するAPIモックの実装例です。Java標準のcom.sun.net.httpserver.HttpServerを使っています。

:open_file_folder: コード：[MockApiServer.java](JEP330/mockapi-server/MockApiServer.java)  




