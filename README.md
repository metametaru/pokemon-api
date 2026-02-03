# Pokemon API

Spring Bootで作成したポケモン情報APIです。

## セットアップ

### 1. Firebase設定（必須）

このプロジェクトはFirebase Authenticationを使用しています。
起動前に以下の設定が必要です。

1. [Firebase Console](https://console.firebase.google.com/)でプロジェクトを作成
2. プロジェクト設定 > サービスアカウント > 新しい秘密鍵を生成
3. ダウンロードしたJSONファイルを `src/main/resources/firebase-service-account.json` として配置

### 2. 起動

```bash
./mvnw spring-boot:run
```

## API

| エンドポイント | 認証 | 説明 |
|---------------|------|------|
| `GET /api/pokemon` | 不要 | ポケモン一覧 |
| `GET /api/pokemon/search?q=` | 不要 | ポケモン検索 |
| `GET /api/favorites` | 必要 | お気に入り一覧 |
| `POST /api/favorites/{id}` | 必要 | お気に入り追加 |
| `DELETE /api/favorites/{id}` | 必要 | お気に入り削除 |
