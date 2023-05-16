package com.example.hanghaetinder_bemain.domain.member.dto.resoponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.hanghaetinder_bemain.domain.member.entity.Favorite;
import com.example.hanghaetinder_bemain.domain.member.entity.Gender;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberFavoriteRepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Component
@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDto {

	private Long id;
	private String userId;
	private String nickname;
	private int age;
	private Gender gender;
	private List<Long> favorites;
	private String img;

	public MemberResponseDto(Member member, List<Long> favoriteList) {

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
		this.favorites = favoriteList;
		this.img = member.getImg();
	}
}
