package com.ngtk5.todoapp.forms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import com.ngtk5.todoapp.validations.UniqueTelephoneValid;
import com.ngtk5.todoapp.validations.UniqueUserIdValid;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignUpUserFormTest {

    @Autowired
    Validator validator;

    @Autowired
    UniqueUserIdValid uniqueUserIdValidator;

    @Autowired
    UniqueTelephoneValid uniqueTelephoneValidator;

    /**
     * ユーザID用バリデーションテストデータ
     * 成功パターン
     * @return テストデータリスト
     */
    String[] userIdTestDataSuccess() {
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
        };
        return testDataList;
    }

    /**
     * ユーザID用バリデーションテストデータ
     * 失敗パターン
     * @return テストデータリスト
     */
    String[] userIdTestDataFailure() {
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
            "admin"// 登録済み
        };
        return testDataList;
    }

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
     * パスワード用バリデーションテストデータ
     * 成功パターン
     * @return テストデータリスト
     */
    String[] passwordTestDataSuccess() {
        String[] testDataList = {
            "qwer", // 4文字(最小長)
            "qwertyuiopasdfghjklz", // 20文字(最大長)
            "dfihia", // a-z
            "KFOAYR",// A-Z
            "12345", // 0-9
            "aoDFfj",// a-z,A-Zを含む
            "AF9F4JG",// A-Z,0-9を含む
            "khgis024d", // a-z,0-9を含む
            "aO97Fj2n",// a-z,A-Z,0-9を含む
            "@!$#?&", // 許可される特殊文字
            "jad!as@", // 許可される特殊文字を含む
            "pass",// 登録済み
        };
        return testDataList;
    }

    /**
     * パスワード用バリデーションテストデータ
     * 失敗パターン
     * @return テストデータリスト
     */
    String[] passwordTestDataFailure() {
        String[] testDataList = {
            null,// null
            "",// 空文字(最小長未満)
            "qwer1234zsas!&&las45?",// 21文字以上(最大長超えの文字数)
            " ",// 半角空白
            "a d",// 半角空白を含む
            "　",// 全角空白
            "6a　D0",// 全角空白を含む
            "ユーザー",// 日本語
            "i6まf1あG",// 日本語を含む
            ":*];[:/]", // 使用できない特殊文字
            "kfio37&:*];", // 使用できない特殊文字を含む
        };
        return testDataList;
    }

    /**
     * 電話番号用バリデーションテストデータ
     * 成功パターン
     * @return テストデータリスト
     */
    String[] telephoneTestDataSuccess() {
        String[] testDataList = {
            "0280112233", // 10文字(最小長)
            "02033446677", // 11文字(最大長)
        };
        return testDataList;
    }

    /**
     * 電話番号用バリデーションテストデータ
     * 失敗パターン
     * @return テストデータリスト
     */
    String[] telephoneTestDataFailure() {
        String[] testDataList = {
            null,// null
            "",// 空文字
            "90742", // 10文字未満(最小長未満)
            "0203344667744", // 12文字以上(最大長超えの文字数)
            "asdfghjklm", // a-z
            "091474iaha", // a-zを含むケース
            "-!#$%&*+?>", // 特殊文字
            "!%7*?356", // 特殊文字を含む
            " ",// 半角空白
            "0275 124",// 半角空白を含む
            "　",// 全角空白
            "664　50",// 全角空白を含む
            "ユーザー",// 日本語
            "66ま251あ4",// 日本語を含む
            "08010203040", // 登録済み
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
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setUserId(userId);

        // 内容確認
        assertEquals(userId, signUpUserForm.getUserId());
    }

    @DisplayName("ユーザーネーム格納")
    @ParameterizedTest
    @ValueSource(strings = {"testName"})
    public void usernameStoringTestSuccess(String username) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setUsername(username);

        // 内容確認
        assertEquals(username, signUpUserForm.getUsername());
    }

    @DisplayName("パスワード格納")
    @ParameterizedTest
    @ValueSource(strings = {"testPass1"})
    public void passwordStoringTestSuccess(String password) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setPassword(password);

        // 内容確認
        assertEquals(password, signUpUserForm.getPassword());
    }

    @DisplayName("確認用パスワード格納")
    @ParameterizedTest
    @ValueSource(strings = {"testPass1"})
    public void confirmPasswordStoringTestSuccess(String confirmPassword) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setConfirmPassword(confirmPassword);

        // 内容確認
        assertEquals(confirmPassword, signUpUserForm.getConfirmPassword());
    }

    @DisplayName("電話番号格納")
    @ParameterizedTest
    @ValueSource(strings = {"01030502246"})
    public void telephoneStoringTestSuccess(String telephone) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setTelephone(telephone);

        // 内容確認
        assertEquals(telephone, signUpUserForm.getTelephone());
    }

    @DisplayName("バリデーション 対象:ユーザID 結果:成功")
    @ParameterizedTest
    @MethodSource("userIdTestDataSuccess")
    void userIdValidationTestSuccess(String userId) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setUserId(userId);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック
        // 独自バリデーション
        uniqueUserIdValidator.validate(signUpUserForm, bindingResult);  // 重複チェック

        // エラー有無確認
        assertTrue(errorNullCheck("userId", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("userId", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:ユーザID 結果:失敗")
    @ParameterizedTest
    @MethodSource("userIdTestDataFailure")
    void userIdValidationTestFailure(String userId) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setUserId(userId);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック
        // 独自バリデーション
        uniqueUserIdValidator.validate(signUpUserForm, bindingResult);  // 重複チェック

        // エラー有無確認
        assertFalse(errorNullCheck("userId", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("userId", bindingResult, 1));
    }

    @DisplayName("バリデーション 対象:ユーザネーム 結果:成功")
    @ParameterizedTest
    @MethodSource("usernameTestDataSuccess")
    void usernameValidationTestSuccess(String username) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setUsername(username);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック

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
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setUsername(username);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック

        // エラー有無確認
        assertFalse(errorNullCheck("username", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("username", bindingResult, 1));
    }

    @DisplayName("バリデーション 対象:パスワード 結果:成功")
    @ParameterizedTest
    @MethodSource("passwordTestDataSuccess")
    void passwordValidationTestSuccess(String password) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setPassword(password);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック

        // エラー有無確認
        assertTrue(errorNullCheck("password", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("password", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:パスワード 結果:失敗")
    @ParameterizedTest
    @MethodSource("passwordTestDataFailure")
    void passwordValidationTestFailure(String password) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setPassword(password);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック

        // エラー有無確認
        assertFalse(errorNullCheck("password", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("password", bindingResult, 1));
    }

    @DisplayName("パスワード相関チェック 結果:成功")
    @ParameterizedTest
    @MethodSource("passwordTestDataSuccess")
    void passwordCorrelationTestSuccess(String password) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setPassword(password);
        signUpUserForm.setConfirmPassword(password);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック

        // エラー有無確認
        assertTrue(errorNullCheck("confirmPassword", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("confirmPassword", bindingResult, 0));
    }

    @DisplayName("パスワード相関チェック 結果:失敗")
    @ParameterizedTest
    @MethodSource("passwordTestDataSuccess")
    void passwordCorrelationTestFailure(String password) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setPassword(password);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック

        // エラー有無確認
        assertFalse(errorNullCheck("confirmPassword", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("confirmPassword", bindingResult, 1));
    }

    @DisplayName("バリデーション 対象:電話番号 結果:成功")
    @ParameterizedTest
    @MethodSource("telephoneTestDataSuccess")
    void telephoneValidationTestSuccess(String telephone) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setTelephone(telephone);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック
        // 独自バリデーション
        uniqueTelephoneValidator.validate(signUpUserForm, bindingResult);   // 重複チェック

        // エラー有無確認
        assertTrue(errorNullCheck("telephone", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("telephone", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:電話番号 結果:失敗")
    @ParameterizedTest
    @MethodSource("telephoneTestDataFailure")
    void telephoneValidationTestFailure(String telephone) {
        // 準備
        SignUpUserForm signUpUserForm = new SignUpUserForm();
        signUpUserForm.setTelephone(telephone);
        BindingResult bindingResult = new BindException(signUpUserForm, "singUpUserForm");
        validator.validate(signUpUserForm, bindingResult);  // バリデーションチェック
        // 独自バリデーション
        uniqueTelephoneValidator.validate(signUpUserForm, bindingResult);   // 重複チェック

        // エラー有無確認
        assertFalse(errorNullCheck("telephone", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("telephone", bindingResult, 1));
    }
}
