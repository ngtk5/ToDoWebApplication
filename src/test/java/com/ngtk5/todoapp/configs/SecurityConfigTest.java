package com.ngtk5.todoapp.configs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class SecurityConfigTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("パスワードエンコード 結果確認")
    @ParameterizedTest
    @ValueSource(strings = "update")
    void test001(String password) {
        String hashPassword = passwordEncoder.encode(password);
        log.info(hashPassword);
    }
}
