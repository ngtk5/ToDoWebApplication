package com.ngtk5.todoapp.forms;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserForm {

    private String userId; // ユーザID

    @NotBlank
    @Length(min=1, max=10)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username; // ユーザネーム

    @NotBlank
    private String password; // パスワード

    @NotBlank
    @Length(min=10, max=11)
    @Pattern(regexp = "[0-9]*")
    private String telephone; // 電話番号

}
