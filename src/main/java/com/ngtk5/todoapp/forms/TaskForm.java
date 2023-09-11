package com.ngtk5.todoapp.forms;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

// 独自アノテーション
import com.ngtk5.todoapp.annotations.DateCheck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


/**
 * タスク編集画面の入力フォーム
 */
@DateCheck
@Data
public class TaskForm {

    // タスクID
    @NotBlank
    @Length(min=10, max=10)
    @Pattern(regexp = "^[a-zA-Z0-9]{10,10}+$")
    private String taskId;

    // タイトル
    @NotBlank
    @Length(min=1, max=20)
    private String title;

    // 詳細
    @NotBlank
    @Length(min=1, max=50)
    private String description;

    // 完了済みフラグ
    @NotNull
    private boolean completedFlg;

    // 期限
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
}
