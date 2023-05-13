package com.example.hanghaetinder_bemain.entity;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "match_member")
public class MatchMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "match_member_id")
	private Member matchMember;

/*	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;*/

/*	public MatchMember(Member member, Member matchedMember, ChatRoom chatRoom) {
		this.member = member;
		this.matchMember = matchedMember;
		this.chatRoom = chatRoom;
	}

	// getter, setter 메소드
	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}*/

	public MatchMember(Member member, Member matchedMember) {
		this.member = member;
		this.matchMember = matchedMember;
	}

}
