package com.example.hanghaetinder_bemain.sevice;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanghaetinder_bemain.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.entity.DislikeMember;
import com.example.hanghaetinder_bemain.entity.LikeMember;
import com.example.hanghaetinder_bemain.entity.Member;
import com.example.hanghaetinder_bemain.repository.DislikeMemberRepository;
import com.example.hanghaetinder_bemain.repository.LikeMemberRepository;
import com.example.hanghaetinder_bemain.repository.MemberRepository;
import com.example.hanghaetinder_bemain.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final DislikeMemberRepository dislikeMemberRepository;
	private final LikeMemberRepository likeMemberRepository;


	/**전체목록조회
	 *내가 좋아요 or 싫어요 를 눌렀거나
	 * 나에게 좋아요 or 싫어요 를 누른사람을 제외한다.
	 */
	@Transactional(readOnly = true)
	public List<MemberResponseDto> users(UserDetailsImpl userDetails) {
		//1.사용자 아이디를꺼낸다
		Long userId = userDetails.getId();
		//2.사용자를 멤버객체에넣고
		Member member = findMemberById(userId);
		//3. 싫어요를 누르거나 눌려진
		List<Long> dislikesUserId = dislikeUser(member);
		//4. 좋아요를 누르거나 눌려진
		List<Long> likesUserId = likeUser(member);

		//5. 3번과 4번을포함 하지 않는 일반유저를 찾는다
		List<Member> normalUsers = new ArrayList<>();
		for (Member user : memberRepository.findAll()) {
			if (!dislikesUserId.contains(user.getId()) && !likesUserId.contains(user.getId())&& !user.getId().equals(userId)) {
				normalUsers.add(user);
			}
		}
		List<MemberResponseDto> result = new ArrayList<>();
		for (Member members : normalUsers) {
			result.add(new MemberResponseDto(members));

		}
		//7. 리스트를 랜덤하게 섞는다
		Collections.shuffle(result);
		return result;
	}
	//하트 를 눌렀을때
	@Transactional
	public void likeToUsers(Long userIdToLike, UserDetailsImpl userDetails) {
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
	}
	//싫어요 를 눌렀을때
	@Transactional
	public void dislikeToUsers(Long userIdToDislike, UserDetailsImpl userDetails) {
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

	}
	//사용자를 좋아요누른사람 목록
	@Transactional(readOnly = true)
	public List<MemberResponseDto> likedUser(UserDetailsImpl userDetails){
		//1.사용자 아이디를꺼낸다
		Long userId = userDetails.getId();

		//2.사용자를 멤버객체에넣고
		Member member = findMemberById(userId);

		//3. 나를 좋아요 누른사람을찾는다
		List<LikeMember> likeMemberToUser = likeMemberRepository.findAllByLikedMember(member);

		//4. 나를누른사람의 user id 추출
		List<Long> likeByUserIds = new ArrayList<>();
		for (LikeMember like : likeMemberToUser) {
			likeByUserIds.add(like.getLikedMember().getId());
		}
		//5. 반환값
		List<MemberResponseDto> result = new ArrayList<>();
		for (Long id : likeByUserIds) {
			Member matchedMember = memberRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("사용자를 찾을수 없습니다."));
			result.add(new MemberResponseDto(matchedMember));
		}
		Collections.shuffle(result);
		return result;

	}
	//서로 좋아요를 눌러 매칭된 목록조회(수정필요)매치드로 집어넣기
	@Transactional(readOnly = true)
	public List<MemberResponseDto> matched(UserDetailsImpl userDetails) {

		//1.사용자 아이디를꺼낸다
		Long userId = userDetails.getId();

		//2.사용자를 멤버객체에넣고
		Member member = findMemberById(userId);

		//3. 내가 좋아요를,나에게 좋아요를  누른사람을찾는다
		List<Long> likesUserId = likeUser(member);

		//4. 교집합 찾기
		List<Long> matchedUserIds = new ArrayList<>(likesUserId);
		matchedUserIds.retainAll(likesUserId);

		//5. 반환값
		List<MemberResponseDto> result = new ArrayList<>();
		for (Long id : matchedUserIds) {
			Member matchedMember = memberRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Invalid user id: " + id));
			result.add(new MemberResponseDto(matchedMember));
		}
		//이걸로 채팅방 목록을만들면될듯
		return result;
	}
	//채팅룸생성(보완필요)
	@Transactional(readOnly = true)
	public List<ChatRoom> getChatRooms(List<MemberResponseDto> matchedUsers, UserDetailsImpl userDetails) {
		//1. 채팅룸 리스트 생성
		List<ChatRoom> chatRooms = new ArrayList<>();


		return chatRooms;
	}

	public Member findMemberById(Long id){
		return memberRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("사용자를 찾을수가 없습니다"));
	}

	private List<Long> dislikeUser(Member member){
		//3. 내가 싫어요를 누른사람과
		List<DislikeMember> dislikesByUser = dislikeMemberRepository.findAllByMember(member);
		//4. 나를 싫어요 누른사람을 담는다
		List<DislikeMember> dislikesToUser = dislikeMemberRepository.findAllByDislikeMember(member);
		//7. 3번과 4번을 충족하는 유저id를 한곳에 넣어주고
		List<Long> dislikeUserIds = new ArrayList<>();
		for (DislikeMember dislike : dislikesByUser) {
			dislikeUserIds.add(dislike.getDislikeMember().getId());
		}
		for (DislikeMember dislike : dislikesToUser) {
			dislikeUserIds.add(dislike.getDislikeMember().getId());
		}
		return dislikeUserIds;
	}

	private List<Long> likeUser(Member member){
		//내가 좋아요를 누른사람
		List<LikeMember> likesByUser = likeMemberRepository.findAllByMember(member);
		//나를 좋아요를 누른사람
		List<LikeMember> likesToUser = likeMemberRepository.findAllByLikedMember(member);
		List<Long> likeUserIds = new ArrayList<>();
		for (LikeMember like : likesByUser) {
			likeUserIds.add(like.getLikedMember().getId());
		}
		for (LikeMember like : likesToUser) {
			likeUserIds.add(like.getLikedMember().getId());
		}
		return likeUserIds;
	}

}
