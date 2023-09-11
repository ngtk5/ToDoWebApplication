package com.ngtk5.todoapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ngtk5.todoapp.beans.User;
import com.ngtk5.todoapp.mappers.UserMapper;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * ユーザIDを元にユーザ情報を取得する
     * @param userId ユーザID
     * @return ユーザ情報
     */
    public User getUserAccount(String userId) {
        return userMapper.getUserAccount(userId);
    }

    /**
     * ユーザ情報を登録する
     * @param user ユーザ情報
     * @return 登録件数
     */
    public int add(User user) {
        // パスワードをハッシュ化する
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user);
    }

    /**
     * ユーザ情報を更新する
     * @param user ユーザ情報
     * @return 更新件数
     */
    public int update(User user) {
        // パスワードをハッシュ化する
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.update(user);
    }

    /**
     * ユーザ情報を削除する
     * @param userId ユーザID
     * @return 削除件数
     */
    public int delete(String userId) {
        return userMapper.delete(userId);
    }

    /**
     * 電話番号の重複チェック
     * @param telephone 電話番号
     * @return あれば重複番号,なければnull
     */
    public String findTelephone(String telephone) {
        return userMapper.findTelephone(telephone);
    }

    /**
     * ユーザIDの重複チェック
     * @param userId ユーザID
     * @return あれば重複ID,なければnull
     */
    public String findUserId(String userId) {
        return userMapper.findUserId(userId);
    }

    /**
     * パスワードを取得
     * @param userId ユーザID
     * @return パスワード
     */
    public String getPassword(String userId) {
        return userMapper.getPassword(userId);
    }

    /**
     * ユーザーネームを取得
     * @param userId ユーザID
     * @return ユーザーネーム
     */
    public String getUsername(String userId) {
        return userMapper.getUsername(userId);
    }
}
