package com.example.hanghaetinder_bemain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	Favorite findByFavoriteName(String favoritename);

	List<Favorite> findAllByFavoriteNameIn(List<String> favoritenames);
}
