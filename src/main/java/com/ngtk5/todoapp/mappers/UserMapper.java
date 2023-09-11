package com.ngtk5.todoapp.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ngtk5.todoapp.beans.User;
import com.ngtk5.todoapp.forms.LoginUserForm;

@Mapper
public interface UserMapper {

    // 全てのユーザー情報(パスワード以外)を取得する
    public List<User> selectAll();

    // ユーザIDとパスワードを取得する
    public LoginUserForm getLoginRequiredInfo(String userId);

    // ユーザ情報を取得する
    public User getUserAccount(String userId);

    // ユーザ情報を登録する
    public int insert(User user);

    // ユーザ情報を更新する
    public int update(User user);

    // ユーザ情報を削除する
    public int delete(String userId);

    // 指定の電話番号を取得する
    public String findTelephone(String telephone);

    // 指定のユーザIDを取得する
    public String findUserId(String userId);

    // パスワードを取得する
    public String getPassword(String userId);

    // ユーザーネームを取得する
    public String getUsername(String userId);
}
