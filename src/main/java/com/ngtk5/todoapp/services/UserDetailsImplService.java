package com.ngtk5.todoapp.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ngtk5.todoapp.beans.UserDetailsImpl;
import com.ngtk5.todoapp.forms.LoginUserForm;
import com.ngtk5.todoapp.mappers.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsImplService implements UserDetailsService {

    @Autowired
	UserMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // LoginUserEntityに設定する
		LoginUserForm user = mapper.getLoginRequiredInfo(userId);
        // 取得結果が0件（ユーザが存在しない）の場合
		if (Objects.isNull(user)) {
			log.info("not found : " + userId);
			throw new UsernameNotFoundException("not found : " + userId);
		}
        // 取得したUserAccountを指定して、UserDetailsを作成
		return (new UserDetailsImpl(user));
	}
}
