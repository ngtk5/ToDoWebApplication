package com.ngtk5.todoapp.mappers;

import com.ngtk5.todoapp.beans.Task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/*
 * 参考
 * https://qiita.com/avenue68/items/e066e672c3c99235aba1
 * https://teratail.com/questions/293403
 * https://mybatis.org/spring-boot-starter/mybatis-spring-boot-test-autoconfigure/
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/TestSchema.sql", "/TestData.sql"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 非staticメソッドをMethodSourceで使えるように
public class TaskMapperTest {

    @Autowired
    private TaskMapper taskMapper;

    private final String testUserId = "testUser";

    // 時間単位を無視して検証するためのデータフォーマット(yyyy-mm-dd)
    DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void testSelectAll() {
        // Given
        // When
        List<Task> tasks = taskMapper.selectAll();
        // Then
        assertThat(tasks).hasSize(6);
    }

    /*
     * 過去→未来順で取得できるかテスト
     */
    @Test
    void selectByUserIdOrderByDeadlineテスト() {
        // Given
        // When
        List<Task> tasks = taskMapper.selectByUserIdOrderByDeadline(testUserId);
        // Then
        assertThat(tasks).hasSize(2);
        LocalDate data1 = tasks.get(0).getDeadline();
        LocalDate data2 = tasks.get(tasks.size()-1).getDeadline();
        assertThat(data1.isBefore(data2) || data1.isEqual(data2)).isTrue();
    }

    /*
     * 未来→過去順で取得できるかテスト
     */
    @Test
    void selectByUserIdOrderByDeadlineDownテスト() {
        // Given
        // When
        List<Task> tasks = taskMapper.selectByUserIdOrderByDeadlineDown(testUserId);
        // Then
        assertThat(tasks).hasSize(2);
        LocalDate data1 = tasks.get(0).getDeadline();
        LocalDate data2 = tasks.get(tasks.size()-1).getDeadline();
        assertThat(data1.isAfter(data2) || data1.isEqual(data2)).isTrue();
    }

    /*
     * 辞書順で取得できるかテスト
     */
    @Test
    void selectByUserIdOrderByTitleテスト() {
        // Given
        // When
        List<Task> tasks = taskMapper.selectByUserIdOrderByTitle(testUserId);
        // Then
        assertThat(tasks).hasSize(2);
        String str1 = tasks.get(0).getTitle();
        String str2 = tasks.get(tasks.size()-1).getTitle();
        int comparisonResult = str1.compareTo(str2);
        assertThat(comparisonResult).isLessThan(1);
    }

    /*
     * 逆辞書順で取得できるかテスト
     */
    @Test
    void selectByUserIdOrderByTitleDownテスト() {
        // Given
        // When
        List<Task> tasks = taskMapper.selectByUserIdOrderByTitleDown(testUserId);
        // Then
        assertThat(tasks).hasSize(2);
        String taskTitle1 = tasks.get(0).getTitle();
        String taskTitle2 = tasks.get(tasks.size()-1).getTitle();
        int comparisonResult = taskTitle1.compareTo(taskTitle2);
        assertThat(comparisonResult).isGreaterThanOrEqualTo(0);
    }

    /*
     * false→trueの順で取得できるかテスト
     */
    @Test
    void selectByUserIdOrderByStatuテスト() {
        // Given
        // When
        List<Task> tasks = taskMapper.selectByUserIdOrderByStatus(testUserId);
        // Then
        assertThat(tasks).hasSize(2);
        boolean bool1 = tasks.get(0).isCompletedFlg();
        boolean bool2 = tasks.get(tasks.size()-1).isCompletedFlg();
        assertThat(bool1).isFalse();
        assertThat(bool2).isTrue();
    }

    /*
     * true→falseの順で取得できるかテスト
     */
    @Test
    void selectByUserIdOrderByStatusDownテスト() {
        // Given
        // When
        List<Task> tasks = taskMapper.selectByUserIdOrderByStatusDown(testUserId);
        // Then
        assertThat(tasks).hasSize(2);
        boolean bool1 = tasks.get(0).isCompletedFlg();
        boolean bool2 = tasks.get(tasks.size()-1).isCompletedFlg();
        assertThat(bool1).isTrue();
        assertThat(bool2).isFalse();
    }

    @Test
    void selectByTaskId_条件を満たす場合() {
        // Given
        // 検証に使用するタスクを準備
        Task expectedTask = new Task(
            "jhf92japdq",
            testUserId,
            "true task title",
            "true task description.",
            true,
            LocalDate.of(9999, 12, 31),
            LocalDateTime.of(1000, 01, 01, 01, 01, 01),
            null
        );
        // When
        Task task = taskMapper.selectByTaskId("jhf92japdq", testUserId);
        // Then
        assertThat(task).isEqualTo(expectedTask);
    }

    /*
     * タスク情報を取得できないタスクIDとユーザーIDの組を提供するメソッド
     */
    private String[][] nullTaskPattern() {
        return new String[][] {
            {"jhf92japdq", "存在しないユーザーID"},
            {"存在しないタスクID", testUserId},
            {"存在しないタスクID", "存在しないユーザーID"}
        };
    }

    @ParameterizedTest
    @MethodSource("nullTaskPattern")
    void selectByTaskId_条件を満たさない場合(String taskId, String userId) {
        // Given
        // When
        Task task = taskMapper.selectByTaskId(taskId, userId);
        // Then
        assertThat(task).isNull();
    }

    @Test
    void update_条件を満たす場合() {
        // Given
        String testTaskId = "jhf92japdq";
        Task task = new Task(
            testTaskId,
            testUserId,
            "change title",
            "change description.",
            false,
            LocalDate.of(5000, 5, 5),
            LocalDateTime.of(1000, 01, 01, 01, 01, 01),
            null
            );
        // When
        int updateCount = taskMapper.update(task);
        Task actualTask = taskMapper.selectByTaskId(testTaskId, testUserId);
        // Then
        assertAll(
            () -> assertThat(updateCount).isEqualTo(1),
            () -> assertThat(actualTask.getTaskId()).isEqualTo(task.getTaskId()),
            () -> assertThat(actualTask.getUserId()).isEqualTo(task.getUserId()),
            () -> assertThat(actualTask.getTitle()).isEqualTo(task.getTitle()),
            () -> assertThat(actualTask.getDescription()).isEqualTo(task.getDescription()),
            () -> assertThat(actualTask.isCompletedFlg()).isFalse(),
            () -> assertThat(actualTask.getDeadline()).isEqualTo(task.getDeadline()),
            () -> assertThat(actualTask.getCreationTime()).isEqualTo(task.getCreationTime()),
            () -> assertThat(actualTask.getUpdateTime().format(dtFormatter)).isEqualTo(LocalDateTime.now().format(dtFormatter))
        );
    }

    @ParameterizedTest
    @MethodSource("nullTaskPattern")
    void update_条件を満たさない場合(String taskId, String userId) {
        // Given
        Task updateTask = new Task(
            taskId,
            userId,
            "change title",
            "change description.",
            false,
            LocalDate.of(5000, 5, 5),
            null,
            null
            );
        // When
        int updateCount = taskMapper.update(updateTask);
        // Then
        assertThat(updateCount).isZero();
    }

    @Test
    void delete_条件を満たす場合() {
        // Given
        // When
        int beforeCount = taskMapper.selectByUserIdOrderByDeadline(testUserId).size();
        int deleteCount = taskMapper.delete("jhf92japdq", testUserId);
        int afterCount = taskMapper.selectByUserIdOrderByDeadline(testUserId).size();
        // Then
        assertAll(
            () -> assertThat(deleteCount).isEqualTo(1),
            () -> assertThat(beforeCount).isGreaterThan(afterCount)
        );
    }

    @ParameterizedTest
    @MethodSource("nullTaskPattern")
    void delete_条件を満たさない場合(String taskId, String userId) {
        // When
        int beforeCount = taskMapper.selectByUserIdOrderByDeadline(userId).size();
        int deleteCount = taskMapper.delete(taskId, userId);
        int afterCount = taskMapper.selectByUserIdOrderByDeadline(userId).size();
        // Then
        assertAll(
            () -> assertThat(deleteCount).isZero(),
            () -> assertThat(afterCount).isEqualTo(beforeCount)
        );
    }

    @Test
    void selectCheckTaskId_重複IDが存在する() {
        // Given
        // When
        int duplicationCount = taskMapper.selectCheckTaskId("jhf92japdq");
        // Then
        assertThat(duplicationCount).isEqualTo(1);
    }

    @Test
    void selectCheckTaskId_重複IDが存在しない() {
        // Given
        // When
        int duplicationCount = taskMapper.selectCheckTaskId("存在しないタスクID");
        // Then
        assertThat(duplicationCount).isZero();
    }

    @Test
    void insert_条件を満たす() {
        // Given
        String testTaskId = "insert0001";
        Task insertTask = new Task(
            testTaskId,
            testUserId,
            "insert title",
            "insert description.",
            false,
            LocalDate.of(8888, 8, 8),
            null,
            null
        );
        // When
        int beforeTaskCount = taskMapper.selectByUserIdOrderByDeadline(testUserId).size();
        int cnt = taskMapper.insert(insertTask);
        int afterTaskCount = taskMapper.selectByUserIdOrderByDeadline(testUserId).size();
        Task actualTask = taskMapper.selectByTaskId(testTaskId, testUserId);
        // Then
        assertAll(
            () -> assertThat(cnt).isEqualTo(1),
            () -> assertThat(beforeTaskCount).isLessThan(afterTaskCount),
            () -> assertThat(actualTask.getTaskId()).isEqualTo(insertTask.getTaskId()),
            () -> assertThat(actualTask.getUserId()).isEqualTo(insertTask.getUserId()),
            () -> assertThat(actualTask.getTitle()).isEqualTo(insertTask.getTitle()),
            () -> assertThat(actualTask.getDescription()).isEqualTo(insertTask.getDescription()),
            () -> assertThat(actualTask.isCompletedFlg()).isFalse(),
            () -> assertThat(actualTask.getDeadline()).isEqualTo(insertTask.getDeadline()),
            () -> assertThat(actualTask.getCreationTime().format(dtFormatter)).isEqualTo(LocalDateTime.now().format(dtFormatter)),
            () -> assertThat(actualTask.getUpdateTime()).isNull()
        );
    }

    @Test
    void completedAllDelete_削除件数が1以上() {
        // Given
        String userId = "trueUser";
        // When
        int beforeTaskCount = taskMapper.selectByUserIdOrderByDeadline(userId).size();
        int deleteCount = taskMapper.completedAllDelete(userId);
        int afterTaskCount = taskMapper.selectByUserIdOrderByDeadline(userId).size();
        // Then
        assertAll(
            () -> assertThat(deleteCount).isGreaterThanOrEqualTo(1),
            () -> assertThat(beforeTaskCount).isGreaterThan(afterTaskCount)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"falseUser", "存在しないユーザーID"})
    void completedAllDelete_削除件数が0(String userId) {
        // Given
        // When
        int beforeTaskCount = taskMapper.selectByUserIdOrderByDeadline(userId).size();
        int deleteCount = taskMapper.completedAllDelete(userId);
        int afterTaskCount = taskMapper.selectByUserIdOrderByDeadline(userId).size();
        // Then
        assertAll(
            () -> assertThat(deleteCount).isZero(),
            () -> assertThat(beforeTaskCount).isEqualTo(afterTaskCount)
        );
    }
}

