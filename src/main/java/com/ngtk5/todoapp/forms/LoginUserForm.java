package com.ngtk5.todoapp.forms;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginUserForm {

    @NotBlank
    private String userId; // ユーザID

    @NotBlank
    private String password; // パスワード
}
