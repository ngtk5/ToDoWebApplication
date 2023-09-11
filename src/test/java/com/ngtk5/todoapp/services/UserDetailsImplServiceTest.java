package com.ngtk5.todoapp.services;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ngtk5.todoapp.beans.UserDetailsImpl;
import com.ngtk5.todoapp.forms.LoginUserForm;
import com.ngtk5.todoapp.mappers.UserMapper;

public class UserDetailsImplServiceTest {

    @InjectMocks
    private UserDetailsImplService userDetailsImplService;

    @Mock
    private UserMapper userMapper;

    // 初期化
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameテスト_UserFound() throws Exception {
        // Given
        doReturn(new LoginUserForm()).when(userMapper).getLoginRequiredInfo(any());
        // When
        UserDetails acUserDetails = userDetailsImplService.loadUserByUsername("test");
        // Then
        assertThat(acUserDetails).isInstanceOf(UserDetailsImpl.class);
        verify(userMapper, times(1)).getLoginRequiredInfo(any());
    }

    @Test
    void loadUserByUsernameテスト_UserNotFound() throws Exception {
        // Given
        doReturn(null).when(userMapper).getLoginRequiredInfo(any());
        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsImplService.loadUserByUsername("test");
        });
        verify(userMapper, times(1)).getLoginRequiredInfo(any());
    }
}
