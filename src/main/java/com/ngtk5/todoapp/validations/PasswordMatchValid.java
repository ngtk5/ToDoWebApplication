package com.ngtk5.todoapp.validations;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ngtk5.todoapp.forms.UserForm;
import com.ngtk5.todoapp.services.UserService;

@Component
public class PasswordMatchValid implements Validator {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserService userService;

    public PasswordMatchValid(UserService userService) {
        this.userService = userService;
    }

    /**
    * 引数で受け取ったクラスが、メソッド内で記述しているチェック対象かどうかを判定
    * @param clazz クラス
    * @return 真偽値(UserFormクラスならTrue)
    */
    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class.isAssignableFrom(clazz);
    }

    /**
    * 渡されたクラスによって異なる処理をする。
    * UserFormの場合、入力内容と更新前データが一致すれば何もしない。
    * @param obj 入力フォームオブジェクト
    * @param errors エラー
    */
    @Override
    public void validate(Object obj, Errors errors) {
        UserForm form = (UserForm) obj;
        validatePassword(form.getPassword(), form.getUserId(), errors);
    }

    /**
     * 値が入力されているならオブジェクトの値をもとに依存クラスのメソッドを呼び出し,
     * 結果がnullでない(値が返ってきた)ならバリデーションエラーとなる.
     * @param password 入力内容のパスワード
     * @param errors エラー
     */
    private void validatePassword(String password, String userId, Errors errors) {
        if (Objects.isNull(password) || password.isEmpty()) {
            return; // null or 空文字なら何もしない
        }
        if (passwordEncoder.matches(password, userService.getPassword(userId))) {
            return; // 一致していれば何もしない
        }
        // エラーフィールドを追加
        errors.rejectValue("password", "passwordMatchValid.errorMessage");
    }
}
