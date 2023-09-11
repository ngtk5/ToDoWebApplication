package com.ngtk5.todoapp.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import com.ngtk5.todoapp.beans.User;
import com.ngtk5.todoapp.forms.LoginUserForm;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/TestSchema.sql", "/TestData.sql"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 非staticメソッドをMethodSourceで使えるように
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    // 時間単位を無視して検証するためのデータフォーマット(yyyy-mm-dd)
    DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void getLoginRequiredInfo_ユーザー情報が存在する() throws Exception {
        // Given
        String userId = "testUser";
        String password = "$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO";
        // When
        LoginUserForm loginUserForm = userMapper.getLoginRequiredInfo(userId);
        // Then
        assertThat(loginUserForm.getUserId()).isEqualTo(userId);
        assertThat(loginUserForm.getPassword()).isEqualTo(password);
    }

    @Test
    void getLoginRequiredInfo_ユーザー情報が存在しない() throws Exception {
        // Given
        String userId = "存在しないユーザーID";
        // When
        LoginUserForm loginUserForm = userMapper.getLoginRequiredInfo(userId);
        // Then
        assertThat(loginUserForm).isNull();
    }

    @Test
    void getUserAccount_ユーザー情報が存在する() throws Exception {
        // Given
        String userId = "testUser";
        User expectedUser = new User(
            userId,
            "testUser",
            null,
            LocalDateTime.of(2000, 01, 01, 00, 00, 00),
            null,
            "09009876543"
        );
        // When
        User user = userMapper.getUserAccount(userId);
        // Then
        assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    void getUserAccount_ユーザー情報が存在しない() throws Exception {
        // Given
        String userId = "存在しないユーザーID";
        // When
        User user = userMapper.getUserAccount(userId);
        // Then
        assertThat(user).isNull();
    }

    @Test
    void insert_正常登録() throws Exception {
        // Given
        User expectedUser = new User(
            "exUserId",
            "exUser",
            "testPass",
            LocalDateTime.now(),
            null,
            "08195710521"
        );
        // When
        int beforeUserListSize = userMapper.selectAll().size();
        int insertCount = userMapper.insert(expectedUser);
        int afterUserListSize = userMapper.selectAll().size();
        User actualUser = userMapper.getUserAccount("exUserId");
        // Then
        assertAll(
            () -> assertThat(insertCount).isEqualTo(1),
            () -> assertThat(beforeUserListSize).isLessThan(afterUserListSize),
            () -> assertThat(actualUser).isNotNull(),
            () -> assertThat(actualUser.getUserId()).isEqualTo(expectedUser.getUserId()),
            () -> assertThat(actualUser.getUsername()).isEqualTo(expectedUser.getUsername()),
            () -> assertThat(actualUser.getCreationTime().format(dtFormatter)).isEqualTo(expectedUser.getCreationTime().format(dtFormatter)),
            () -> assertThat(actualUser.getTelephone()).isEqualTo(expectedUser.getTelephone()),
            () -> assertThat(actualUser.getUpdateTime()).isNull()
        );
    }

    @Test
    void update_ユーザー情報が存在する() throws Exception {
        // Given
        User expectedUser = new User(
            "testUser",
            "changeName",
            "testPass",
            null,
            null,
            "09871240271"
        );
        // When
        int updateCount = userMapper.update(expectedUser);
        User actualUser = userMapper.getUserAccount("testUser");
        // Then
        assertAll(
            () -> assertThat(updateCount).isEqualTo(1),
            () -> assertThat(actualUser).isNotNull(),
            () -> assertThat(actualUser.getUserId()).isEqualTo(expectedUser.getUserId()),
            () -> assertThat(actualUser.getUsername()).isEqualTo(expectedUser.getUsername()),
            () -> assertThat(LocalDateTime.of(2000, 01, 01, 00, 00, 00).format(dtFormatter)).isEqualTo(actualUser.getCreationTime().format(dtFormatter)),
            () -> assertThat(actualUser.getUpdateTime().format(dtFormatter)).isEqualTo(LocalDateTime.now().format(dtFormatter)),
            () -> assertThat(actualUser.getTelephone()).isEqualTo(expectedUser.getTelephone())
        );
    }

    @Test
    void update_ユーザー情報が存在しない() throws Exception {
        // Given
        User expectedUser = new User(
            "存在しないユーザーID",
            "changeName",
            "testPass",
            LocalDateTime.of(2000, 01, 01, 00, 00, 00),
            null,
            "09871240271"
        );
        // When
        int updateCount = userMapper.update(expectedUser);
        // Then
        assertThat(updateCount).isZero();
    }

    @Test
    void delete_ユーザー情報が存在する() throws Exception {
        // Given
        String userId = "testUser";
        // When
        int beforeUserSize = userMapper.selectAll().size();
        int deleteCount = userMapper.delete(userId);
        int afterUserSize = userMapper.selectAll().size();
        User user = userMapper.getUserAccount(userId);
        // Then
        assertAll(
            () -> assertThat(deleteCount).isEqualTo(1),
            () -> assertThat(beforeUserSize).isGreaterThan(afterUserSize),
            () -> assertThat(user).isNull()
        );
    }

    @Test
    void delete_ユーザー情報が存在しない() throws Exception {
        // Given
        String userId = "存在しないユーザーID";
        // When
        int beforeUserSize = userMapper.selectAll().size();
        int deleteCount = userMapper.delete(userId);
        int afterUserSize = userMapper.selectAll().size();
        // Then
        assertAll(
            () -> assertThat(deleteCount).isZero(),
            () -> assertThat(beforeUserSize).isEqualTo(afterUserSize)
        );
    }

    @Test
    void findTelephone_電話番号が存在する() throws Exception {
        // Given
        String telephone = "09009876543";
        // When
        String findTelephone = userMapper.findTelephone(telephone);
        // Then
        assertThat(findTelephone).isEqualTo(telephone);
    }

    @Test
    void findTelephone_電話番号が存在しない() throws Exception {
        // Given
        String telephone = "存在しない電話番号";
        // When
        String findTelephone = userMapper.findTelephone(telephone);
        // Then
        assertThat(findTelephone).isNull();
    }

    @Test
    void findUserId_ユーザーIDが存在する() throws Exception {
        // Given
        String userId = "testUser";
        // When
        String finduserId = userMapper.findUserId(userId);
        // Then
        assertThat(finduserId).isEqualTo(userId);
    }

    @Test
    void findUserId_ユーザーIDが存在しない() throws Exception {
        // Given
        String userId = "存在しないユーザーID";
        // When
        String finduserId = userMapper.findUserId(userId);
        // Then
        assertThat(finduserId).isNull();
    }

    @Test
    void getPassword_ユーザ情報が存在する() throws Exception {
        // Given
        String userId = "testUser";
        String expectedpassword = "$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO";
        // When
        String actualPassword = userMapper.getPassword(userId);
        // Then
        assertThat(actualPassword).isEqualTo(expectedpassword);
    }

    @Test
    void getPassword_ユーザ情報が存在しない() throws Exception {
        // Given
        String userId = "存在しないユーザーID";
        // When
        String actualPassword = userMapper.getPassword(userId);
        // Then
        assertThat(actualPassword).isNull();
    }

    @Test
    void getUsername_ユーザー情報が存在する() throws Exception {
        // Given
        String userId = "testUser";
        String expectedUsername = "testUser";
        // When
        String actualUsername = userMapper.getUsername(userId);
        // Then
        assertThat(actualUsername).isEqualTo(expectedUsername);
    }

    @Test
    void getUsername_ユーザー情報が存在しない() throws Exception {
        // Given
        String userId = "存在しないユーザーID";
        // When
        String actualUsername = userMapper.getUsername(userId);
        // Then
        assertThat(actualUsername).isNull();
    }
}
