package com.ngtk5.todoapp.annotations;

import java.util.Objects;

import com.ngtk5.todoapp.forms.SignUpUserForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordCorrelationCheckImpl implements ConstraintValidator<PasswordCorrelationCheck, Object> {

    private String message;

    @Override
    public void initialize(PasswordCorrelationCheck constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (Objects.isNull(obj)) {
            return true;
        }
        if (obj instanceof SignUpUserForm) {
            // SignUpUserFormの場合
            SignUpUserForm form = (SignUpUserForm) obj;
            return errorCheck(form.getPassword(), form.getConfirmPassword(), context);
        } else {
            // 想定外のデータ型の場合
            throw new IllegalArgumentException("このデータ型はサポートされていません");
        }
    }

    private boolean errorCheck(String password, String confirmPassword, ConstraintValidatorContext context) {
        // パスワードがnullなら何もしない
        if (Objects.isNull(password)) {
            return true;
        }
        // パスワードと確認用パスワードが一致していれば何もしない
        if (Objects.equals(password, confirmPassword)) {
            return true;
        }
        // デフォルトの制約違反情報をクリア
        context.disableDefaultConstraintViolation();
        // エラーメッセージをconfirmPasswordにセット
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode("confirmPassword")
            .addConstraintViolation();
        return false;
    }
}

