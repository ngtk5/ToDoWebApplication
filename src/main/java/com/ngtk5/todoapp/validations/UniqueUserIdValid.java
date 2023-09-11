package com.ngtk5.todoapp.validations;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ngtk5.todoapp.forms.SignUpUserForm;
import com.ngtk5.todoapp.services.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 参考
 * https://qiita.com/be834194/items/b986f6574d6c9d5d7f44
 *
 * ユーザIDのバリデーションに使用するクラス
 */
@Component
@Slf4j
public class UniqueUserIdValid implements Validator {

    private final UserService userService;

    public UniqueUserIdValid(UserService userService) {
        this.userService = userService;
    }

    /**
     * 引数で受け取ったクラスが、メソッド内で記述しているチェック対象かどうかを判定
     * @param clazz クラス
     * @return 真偽値(SignUpUserFormクラスならTrue)
     */
	@Override
	public boolean supports(Class<?> clazz) {
		return SignUpUserForm.class.isAssignableFrom(clazz);
	}

    /**
     * 値が入力されているならオブジェクトの値をもとに依存クラスのメソッドを呼び出し,
     * 結果がnullでない(値が返ってきた)ならバリデーションエラーとなる.
     * @param obj
     * @param errors
     */
	@Override
	public void validate(Object obj, Errors errors) {
        log.info("validate method!");
        if (Objects.isNull(obj)) {
            log.info("obj null!");
            return;
        }
		SignUpUserForm form = (SignUpUserForm)obj;
        // ユーザIDがnull or 空白なら以降のチェックは実施しない.
		if(Objects.isNull(form.getUserId()) || form.getUserId().isEmpty()) {
            log.info("form userId null or empty!");
			return;
		}
        String userId = userService.findUserId(form.getUserId());
		if(Objects.isNull(userId)) {
            log.info("db userId null!");
            return;
        }
        log.info("field error!");
        errors.rejectValue("userId", "uniqueUserIdValid.errorMessage");
    }
}
