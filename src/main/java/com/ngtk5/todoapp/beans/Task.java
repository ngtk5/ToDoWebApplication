package com.ngtk5.todoapp.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    // タスクID
    private String taskId;
    // ユーザID
    private String userId;
    // タイトル
    private String title;
    // 詳細
    private String description;
    // 完了済みフラグ(必ずtrue or falseが格納される)
    private boolean completedFlg;
    // タスク期限
    private LocalDate deadline;
    // 登録日時
    private LocalDateTime creationTime;
    // 更新日時
    private LocalDateTime updateTime;
}

