package com.example.hanghaetinder_bemain.security;


import com.example.hanghaetinder_bemain.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImp implements UserDetails {

	private final Member member; // 인증 완료된 User 객체
	private final String userId; // 인증 완료된 User 의 ID
	private final String password; // 인증 완료된 User 의 pw

	public UserDetailsImp(Member member, String userId, String password) {
		this.member = member;
		this.userId = userId;
		this.password = password;
	}

	public Member user() {
		return member;
	}

	public Member getUser() {
		return member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return null;
	}
	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
