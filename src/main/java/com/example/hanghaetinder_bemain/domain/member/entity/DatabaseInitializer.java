package com.example.hanghaetinder_bemain.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.hanghaetinder_bemain.domain.member.repository.FavoriteRepository;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class DatabaseInitializer {

	private FavoriteRepository favoriteRepository;

	//java.lang.NullPointerException 피해가기위해서
	@Autowired
	public DatabaseInitializer(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}

	List<String> favoriteList = new ArrayList<>() {{
		add("workout");
		add("game");
		add("coding");
		add("traveling");
		add("music");
		add("movie");
		add("cooking");
		add("pet");
		add("book");
		add("art");
	}};
	@PostConstruct
	public void initializeData() {

		for (String favoriteName : favoriteList) {
			if (!favoriteRepository.existsByFavoriteName(favoriteName)) {
				Favorite favorite = new Favorite(favoriteName);
				favoriteRepository.save(favorite);
			}
		}
	}
}