package com.ngtk5.todoapp.data;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class TestData {

    /**
     * 既存のユーザIDを全て返す
     * @return ユーザ情報
     */
    public Stream<String> getAllUserId() {
        return Stream.of("demo", "test", "admin");
    }
    /**
     * 既存のタスクIDを全て返す
     * @return タスクID
     */
    public Stream<String> getAllTaskId() {
        return Stream.of("safih23r2d", "494hjf2fff", "ianru29ckd", "10u9fhiche", "a2df8a93m1");
    }
    /**
     * 既存のタスクID全てをリストで返す
     * @return タスクIDのリスト
     */
    public String[] taskIds() {
        String[] taskIdList = {"safih23r2d", "494hjf2fff", "ianru29ckd", "10u9fhiche", "a2df8a93m1"};
        return taskIdList;
    }

    /**
     * 1番目の既存ユーザ情報を返す
     * @return ユーザ情報
     */
    private Object[] getUserData1() {
        return new Object[] {
            "demo",
            "demo",
            "pass",
            LocalDateTime.of(2023, 07, 15, 9, 23, 10),
            null,
            "09009876543"
        };
    }
    /**
     * 2番目の既存ユーザ情報を返す
     * @return ユーザ情報
     */
    private Object[] getUserData2() {
        return new Object[] {
            "test",
            "test",
            "pass",
            LocalDateTime.of(2022, 10, 20, 15, 31, 00),
            null,
            "09010293847"
        };
    }

    /**
     * 3番目の既存ユーザ情報を返す
     * @return ユーザ情報
     */
    private Object[] getUserData3() {
        return new Object[] {
            "admin",
            "admin",
            "pass",
            LocalDateTime.of(2022, 11, 17, 21, 12, 05),
            LocalDateTime.of(2023, 05, 02, 07, 30, 40),
            "08099991111"
        };
    }

    /**
     * 4番目の既存ユーザ情報を返す
     * @return ユーザ情報
     */
    private Object[] getUserData4() {
        return new Object[] {
            "mob",
            "mob",
            "pass",
            LocalDateTime.of(2020, 02, 10, 12, 34, 56),
            LocalDateTime.of(2023, 05, 20, 00, 00, 00),
            "08010203040"
        };
    }

    /**
     * 既存のユーザ情報を全て返す
     * @return ユーザ情報
     */
    public Stream<Arguments> getAllUserDataArgs() {
        return Stream.of(
            Arguments.of(getUserData1()),
            Arguments.of(getUserData2()),
            Arguments.of(getUserData3()),
            Arguments.of(getUserData4())
        );
    }

    /**
     * 1番目の既存ユーザの更新用データを返す
     * @return 更新用ユーザ情報
     */
    public Stream<Arguments> getUpdateUserData1() {
        return Stream.of(Arguments.of(
            "demo",
            "jett",
            "pjk3fia",
            LocalDateTime.of(2023, 07, 15, 9, 23, 10),
            null,
            "08090219021"
        ));
    }

    /**
     * 新規ユーザ情報を返す
     * @return 新規ユーザ情報
     */
    public Stream<Arguments> getNewUserTestData1() {
         return Stream.of(Arguments.of(
            "userId",
            "username",
            "password",
            null,
            null,
            "07022228888"
         ));
    }


    /**
     * 1番目の既存タスク情報を返す
     * @return タスク情報
     */
    public Object[] getTaskData1() {
        return new Object[] {
            "safih23r2d",
            "demo",
            "ダミーテキスト。ダミーテキスト。ダミーテ",
            "ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ",
            false,
            "2100-05-20",
            LocalDateTime.now(),
            null
        };
    }
    /**
     * 2番目の既存タスク情報を返す
     * @return タスク情報
     */
    public Object[] getTaskData2() {
        return new Object[] {
            "494hjf2fff",
            "demo",
            "ダミーテキスト。ダミ",
            "ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ",
            true,
            "2100-06-04",
            LocalDateTime.now(),
            null
        };
    }
    /**
     * 3番目の既存タスク情報を返す
     * @return タスク情報
     */
    public Object[] getTaskData3() {
        return new Object[] {
            "ianru29ckd",
            "test",
            "ダミーテキスト。ダミ",
            "ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ",
            true,
            "2100-10-22",
            LocalDateTime.now(),
            null
        };
    }
    /**
     * 4番目の既存タスク情報を返す
     * @return タスク情報
     */
    public Object[] getTaskData4() {
        return new Object[] {
            "10u9fhiche",
            "test",
            "ダミーテキスト。ダミ",
            "ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ",
            false,
            "2100-12-01",
            LocalDateTime.now(),
            null
        };
    }
    /**
     * 5番目の既存タスク情報を返す
     * @return タスク情報
     */
    public Object[] getTaskData5() {
        return new Object[] {
            "a2df8a93m1",
            "admin",
            "adminタイトル",
            "admin詳細",
            false,
            "2130-02-22",
            LocalDateTime.now(),
            null
        };
    }
    /**
     * 既存タスク情報を全て返す
     * @return タスク情報
     */
    public Stream<Arguments> getAllTaskDataArgs() {
        return Stream.of(
            Arguments.of(getTaskData1()),
            Arguments.of(getTaskData2()),
            Arguments.of(getTaskData3()),
            Arguments.of(getTaskData4()),
            Arguments.of(getTaskData5())
        );
    }
    /**
     * ユーザID:demoが作成したタスク情報
     * 期限:期限(昇順)
     * @return タスク情報
     */
    public Stream<Arguments> getDemoTaskDataArgsDeadlineSortUp() {
        return Stream.of(
            Arguments.of(getTaskData1()),
            Arguments.of(getTaskData2())
        );
    }
    /**
     * 新規タスク情報を返す
     * @return タスク情報
     */
    public Stream<Arguments> getNewTaskData1() {
        return Stream.of(Arguments.of(
            "demo",
            "demoタイトル",
            "demo詳細",
            false,
            "2040-06-10",
            null,
            null
        ));
    }
    /**
     * 1番目のタスクの更新情報を返す
     * @return タスク更新情報
     */
    public Stream<Arguments> getUpdateTaskData1() {
        return Stream.of(Arguments.of(
            "safih23r2d",
            "demo",
            "更新タイトル",
            "更新詳細",
            true,
            "2100-05-20",
            LocalDateTime.now(),
            null
        ));
    }
}
