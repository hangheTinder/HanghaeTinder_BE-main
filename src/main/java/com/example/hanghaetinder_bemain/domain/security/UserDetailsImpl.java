package com.example.hanghaetinder_bemain.domain.security;


import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {

	private final Member member; // 인증 완료된 User 객체
	private final String userId; // 인증 완료된 User 의 ID
	private final Long Id;
	private final String password; // 인증 완료된 User 의 pw


	public UserDetailsImpl(Member member, String userId, Long id, String password) {
		this.member = member;
		this.userId = userId;
		Id = id;
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

		String authority = "USER";
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(simpleGrantedAuthority);

		return authorities;
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
