package com.example.hanghaetinder_bemain.domain.security;


import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;

	// 스프링 시큐리티에서 인증을 처리할때.
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
		return new UserDetailsImpl(member, member.getUserId(), member.getId(), member.getPassword());
	}
}
