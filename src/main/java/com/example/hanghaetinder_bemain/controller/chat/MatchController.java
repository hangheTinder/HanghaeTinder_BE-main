package com.example.hanghaetinder_bemain.controller.chat;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.entity.Gender;
import com.example.hanghaetinder_bemain.entity.MatchMember;
import com.example.hanghaetinder_bemain.entity.Member;
import com.example.hanghaetinder_bemain.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.repository.MemberRepository;
import com.example.hanghaetinder_bemain.sevice.chat.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MatchController {

	private final MatchMemberRepository matchMemberRepository;
	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomService chatRoomService;

	@Transactional
	@PostMapping("/api/v1/matches")
	public ChatRoom match() {
		Member member = new Member("user1@test.com", "password1", "송혜교", Gender.FEMALE);
		Member matchedMember = new Member("user2@test.com", "password2", "강동원", Gender.MALE);
		memberRepository.save(member);
		memberRepository.save(matchedMember);


		String roomName = member.getNickname() + "," + matchedMember.getNickname();

		ChatRoom chatRoom = ChatRoom.create(roomName);
		MatchMember match = new MatchMember(member, matchedMember, chatRoom);
		matchMemberRepository.save(match);
		chatRoomRepository.save(chatRoom);

		return chatRoom;
	}


}
