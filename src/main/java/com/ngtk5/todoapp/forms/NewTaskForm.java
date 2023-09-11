package com.ngtk5.todoapp.forms;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.ngtk5.todoapp.annotations.DateCheck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 新規タスク作成画面の入力フォーム
 */
@DateCheck
@Data
public class NewTaskForm {

    // タイトル
    @NotBlank
    @Length(min=1, max=20)
    private String title;

    // 説明
    @NotBlank
    @Length(min=1, max=50)
    private String description;

    // 期限
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
}
