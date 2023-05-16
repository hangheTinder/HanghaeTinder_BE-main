package com.example.hanghaetinder_bemain.domain.member.entity;

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

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;

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
	@JoinColumn(name = "member1_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "member2_id")
	private Member matchedMember;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;

	public MatchMember(Member member, Member matchedMember, ChatRoom chatRoom) {
		this.member = member;
		this.matchedMember = matchedMember;
		this.chatRoom = chatRoom;
	}

}
