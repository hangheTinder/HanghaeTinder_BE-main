package com.example.hanghaetinder_bemain.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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

	@Column(nullable = false)
	private LocalDate birth;

	@Column(nullable = false)
	private int age;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String img;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "member_favorite",
		joinColumns = @JoinColumn(name = "member_id"),
		inverseJoinColumns = @JoinColumn(name = "favorite_id")
	)
	private Set<Favorite> favorites = new HashSet<>();

	@OneToMany(mappedBy = "member")
	private Set<LikeMember> likeMembers = new HashSet<>();

	@OneToMany(mappedBy = "member")
	private Set<DislikeMember> dislikeMember = new HashSet<>();

	@OneToMany(mappedBy = "member")
	private Set<MatchMember> matchMember = new HashSet<>();

}
