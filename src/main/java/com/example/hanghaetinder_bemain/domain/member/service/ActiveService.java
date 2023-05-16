package com.example.hanghaetinder_bemain.domain.member.service;

import static com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatMessageRepository;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.domain.common.exception.CustomException;
import com.example.hanghaetinder_bemain.domain.member.entity.DislikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.LikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.MatchMember;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.member.repository.DislikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.LikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.member.util.StatusEnum;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActiveService {

	private final MemberRepository memberRepository;
	private final LikeMemberRepository likeMemberRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MatchMemberRepository matchMemberRepository;
	private final DislikeMemberRepository dislikeMemberRepository;
	private final ChatRoomRepository chatRoomRepository;
	@Transactional

	public ResponseEntity<Message> likeToUsers(final Long userIdToLike,final UserDetailsImpl userDetails) {
		//1. 사용자의 아이디를꺼낸다
		Long userId = userDetails.getId();
		//2. 좋아요를 눌려진 사용자를 찾는다
		Member member = findMemberById(userId);
		//3. 좋아요를 (사용자에게)눌려진
		Member memberToLike = findMemberById(userIdToLike);
		//4. LikeMember에 넣는다.
		LikeMember likeMember = new LikeMember();
		likeMember.setMember(member);
		likeMember.setLikedMember(memberToLike);
		likeMemberRepository.save(likeMember);

		boolean isLikedByMe = isLikedByMe(memberToLike, member);
		if (isLikedByMe) {
			String roomName = member.getNickname() + "님과 " + memberToLike.getNickname()+"의 채팅방 ";
			ChatRoom chatRoom = ChatRoom.create(roomName);
			MatchMember match = new MatchMember(member, memberToLike, chatRoom);
			ChatMessage chatMessage = new ChatMessage(ChatMessage.MessageType.ENTER, chatRoom.getRoomId(), "알림", "채팅방이 생성되었습니다.",
				new Date(), chatRoom);
			chatMessageRepository.save(chatMessage);
			matchMemberRepository.save(match);
			chatRoomRepository.save(chatRoom);
		}
		Message message = Message.setSuccess(StatusEnum.OK, "좋아요 요청 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);




	}
	@Transactional
	public boolean isLikedByMe(Member member, Member likedMember) {

		// 현재 사용자가 좋아요를 누른 사용자의 목록을 가져옵니다.
		List<Member> likesByMe = likeMemberRepository.findAllByMember(member.getId());

		// 가져온 목록에서 나를 좋아요 누른 사용자가 있는지 확인합니다.
		for (Member likeMember : likesByMe) {
			if (likeMember.getId().equals(likedMember.getId())) {
				return true;
			}
		}
		return false;
	}


	//싫어요 를 눌렀을때
	@Transactional
	public ResponseEntity<Message> dislikeToUsers(final Long userIdToDislike,final UserDetailsImpl userDetails) {
		//1.사용자의 아이디를 꺼내온다.
		Long userId = userDetails.getId();
		//2. 싫어요를 누르는 사용자를 찾는다,
		Member member = findMemberById(userId);
		//3. 싫어요를 당하는 사용차를 찾는다.
		Member memberToDislike = findMemberById(userIdToDislike);
		//4. DisLikeMember에 저장한다
		DislikeMember dislikeMember = new DislikeMember();
		dislikeMember.setMember(member);
		dislikeMember.setDislikeMember(memberToDislike);
		dislikeMemberRepository.save(dislikeMember);

		Message message = Message.setSuccess(StatusEnum.OK, "싫어요 요청 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	public Member findMemberById(Long id){
		return memberRepository.findById(id).orElseThrow(
			() -> new CustomException(NOT_FOUND_USER));
	}
}
