package com.ngtk5.todoapp.forms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import com.ngtk5.todoapp.validations.PasswordMatchValid;
import com.ngtk5.todoapp.validations.UniqueTelephoneValid;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 非staticメソッドをMethodSourceで使えるように
public class UserFormTest {

    @Autowired
    Validator validator;

    @Autowired
    UniqueTelephoneValid uniqueTelephoneValid;

    @Autowired
    PasswordMatchValid passwordmatchValid;

    /**
     * ユーザーネーム用バリデーションテストデータ
     * 成功パターン
     * @return テストデータリスト
     */
    String[] usernameTestDataSuccess() {
        String[] testDataList = {
            "q",// 1文字(最小長)
            "qwertyuiop",// 10文字(最大長)
            "dfihia",// a-z
            "KFOAYR",// A-Z
            "956127",// 0-9
            "aoDFfj",// a-z,A-Zを含む
            "aof025nd",// a-z,0-9を含む
            "AF9F4JG",// A-Z,0-9を含む
            "aO97Fj2n",// a-z,A-Z,0-9を含む
            "admin"// 登録済み
        };
        return testDataList;
    }

    /**
     * ユーザーネーム用バリデーションテストデータ
     * 失敗パターン
     * @return テストデータリスト
     */
    String[] usernameTestDataFailure() {
        String[] testDataList = {
            null,// null
            "",// 空文字(最小長未満)
            "qwer1234zsas",// 11文字以上(最大長超えの文字数)
            " ",// 半角空白
            "a d",// 半角空白を含む
            "　",// 全角空白
            "6a　D0",// 全角空白を含む
            "@:!?",// 特殊文字
            "useR&13@",// 特殊文字を含む
            "ユーザー",// 日本語
            "i6まf1あG",// 日本語を含む
        };
        return testDataList;
    }


    /**
     * エラーフィールドの内容を確認後、真偽値を返す
     * nullの場合: true
     * null以外の場合: false
     * @param testFieldName テスト対象のフィールド名
     * @param bindingResult バインド結果を表す
     * @return 真偽値
     */
    boolean errorNullCheck(String testFieldName, BindingResult bindingResult) {
        FieldError errorFieldContents = bindingResult.getFieldError(testFieldName);  // エラーフィールド取得
        if (Objects.isNull(errorFieldContents)) {
            // エラーフィールドがnull
            return true;
        }
        // エラーフィールドがnull以外
        return false;
    }

    /**
     * 期待値と実測値を比較後、真偽値を返す
     * 実測値(actualResult)の取得方法
     *  エラー数(errorCount)を0と比較して取得
     *  errorCount>0の場合: 1
     *  errorCount=0の場合: 0
     *  errorCount<0の場合: -1
     * 期待値通りの場合: true
     * 期待値以外の場合: false
     * @param testFieldName テスト対象のフィールド名
     * @param bindingResult バインド結果を表す
     * @param expectedResult 期待値
     * @return 真偽値
     */
    boolean errorCountCheck(String testFieldName, BindingResult bindingResult, int expectedResult) {
        int errorCount = bindingResult.getFieldErrorCount(testFieldName); // エラー数取得
        int actualResult = Integer.compare(errorCount, 0);    // 実測値取得
        if (Objects.equals(expectedResult, actualResult)) {
            // 期待値と実測値が同じ
            return true;
        }
        // 期待値と実測値が相違
        return false;
    }

    @DisplayName("ユーザID格納")
    @ParameterizedTest
    @ValueSource(strings = {"testId"})
    public void userIdStoringTestSuccess(String userId) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUserId(userId);

        // 内容確認
        assertEquals(userId, userForm.getUserId());
    }

    @DisplayName("ユーザーネーム格納")
    @ParameterizedTest
    @ValueSource(strings = {"testName"})
    public void usernameStoringTestSuccess(String username) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUsername(username);

        // 内容確認
        assertEquals(username, userForm.getUsername());
    }

    @DisplayName("パスワード格納")
    @ParameterizedTest
    @ValueSource(strings = {"testPass1"})
    public void passwordStoringTestSuccess(String password) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setPassword(password);

        // 内容確認
        assertEquals(password, userForm.getPassword());
    }

    @DisplayName("電話番号格納")
    @ParameterizedTest
    @ValueSource(strings = {"01030502246"})
    public void telephoneStoringTestSuccess(String telephone) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setTelephone(telephone);

        // 内容確認
        assertEquals(telephone, userForm.getTelephone());
    }

    @DisplayName("バリデーション 対象:ユーザネーム 結果:成功")
    @ParameterizedTest
    @MethodSource("usernameTestDataSuccess")
    void usernameValidationTestSuccess(String username) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUsername(username);
        BindingResult bindingResult = new BindException(userForm, "userForm");
        validator.validate(userForm, bindingResult);  // バリデーションチェック

        // エラー有無確認
        assertTrue(errorNullCheck("username", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("username", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:ユーザネーム 結果:失敗")
    @ParameterizedTest
    @MethodSource("usernameTestDataFailure")
    void usernameValidationTestFailure(String username) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUsername(username);
        BindingResult bindingResult = new BindException(userForm, "userForm");
        validator.validate(userForm, bindingResult);  // バリデーションチェック

        // エラー有無確認
        assertFalse(errorNullCheck("username", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("username", bindingResult, 1));
    }

    @DisplayName("バリデーション 対象:パスワード 結果:成功")
    @ParameterizedTest
    @CsvSource({"demo, pass"})
    void passwordValidationTestSuccess(String userId, String password) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUserId(userId);
        userForm.setPassword(password);
        BindingResult bindingResult = new BindException(userForm, "userForm");
        validator.validate(userForm, bindingResult);  // バリデーションチェック
        passwordmatchValid.validate(userForm, bindingResult);

        // エラー有無確認
        assertTrue(errorNullCheck("password", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("password", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:パスワード 結果:失敗")
    @ParameterizedTest
    @CsvSource({"demo, miss"})
    void passwordValidationTestFailure(String userId, String password) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUserId(userId);
        userForm.setPassword(password);
        BindingResult bindingResult = new BindException(userForm, "userForm");
        validator.validate(userForm, bindingResult);  // バリデーションチェック
        passwordmatchValid.validate(userForm, bindingResult);

        // エラー有無確認
        assertFalse(errorNullCheck("password", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("password", bindingResult, 1));
    }

    @DisplayName("バリデーション 対象:電話番号 結果:成功")
    @ParameterizedTest
    @CsvSource({"demo, 09009876543"})
    void telephoneValidationTestSuccess(String userId, String telephone) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUserId(userId);
        userForm.setTelephone(telephone);
        BindingResult bindingResult = new BindException(userForm, "userForm");
        validator.validate(userForm, bindingResult);  // バリデーションチェック
        // 独自バリデーション
        uniqueTelephoneValid.validate(userForm, bindingResult);   // 重複チェック

        // エラー有無確認
        assertTrue(errorNullCheck("telephone", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("telephone", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:電話番号 結果:失敗")
    @ParameterizedTest
    @CsvSource({"demo, 09010293847"})
    void telephoneValidationTestFailure(String userId, String telephone) {
        // 準備
        UserForm userForm = new UserForm();
        userForm.setUserId(userId);
        userForm.setTelephone(telephone);
        BindingResult bindingResult = new BindException(userForm, "userForm");
        validator.validate(userForm, bindingResult);  // バリデーションチェック
        // 独自バリデーション
        uniqueTelephoneValid.validate(userForm, bindingResult);   // 重複チェック

        // エラー有無確認
        assertFalse(errorNullCheck("telephone", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("telephone", bindingResult, 1));
    }
}
