package com.ngtk5.todoapp.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngtk5.todoapp.beans.Task;
import com.ngtk5.todoapp.mappers.TaskMapper;

import com.ngtk5.todoapp.components.RandomGenerater;

@Service
public class TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    RandomGenerater randomGenerater;

    /**
     * 全てのタスク情報を取得する
     * @return タスク情報のリスト
     */
    public List<Task> getAllTaskList() {
        return taskMapper.selectAll();
    }

    /**
     * ログイン中のユーザが作成したタスク情報の一覧を取得する
     * ソート基準:期限(昇順)
     * @return タスク情報のリスト
     */
    private List<Task> getTaskListOrderByDeadline(String userId) {
        return taskMapper.selectByUserIdOrderByDeadline(userId);
    }

    /**
     * ログイン中のユーザが作成したタスク情報の一覧を取得する
     * ソート基準:タイトル(昇順)
     * @return タスク情報のリスト
     */
    private List<Task> getTaskListOrderByTitle(String userId) {
        return taskMapper.selectByUserIdOrderByTitle(userId);
    }

    /**
     * ログイン中のユーザが作成したタスク情報の一覧を取得する
     * ソート基準:状態(昇順)
     * @return タスク情報のリスト
     */
    private List<Task> getTaskListOrderByStatus(String userId) {
        return taskMapper.selectByUserIdOrderByStatus(userId);
    }

    /**
     * ログイン中のユーザが作成したタスク情報の一覧を取得する
     * ソート基準:期限(降順)
     * @return タスク情報のリスト
     */
    private List<Task> getTaskListOrderByDeadlineDown(String userId) {
        return taskMapper.selectByUserIdOrderByDeadlineDown(userId);
    }

    /**
     * ログイン中のユーザが作成したタスク情報の一覧を取得する
     * ソート基準:タイトル(降順)
     * @return タスク情報のリスト
     */
    private List<Task> getTaskListOrderByTitleDown(String userId) {
        return taskMapper.selectByUserIdOrderByTitleDown(userId);
    }

    /**
     * ログイン中のユーザが作成したタスク情報の一覧を取得する
     * ソート基準:状態(降順)
     * @return タスク情報のリスト
     */
    private List<Task> getTaskListOrderByStatusDown(String userId) {
        return taskMapper.selectByUserIdOrderByStatusDown(userId);
    }

    /**
     * ソートIDを元にソート方法を選択する
     * @param sortId ソートID
     * @param userId ユーザID
     * @return
     */
    public List<Task> sortTaskList(String sortId, String userId) {
        switch (sortId) {
            case "title":
                return getTaskListOrderByTitle(userId);
            case "titleDown":
                return getTaskListOrderByTitleDown(userId);
            case "status":
                return getTaskListOrderByStatus(userId);
            case "statusDown":
                return getTaskListOrderByStatusDown(userId);
            case "deadlineDown":
                return getTaskListOrderByDeadlineDown(userId);
            default:
                return getTaskListOrderByDeadline(userId);
        }
    }

    /**
     * タスクIDを元にタスク情報を取得する
     * @param taskId タスクID
     * @return タスク情報
     */
    public Task getTask(String taskId, String userId) {
        return taskMapper.selectByTaskId(taskId, userId);
    }

    /**
     * タスク情報を更新する
     * @param task 入力内容
     * @return 更新件数
     */
    @Transactional
    public int update(Task task) {
        int cnt = taskMapper.update(task);
        return cnt;
    }

    /**
     * タスク情報を削除する
     * @param userId ユーザID
     * @param taskId タスクID
     * @return 削除件数
     */
    @Transactional
    public int delete(String taskId, String userId) {
        int cnt = taskMapper.delete(taskId, userId);
        return cnt;
    }

    /**
     * 新しいタスク情報を登録する
     * @param task 入力内容
     * @return 登録件数
     */
    @Transactional
    public int add(Task task) {
        // タスク情報を登録
        int cnt = taskMapper.insert(task);
        return cnt;
    }

    /**
     * 指定したユーザが作成した完了済みタスクをすべて削除する.
     * 条件:ユーザID
     * @param userId ユーザID
     * @return 削除件数
     */
    @Transactional
    public int completedAllDelete(String userId) {
        int cnt = taskMapper.completedAllDelete(userId);
        return cnt;
    }

    /**
     * 新しいタスクIDを生成する.
     * 重複のないタスクIDが生成されるまでループする.
     * @return タスクID
     */
    public String createNewTaskId() {
        do {
            // 新しいタスクIDを発行
            String taskId = randomGenerater.generateRandomString(10);
            // タスクIDの重複をチェック
            Integer checkIdNum = taskMapper.selectCheckTaskId(taskId);
            if (Objects.equals(0, checkIdNum)) {
                return taskId;
        }
        } while (true);
    }
}
