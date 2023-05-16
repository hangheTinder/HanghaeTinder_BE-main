package com.example.hanghaetinder_bemain.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.domain.member.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	Favorite findByFavoriteName(String favoritename);

	boolean existsByFavoriteName(String name);

	List<Favorite> findAllByFavoriteNameIn(List<String> favoritenames);
}
