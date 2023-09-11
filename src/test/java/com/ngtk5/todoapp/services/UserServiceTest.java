package com.ngtk5.todoapp.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ngtk5.todoapp.beans.User;
import com.ngtk5.todoapp.mappers.UserMapper;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    // 初期化
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    private static Stream<User> getUserAccountMethodSource() {
        return Stream.of(new User(), null);
    }

    private static Stream<String> findTelephoneMethodSource() {
        return Stream.of("12345678901", null);
    }

    private static Stream<String> findUserIdMethodSource() {
        return Stream.of("userId01", null);
    }

    private static Stream<String> getPasswordMethodSource() {
        return Stream.of("$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO", null);
    }

    private static Stream<String> getUsernameMethodSource() {
        return Stream.of("username01", null);
    }

    @ParameterizedTest
    @MethodSource("getUserAccountMethodSource")
    void getUserAccountテスト(User exUser) throws Exception {
        // Given
        doReturn(exUser).when(this.userMapper).getUserAccount(any());
        // When
        User acUser = this.userService.getUserAccount("test");
        // Then
        assertThat(acUser).isEqualTo(exUser);
        verify(this.userMapper, times(1)).getUserAccount(any());
    }

    @Test
    void addテスト() throws Exception {
        // Given
        int exAddCount = 5;
        doReturn("hashPassword").when(this.passwordEncoder).encode(any());
        doReturn(exAddCount).when(this.userMapper).insert(any());
        // When
        int acAddCount = this.userService.add(new User());
        // Then
        assertThat(acAddCount).isEqualTo(exAddCount);
        verify(this.passwordEncoder, times(1)).encode(any());
        verify(this.userMapper, times(1)).insert(any());
    }

    @Test
    void updateテスト() throws Exception {
        // Given
        int exUpdateCount = 5;
        doReturn("hashPassword").when(this.passwordEncoder).encode(any());
        doReturn(exUpdateCount).when(this.userMapper).update(any());
        // When
        int updateCount = this.userService.update(new User());
        // Then
        assertThat(updateCount).isEqualTo(exUpdateCount);
        verify(this.passwordEncoder, times(1)).encode(any());
        verify(this.userMapper, times(1)).update(any());
    }

    @Test
    void deleteテスト() throws Exception {
        // Given
        int exDeleteCount = 5;
        doReturn(exDeleteCount).when(this.userMapper).delete(any());
        // When
        int deleteCount = this.userService.delete("test");
        // Then
        assertThat(deleteCount).isEqualTo(exDeleteCount);
        verify(this.userMapper, times(1)).delete(any());
    }

    @ParameterizedTest
    @MethodSource("findTelephoneMethodSource")
    void findTelephoneテスト(String exTelephone) throws Exception {
        // Given
        doReturn(exTelephone).when(this.userMapper).findTelephone(any());
        // When
        String acTelephone = this.userService.findTelephone("test");
        // Then
        assertThat(acTelephone).isEqualTo(exTelephone);
        verify(this.userMapper, times(1)).findTelephone(any());
    }

    @ParameterizedTest
    @MethodSource("findUserIdMethodSource")
    void findUserIdテスト(String exUserId) throws Exception {
        // Given
        doReturn(exUserId).when(this.userMapper).findUserId(any());
        // When
        String acUserId = this.userService.findUserId("test");
        // Then
        assertThat(acUserId).isEqualTo(exUserId);
        verify(this.userMapper, times(1)).findUserId(any());
    }

    @ParameterizedTest
    @MethodSource("getPasswordMethodSource")
    void getPasswordテスト(String exPassword) throws Exception {
        // Given
        doReturn(exPassword).when(this.userMapper).getPassword(any());
        // When
        String acPassword = this.userService.getPassword("test");
        // Then
        assertThat(acPassword).isEqualTo(exPassword);
        verify(this.userMapper, times(1)).getPassword(any());
    }

    @ParameterizedTest
    @MethodSource("getUsernameMethodSource")
    void getUsernameテスト(String exUsername) throws Exception {
        // Given
        doReturn(exUsername).when(this.userMapper).getUsername(any());
        // When
        String acUsername = this.userService.getUsername("test");
        // Then
        assertThat(acUsername).isEqualTo(exUsername);
        verify(this.userMapper, times(1)).getUsername(any());
    }
}
