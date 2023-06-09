package com.example.hanghaetinder_bemain.domain.member.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hanghaetinder_bemain.domain.member.entity.LikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;

public interface LikeMemberRepository extends JpaRepository<LikeMember,Long> {

	@Query("SELECT distinct lm.likedMember from LikeMember lm where lm.member.id = :id")
	List<Member> findAllByMember(@Param("id") Long id);

	@Query("SELECT lm.member from LikeMember lm where lm.likedMember.id = :id group by lm.member.id ORDER BY RAND()")
	List<Member> findAllByLikedMember(@Param("id") Long id, Pageable pagealbe);

	@Modifying
	@Query("DELETE from LikeMember lm where lm.member = :member1 AND lm.likedMember = :member2")
	void deleteMember(@Param("member1") Member member1, @Param("member2") Member member2);

}
