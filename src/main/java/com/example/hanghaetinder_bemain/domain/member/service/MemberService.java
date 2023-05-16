package com.example.hanghaetinder_bemain.domain.member.service;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage;
import com.example.hanghaetinder_bemain.domain.member.dto.request.LoginRequestDto;
import com.example.hanghaetinder_bemain.domain.member.dto.request.SignupRequestDto;
import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.LoginResponseDto;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.member.entity.Favorite;
import com.example.hanghaetinder_bemain.domain.member.entity.MatchMember;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.common.exception.CustomException;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberFavoriteRepository;
import com.example.hanghaetinder_bemain.domain.security.jwt.JwtUtil;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatMessageRepository;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.FavoriteRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;
import com.example.hanghaetinder_bemain.domain.member.util.AgeCalculator;
import com.example.hanghaetinder_bemain.domain.member.util.S3Uploader;
import java.util.ArrayList;
import java.util.Collections;


import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;
import com.example.hanghaetinder_bemain.domain.member.entity.DislikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.LikeMember;
import com.example.hanghaetinder_bemain.domain.member.repository.DislikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.LikeMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;

	private final FavoriteRepository favoriteRepository;
	private final PasswordEncoder passwordEncoder;
	private final S3Uploader s3Uploader;
	private final DislikeMemberRepository dislikeMemberRepository;
	private final LikeMemberRepository likeMemberRepository;
	private final MatchMemberRepository matchMemberRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberFavoriteRepository memberFavoriteRepository;




	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
		String userId = loginRequestDto.getUserId();
		String password = loginRequestDto.getPassword();

		Member member = memberRepository.findByUserId(userId).orElseThrow(
			() -> new CustomException(ResponseMessage.NOT_FOUND_USER));// 예외처리 해주기

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomException(ResponseMessage.NOT_Fail_USER);
		}

		String accessToken = jwtUtil.createAccessToken(member.getUserId());

		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		// System.out.println(accessToken);

		return new LoginResponseDto(member.getNickname());
	}

	// @Transactional(readOnly = true)
	// public void logout(HttpServletRequest request) {
	// 	String accessToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER).substring(7);
	//
	// 	if (accessToken != null && redisService.getValues(accessToken) != null) {
	// 		redisService.deleteValues(accessToken);
	// 	} else {
	// 		throw new CustomException(ResponseMessage.WRONG_ACCESS);
	// 	}
	// }

	//회원가입
	@Transactional
	public void signup(SignupRequestDto signupRequestDto) {

		//1.들어온값에 널이 포함되어있는지확인
		if (signupRequestDto.getUserId() == null ||
			signupRequestDto.getPassword() == null ||
			signupRequestDto.getNickname() == null ||
			signupRequestDto.getBirth() == null ||
			signupRequestDto.getGender() == null ||
			signupRequestDto.getImage() == null ) {
			throw new CustomException(ResponseMessage.WRONG_FORMAT);
		}
		//2. existsByUserId이미 존재하는지확인 존재한다면 true를반환해서 오류메시지반환
		if (memberRepository.existsByUserId(signupRequestDto.getUserId())) {
			throw new CustomException(ResponseMessage.ALREADY_ENROLLED_USER);
		}

		//3.signupRequestDto생성자로받는 member객체생성
		Member member = new Member(signupRequestDto);
		//4. password암호화
		String password = passwordEncoder.encode(signupRequestDto.getPassword());
		//5. 암호화한 비밀번호 세팅
		member.setPassword(password);
		//6.Favorite형태의 리스트생성해서 signupRequestDto에서 꺼내서 저장
		List<Favorite> favorites = favoriteRepository.findAllByFavoriteNameIn(signupRequestDto.getFavorites());
		//7. 저장
		member.setFavorites(favorites);


		try { // upload method 에서 발생하는 IOException 을 Customize 하기 위해 try-catch 사용
			String imgPath = s3Uploader.upload(signupRequestDto.getImage());

			member.setImg(imgPath);

		} catch (IOException e) {
			throw new CustomException(ResponseMessage.S3_ERROR);
		}

		int age = AgeCalculator.calculateAge(signupRequestDto.getBirth());

		member.setAge(age);

		memberRepository.save(member);


	}

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
			List<Long> favoriteList = memberFavoriteRepository.findByFavoriteList1(member.getId());
			result.add(new MemberResponseDto(members, favoriteList));
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
	}
	@Transactional
	public boolean isLikedByMe(Member member, Member likedMember) {

		// 현재 사용자가 좋아요를 누른 사용자의 목록을 가져옵니다.
		List<LikeMember> likesByMe = likeMemberRepository.findAllByMember(member);

		// 가져온 목록에서 나를 좋아요 누른 사용자가 있는지 확인합니다.
		for (LikeMember likeMember : likesByMe) {
			if (likeMember.getLikedMember().getId().equals(likedMember.getId())) {
				return true;
			}
		}
		return false;
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
			List<Long> favoriteList = memberFavoriteRepository.findByFavoriteList1(matchedMember.getId());
			result.add(new MemberResponseDto(matchedMember, favoriteList));
		}
		//6.랜덤으로섞는다
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
			List<Long> favoriteList = memberFavoriteRepository.findByFavoriteList1(matchedMember.getId());
			result.add(new MemberResponseDto(matchedMember, favoriteList));
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
