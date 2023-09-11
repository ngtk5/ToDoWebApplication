-- タスクマスタのテーブル作成
DROP TABLE IF EXISTS task;
CREATE TABLE task (
    task_id VARCHAR(10) NOT NULL PRIMARY KEY COMMENT 'タスクID',
    user_id VARCHAR(20) NOT NULL COMMENT 'ユーザID',
    title VARCHAR(20) NOT NULL COMMENT 'タイトル',
    description VARCHAR(50) NOT NULL COMMENT '詳細',
    completed_flg BOOLEAN NOT NULL COMMENT '完了済みフラグ',
    deadline DATE NOT NULL COMMENT '期限',
    creation_time DATETIME NOT NULL COMMENT '登録日時',
    update_time DATETIME COMMENT '更新日時'
) COMMENT 'タスクマスタ';

-- ユーザマスタのテーブル作成
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    user_id VARCHAR(20) NOT NULL PRIMARY KEY COMMENT 'ユーザID',
    username VARCHAR(10) NOT NULL COMMENT 'ユーザネーム',
    password VARCHAR(150) NOT NULL COMMENT 'パスワード',
    creation_time DATETIME NOT NULL COMMENT '登録日時',
    update_time DATETIME COMMENT '更新日時',
    telephone VARCHAR(11) NOT NULL UNIQUE COMMENT '電話番号' -- ハイフンなし
) COMMENT 'ユーザマスタ';