package com.example.hanghaetinder_bemain.domain.member.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.example.hanghaetinder_bemain.domain.member.dto.request.SignupRequestDto;
import com.example.hanghaetinder_bemain.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@Entity
public class Member extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = true)
	private LocalDate birth;

	@Column(nullable = true)
	private int age;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = true)
	private String img;


	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
		name = "member_favorite",
		joinColumns = @JoinColumn(name = "member_id"),
		inverseJoinColumns = @JoinColumn(name = "favorite_id"))
	private List<Favorite> favorites;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "member")
	private Set<LikeMember> likeMembers = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "member")
	private Set<DislikeMember> dislikeMember = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "member")
	private Set<MatchMember> matchMember = new HashSet<>();

	public Member(SignupRequestDto signupRequestDto){
		this.userId = signupRequestDto.getUserId();
		this.nickname = signupRequestDto.getNickname();
		this.birth = signupRequestDto.getBirth();
		this.gender =signupRequestDto.getGender();
	}


	public Member(String userId, String password, String nickname, Gender gender) {
		this.userId = userId;
		this.password = password;
		this.nickname = nickname;
		this.gender = gender;
	}


}
