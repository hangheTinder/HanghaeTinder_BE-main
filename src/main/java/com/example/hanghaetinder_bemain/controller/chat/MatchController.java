package com.example.hanghaetinder_bemain.controller.chat;

import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MatchController {

	private final MatchMemberRepository matchMemberRepository;
	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomRepository chatRoomService;

	/*@PostMapping("/api/v1/matches")
	public ChatRoomDto match() {
		Member member = new Member("user1", "password1", "user1@test.com", Gender.FEMALE);
		Member matchedMember = new Member("user2", "password2", "user2@test.com", Gender.MALE);
		memberRepository.save(member);
		memberRepository.save(matchedMember);

		MatchMember match = new MatchMember(member, matchedMember);
		matchMemberRepository.save(match);


		return chatRoomService.createChatRoom(member, matchedMember);
	}

	@PostMapping("/api/v2/matches")
	public ChatRoomDto match1() {
		Member member = new Member("user3", "password1", "user1@test.com", Gender.FEMALE);
		Member matchedMember = new Member("user4", "password2", "user2@test.com", Gender.MALE);
		memberRepository.save(member);
		memberRepository.save(matchedMember);

		MatchMember match = new MatchMember(member, matchedMember);
		matchMemberRepository.save(match);


		return chatRoomService.createChatRoom(member, matchedMember);
	}*/

}
