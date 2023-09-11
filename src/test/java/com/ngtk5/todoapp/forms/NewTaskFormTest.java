package com.ngtk5.todoapp.forms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 非staticメソッドをMethodSourceで使えるように
public class NewTaskFormTest {

    @Autowired
    Validator validator;

    /**
     * タイトル用バリデーションテストデータ
     * 成功パターン
     * @return テストデータ
     */
    String[] titleTestDataSuccess() {
        String[] testDataList = {
            "A",// 1文字(最小長)
            "ThisIs20-Characters.",// 20文字(最大長)
            "こんにちは",// 全角文字
            "Hello World",// 半角空白を含む
            "　",// 全角空白
            "全角　空白",// 全角空白を含む
            "@#$%",// 特殊文字
            "@#$% characters",// 特殊文字を含む
            "Title\nnewlines",// 制御文字を含む
        };
        return testDataList;
    }

    /**
     * タイトル用バリデーションテストデータ
     * 失敗パターン
     * @return テストデータ
     */
    String[] titleTestDataFailure() {
        String[] testDataList = {
            null,// null
            "",// 空文字(最小長未満)
            "This is a 21-char title!",// 21文字以上(最大長超えの文字数)
            " ",// 半角空白
            "\n\t",// 特殊文字
    };
        return testDataList;
    }

    /**
     * 詳細用バリデーションテストデータ
     * 成功パターン
     * @return テストデータ
     */
    String[] descriptionTestDataSuccess() {
        String[] testDataList = {
            "A",// 1文字(最小長)
            "ThisIsA50-CharDescription.TheMaximumLengthAllowed.",// 50文字(最大長)
            "Hello World",// 半角空白を含む
            "こんにちは",// 全角文字
        };
        return testDataList;
    }

    /**
     * 詳細用バリデーションテストデータ
     * 失敗パターン
     * @return テストデータ
     */
    String[] descriptionTestDataFailure() {
        String[] testDataList = {
            null,// null
            "",// 空文字
            " ",// 半角空白(最小長未満)
            "ThisIs51charDescriptionExceedsMaximumLengthAllowed.",// 51文字以上(最大長超えの文字数)
            "\n\t",// 特殊文字
        };
        return testDataList;
    }

    /**
     * 期限格納用テストデータ
     * 成功パターン
     * @return テストデータ
     */
    LocalDate[] deadlineTestDataSuccess() {
        LocalDate[] testDataList = {
            LocalDate.now(),    // 期限が今日
            LocalDate.of(3000, 12, 31), // 期限が未来
        };
        return testDataList;
    }

    /**
     * 期限格納用テストデータ
     * 失敗パターン
     * @return テストデータ
     */
    LocalDate[] deadlineTestDataFailure() {
        LocalDate[] testDataList = {
            null,   // 期限が未設定
            LocalDate.of(2000, 1, 1),  // 期限が過去
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

    @DisplayName("タイトル格納")
    @ParameterizedTest
    @ValueSource(strings = {"Sample Task"})
    public void testTitle(String title) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setTitle(title);

        // 内容確認
        assertEquals(title, newTaskForm.getTitle());
    }

    @DisplayName("詳細格納")
    @ParameterizedTest
    @ValueSource(strings = {"This is a sample task description."})
    public void testDescription(String description) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setDescription(description);

        // 内容確認
        assertEquals(description, newTaskForm.getDescription());
    }

    @DisplayName("期限格納")
    @ParameterizedTest
    @ValueSource(strings = {"2399-06-30"})
    public void testDeadline(String deadlineStr) {
        // 準備
        LocalDate deadline = LocalDate.parse(deadlineStr);
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setDeadline(deadline);

        // 内容確認
        assertEquals(deadline, newTaskForm.getDeadline());
    }

    @DisplayName("全フィールド格納")
    @Test
    void allFieldTest() {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setTitle("title");
        newTaskForm.setDescription("description");
        newTaskForm.setDeadline(LocalDate.of(2023, 10, 10));

        // 内容確認
        assertEquals("title", newTaskForm.getTitle());
        assertEquals("description", newTaskForm.getDescription());
        assertEquals(LocalDate.of(2023, 10, 10), newTaskForm.getDeadline());
    }

    @DisplayName("バリデーション 対象:タイトル 結果:成功")
    @ParameterizedTest
    @MethodSource("titleTestDataSuccess")
    public void titleValidationTestSuccess(String title) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setTitle(title);
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);    // バリデーションチェック

        // エラー有無確認
        assertTrue(errorNullCheck("title", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("title", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:タイトル 結果:失敗")
    @ParameterizedTest
    @MethodSource("titleTestDataFailure")
    public void titleValidationTestFailure(String title) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setTitle(title);
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);    // バリデーションチェック

        // エラー有無確認
        assertFalse(errorNullCheck("title", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("title", bindingResult, 1));
    }


    @DisplayName("バリデーション 対象:詳細 結果:成功")
    @ParameterizedTest
    @MethodSource("descriptionTestDataSuccess")
    public void descriptionValidationTestSuccess(String description) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setDescription(description);
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);    // バリデーションチェック

        // エラー有無確認
        assertTrue(errorNullCheck("description", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("description", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:詳細 結果:失敗")
    @ParameterizedTest
    @MethodSource("descriptionTestDataFailure")
    public void descriptionValidationTestFailure(String description) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setDescription(description);
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);

        // エラー有無確認
        assertFalse(errorNullCheck("description", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("description", bindingResult, 1));
    }


    @DisplayName("バリデーション 対象:期限 結果:成功")
    @ParameterizedTest
    @MethodSource("deadlineTestDataSuccess")
    void deadlineValidationTestSuccess(LocalDate deadline) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setDeadline(deadline);
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);

        // エラー有無確認
        assertTrue(errorNullCheck("deadline", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("deadline", bindingResult, 0));
    }

    @DisplayName("バリデーション 対象:期限 結果:失敗")
    @ParameterizedTest
    @MethodSource("deadlineTestDataFailure")
    void deadlineValidationTestFailure(LocalDate deadline) {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setDeadline(deadline);
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);

        // エラー有無確認
        assertFalse(errorNullCheck("deadline", bindingResult));

        // エラー数確認
        assertTrue(errorCountCheck("deadline", bindingResult, 1));
    }

    @DisplayName("バリデーション 対象:全て 結果:成功")
    @Test
    void allValidationTestSuccess() {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        newTaskForm.setTitle("title");
        newTaskForm.setDescription("description");
        newTaskForm.setDeadline(LocalDate.of(2023, 10, 10));
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);

        // エラー有無確認
        FieldError allFieldError = bindingResult.getFieldError();
        assertNull(allFieldError);

        // エラー数確認
        int errorCount = bindingResult.getErrorCount();
        assertEquals(errorCount, 0);
    }

    @DisplayName("バリデーション 対象:全て 結果:失敗")
    @Test
    void allValidationTestFailure() {
        // 準備
        NewTaskForm newTaskForm = new NewTaskForm();
        BindingResult bindingResult = new BindException(newTaskForm, "newTaskForm");
        validator.validate(newTaskForm, bindingResult);

        // エラー有無確認
        FieldError allFieldError = bindingResult.getFieldError();
        assertNotNull(allFieldError);

        // エラー数確認
        int errorCount = bindingResult.getErrorCount();
        assertEquals(errorCount, 3);
    }
}
