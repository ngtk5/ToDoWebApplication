package com.ngtk5.todoapp.beans;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId; // ユーザID
    private String username; // ユーザネーム
    private String password; // パスワード
    private LocalDateTime creationTime; // 登録日時
    private LocalDateTime updateTime; // 更新日時
    private String telephone; // 電話番号
}
