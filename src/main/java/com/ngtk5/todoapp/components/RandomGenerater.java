package com.ngtk5.todoapp.components;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class RandomGenerater {

    // 文字列生成に使用する文字
    private final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 指定された文字数のランダムな文字列を生成する。
     * @param length 文字数
     * @return 生成されたランダムな文字列
     */
    public String generateRandomString(int length) {
        // 乱数生成
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();

        // length分の文字を選ぶ
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
