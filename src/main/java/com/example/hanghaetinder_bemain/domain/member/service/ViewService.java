package com.example.hanghaetinder_bemain.domain.member.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.domain.member.entity.DislikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.member.repository.DislikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.LikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberFavoriteRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViewService {

	private final MemberRepository memberRepository;
	private final MemberFavoriteRepository memberFavoriteRepository;
	private final LikeMemberRepository likeMemberRepository;
	private final DislikeMemberRepository dislikeMemberRepository;

	@Transactional(readOnly = true)
	public List<MemberResponseDto> users(UserDetailsImpl userDetails) {
		//1.사용자 아이디를꺼낸다
		Long userId = userDetails.getId();
		//2.사용자를 멤버객체에넣고
		Member member = findMemberById(userId);
		List<Member> normalUsers = memberRepository.findMembersExcludingLikesAndDislikes(member.getId());
		List<MemberResponseDto> result = new ArrayList<>();
		for (Member members : normalUsers) {
			List<Long> favoriteList = memberFavoriteRepository.findByFavoriteList1(member.getId());
			result.add(new MemberResponseDto(members, favoriteList));
		}
		//3. 리스트를 랜덤하게 섞는다
		Collections.shuffle(result);
		return result;
	}

	//사용자를 좋아요누른사람 목록
	@Transactional(readOnly = true)
	public List<MemberResponseDto> likedUser(UserDetailsImpl userDetails){
		//1.사용자 아이디를꺼낸다
		Long userId = userDetails.getId();

		//2.사용자를 멤버객체에넣고
		Member member = findMemberById(userId);

		//3. 나를 좋아요 누른사람을찾는다
		List<Member> likeMemberToUser = likeMemberRepository.findAllByLikedMember(member.getId());

		//4. 반환값
		List<MemberResponseDto> result = new ArrayList<>();
		for (Member findMember : likeMemberToUser) {
			Member matchedMember = memberRepository.findById(findMember.getId()).orElseThrow(
				() -> new IllegalArgumentException("사용자를 찾을수 없습니다."));
			List<Long> favoriteList = memberFavoriteRepository.findByFavoriteList1(matchedMember.getId());
			result.add(new MemberResponseDto(matchedMember, favoriteList));
		}
		//6.랜덤으로섞는다
		Collections.shuffle(result);
		return result;

	}
	public Member findMemberById(Long id){
		return memberRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("사용자를 찾을수가 없습니다"));
	}

}
