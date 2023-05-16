package com.example.hanghaetinder_bemain.domain.member.service;

import static com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanghaetinder_bemain.domain.common.exception.CustomException;
import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.member.repository.DislikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.LikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberFavoriteRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.member.util.StatusEnum;
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

	//전체목록조회
	@Transactional(readOnly = true)

	public ResponseEntity<Message> users(final UserDetailsImpl userDetails) {

		Long userId = userDetails.getId();
		Member member = findMemberById(userId);
		List<Member> normalUsers = memberRepository.findMembersExcludingLikesAndDislikes(member.getId(), PageRequest.of(0, 1));
		List<MemberResponseDto> result = new ArrayList<>();
		for (Member members : normalUsers) {
			List<Long> favoriteList = memberFavoriteRepository.findByFavoriteList1(member.getId());
			result.add(new MemberResponseDto(members, favoriteList));
		}
		Message message = createMessage(result);

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	//사용자를 좋아요누른사람 목록좋회
	@Transactional(readOnly = true)

	public ResponseEntity<Message> likedUser(final UserDetailsImpl userDetails) {

		Long userId = userDetails.getId();

		Member member = findMemberById(userId);

		List<Member> likeMemberToUser = likeMemberRepository.findAllByLikedMember(member.getId(), PageRequest.of(0, 1));

		List<MemberResponseDto> result = new ArrayList<>();
		for (Member findMember : likeMemberToUser) {
			Member matchedMember = memberRepository.findById(findMember.getId()).orElseThrow(
				() -> new CustomException(NOT_FOUND_USER));
			List<Long> favoriteList = memberFavoriteRepository.findByFavoriteList1(matchedMember.getId());
			result.add(new MemberResponseDto(matchedMember, favoriteList));
		}
		Message message = createMessage(result);

		return new ResponseEntity<>(message, HttpStatus.OK);

	}

	private Member findMemberById(Long id) {
		return memberRepository.findById(id).orElseThrow(
			() -> new CustomException(NOT_FOUND_USER));
	}

	private Message createMessage(List<MemberResponseDto> result) {
		if (result.isEmpty()) {
			return Message.setSuccess(StatusEnum.OK, "조회 결과 없음");
		} else {
			//Collections.shuffle(result);
			return Message.setSuccess(StatusEnum.OK, "조회 성공", result.get(0));
		}

	}
}
