package com.ngtk5.todoapp.forms;

import org.hibernate.validator.constraints.Length;

// 独自アノテーションimport
import com.ngtk5.todoapp.annotations.PasswordCorrelationCheck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@PasswordCorrelationCheck
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpUserForm {

    /*
     * ユーザIDフィールド
     * 空白なし
     * 文字数:1-10文字
     * 許可される文字:a-z,A-Z,0-9
     * 登録済み文字列なら不可
     */
    @NotBlank
    @Length(min=1, max=10)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String userId; // ユーザID

    /*
     * ユーザネームフィールド
     * 空白なし
     * 文字数:1-10文字
     * 許可される文字:a-z,A-Z,0-9
     */
    @NotBlank
    @Length(min=1, max=10)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username; // ユーザネーム

    /*
     * パスワードフィールド
     * 空白なし
     * 文字数:4-20文字
     * 許可される文字:a-z,A-Z,0-9,@!$#?
     */
    @NotBlank
    @Length(min=4, max=20)
    @Pattern(regexp = "^[a-zA-Z0-9@!$#?&]+$")
    private String password; // パスワード

    /*
     * 確認用パスワードフィールド
     */
    private String confirmPassword; // 確認用パスワード

    /*
     * 電話番号フィールド
     * 空白なし
     * 文字数:10-11文字
     * 許可される文字:0-9
     * 登録済み文字列なら不可
     */
    @NotBlank
    @Length(min=10, max=11)
    @Pattern(regexp = "[0-9]*")
    private String telephone; // 電話番号
}
