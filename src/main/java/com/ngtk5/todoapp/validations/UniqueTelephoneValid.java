package com.ngtk5.todoapp.validations;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ngtk5.todoapp.beans.User;
import com.ngtk5.todoapp.forms.SignUpUserForm;
import com.ngtk5.todoapp.forms.UserForm;
import com.ngtk5.todoapp.services.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 参考
 * https://qiita.com/be834194/items/b986f6574d6c9d5d7f44
 *
 * 電話番号のバリデーションに使用するクラス
 */
@Slf4j
@Component
public class UniqueTelephoneValid implements Validator {

    private final UserService userService;

    public UniqueTelephoneValid(UserService userService) {
        this.userService = userService;
    }

    /**
    * 引数で受け取ったクラスが、メソッド内で記述しているチェック対象かどうかを判定
    * @param clazz クラス
    * @return 真偽値(SignUpUserFormクラスまたはUserFormクラスならTrue)
    */
    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpUserForm.class.isAssignableFrom(clazz) ||
            UserForm.class.isAssignableFrom(clazz);
    }

    /**
    * 渡されたクラスによって異なる処理をする。
    * UserFormの場合、入力内容と更新前データが一致すれば何もしない。
    * @param obj 入力フォームオブジェクト
    * @param errors エラー
    */
    @Override
    public void validate(Object obj, Errors errors) {
        if (Objects.isNull(obj)) {
            return;
        }
        if (obj instanceof UserForm) {
            // UserFormの場合
            UserForm form = (UserForm) obj;
            log.info("{}", form);
            User user = userService.getUserAccount(form.getUserId());   // アカウント情報取得
            // 入力された電話番号とユーザーが登録済みの電話番号が同じなら以降の処理は実施しない
            if (Objects.equals(form.getTelephone(), user.getTelephone())) {
                return;
            }
            validateTelephone(form.getTelephone(), errors);
        } else if (obj instanceof SignUpUserForm) {
            // SignUpUserFormの場合
            SignUpUserForm form = (SignUpUserForm) obj;
            validateTelephone(form.getTelephone(), errors);
        }
    }

    /**
     * 値が入力されているならオブジェクトの値をもとに依存クラスのメソッドを呼び出し,
     * 結果がnullでない(値が返ってきた)ならバリデーションエラーとなる.
     * @param telephone 入力内容の電話番号
     * @param errors エラー
     */
    private void validateTelephone(String telephone, Errors errors) {
        // 電話番号がnull or 空白なら以降の処理は実施しない
        if (Objects.isNull(telephone) || telephone.isEmpty()) {
            return;
        }
        // 取得した情報がnullなら以降の処理は実施しない
        if (Objects.isNull(userService.findTelephone(telephone))) {
            return;
        }
        // エラーフィールドを追加
        errors.rejectValue("telephone", "uniqueTelephoneValid.errorMessage");
    }
}
