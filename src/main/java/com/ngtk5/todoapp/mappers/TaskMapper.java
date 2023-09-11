package com.ngtk5.todoapp.mappers;

import com.ngtk5.todoapp.beans.Task;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskMapper {

    /**
     * タスクの情報を全件取得する
     * @return タスク情報リスト
     */
    public List<Task> selectAll();

    /**
     * ユーザIDを条件にタスクの情報の一覧を取得する
     * ソート基準:期限(昇順)
     * @param userId ユーザID
     * @return タスク情報のリスト
     */
    public List<Task> selectByUserIdOrderByDeadline(@Param("userId") String userId);

    /**
     * ユーザIDを条件にタスクの情報の一覧を取得する
     * ソート基準:タイトル(昇順)
     * @param userId ユーザID
     * @return タスク情報のリスト
     */
    public List<Task> selectByUserIdOrderByTitle(@Param("userId") String userId);

    /**
     * ユーザIDを条件にタスクの情報の一覧を取得する
     * ソート基準:状態(昇順)
     * @param userId ユーザID
     * @return タスク情報のリスト
     */
    public List<Task> selectByUserIdOrderByStatus(@Param("userId") String userId);

    /**
     * ユーザIDを条件にタスクの情報の一覧を取得する
     * ソート基準:期限(降順)
     * @param userId ユーザID
     * @return タスク情報のリスト
     */
    public List<Task> selectByUserIdOrderByDeadlineDown(@Param("userId") String userId);

    /**
     * ユーザIDを条件にタスクの情報の一覧を取得する
     * ソート基準:タイトル(降順)
     * @param userId ユーザID
     * @return タスク情報のリスト
     */
    public List<Task> selectByUserIdOrderByTitleDown(@Param("userId") String userId);

    /**
     * ユーザIDを条件にタスクの情報の一覧を取得する
     * ソート基準:状態(降順)
     * @param userId ユーザID
     * @return タスク情報のリスト
     */
    public List<Task> selectByUserIdOrderByStatusDown(@Param("userId") String userId);

    /**
     * タスクIDを条件にタスクの情報を取得する
     * @param taskId タスクID
     * @return タスク情報
     */
    public Task selectByTaskId(@Param("taskId") String taskId, @Param("userId") String userId);

    /**
     * タスク情報を更新する
     * @param task タスク情報
     * @return 更新件数
     */
    public int update(Task task);

    /**
     * タスク情報を削除する
     * @param userId ユーザID
     * @param taskId タスクID
     * @return 削除件数
     */
    public int delete(@Param("taskId") String taskId, @Param("userId") String userId);

    /**
     * タスクIDの重複をチェックする
     * @param taskId タスクID
     * @return 重複件数
     */
    public Integer selectCheckTaskId(@Param("taskId") String taskId);

    /**
     * タスク情報を登録する
     * @param task タスク情報
     * @return 登録件数
     */
    public int insert(Task task);

    /**
     * ユーザIDを条件に完了済みタスクを全て削除する
     * @param userId ユーザID
     * @return 削除件数
     */
    public int completedAllDelete(@Param("userId") String userId);
}
