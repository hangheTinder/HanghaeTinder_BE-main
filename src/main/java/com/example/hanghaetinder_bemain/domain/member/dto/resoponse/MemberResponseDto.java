package com.example.hanghaetinder_bemain.domain.member.dto.resoponse;

import java.util.ArrayList;
import java.util.List;

import com.example.hanghaetinder_bemain.domain.member.entity.Favorite;
import com.example.hanghaetinder_bemain.domain.member.entity.Gender;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {

	private Long id;
	private String userId;
	private String nickname;
	private int age;
	private Gender gender;

	private List<String> favorites;
	private String img;

	public MemberResponseDto(Member member) {
		this.id = member.getId();
		this.userId = member.getUserId();
		this.nickname = member.getNickname();
		this.age = member.getAge();
		if(member.getGender().equals(Gender.FEMALE))
		{
			this.gender = Gender.FEMALE;
		}
		else{
			this.gender = Gender.MALE;
		}

		List<String> favoriteNames = new ArrayList<>();
		for (Favorite favorite : member.getFavorites()) {
			favoriteNames.add(favorite.getFavoriteName());
		}
		this.img = member.getImg();
	}






}
