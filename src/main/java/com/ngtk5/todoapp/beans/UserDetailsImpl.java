package com.ngtk5.todoapp.beans;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ngtk5.todoapp.forms.LoginUserForm;


/**
 * ログイン関係のクラス
 * ログインにはuserId,passwordを使う
 */
public class UserDetailsImpl implements UserDetails {
    private final LoginUserForm user;

	public UserDetailsImpl(LoginUserForm user) {
		this.user =user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
