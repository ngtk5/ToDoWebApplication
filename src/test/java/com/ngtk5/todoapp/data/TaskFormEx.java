package com.ngtk5.todoapp.data;

import java.time.LocalDate;

import com.ngtk5.todoapp.annotations.DateCheck;

import lombok.Data;

/*
 * 例外を誘発させるするためのテスト用クラス
 */
@DateCheck
@Data
public class TaskFormEx {
    private boolean completedFlg;
    private LocalDate deadline;
}
