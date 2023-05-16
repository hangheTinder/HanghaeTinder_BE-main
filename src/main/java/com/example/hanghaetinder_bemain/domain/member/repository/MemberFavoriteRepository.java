package com.example.hanghaetinder_bemain.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.domain.member.entity.Favorite;
import com.example.hanghaetinder_bemain.domain.member.entity.MemberFavorite;

@Repository
public interface MemberFavoriteRepository extends JpaRepository<MemberFavorite, Long> {

	//데이터 처리속도가 느림 단순 favorite id정보만 처리하기위해서 프론트엔드단에서 처리
/*
	@Query("select f From MemberFavorite mf join fetch Favorite f on mf.favorite.id = f.id where mf.member.id = :id")
	List<Favorite> findByFavoriteList(@Param("id") Long id);
*/

	@Query("select mf.favorite.id From MemberFavorite mf where mf.member.id = :id")
	List<Long> findByFavoriteList1(@Param("id") Long id);

}
