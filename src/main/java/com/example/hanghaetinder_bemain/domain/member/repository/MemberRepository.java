package com.example.hanghaetinder_bemain.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUserId(String userId);

	boolean existsByUserId(String userId);

	@Query("SELECT m FROM Member m " +
		"WHERE NOT EXISTS (" +
		"    SELECT 1 FROM DislikeMember dm " +
		"    WHERE (dm.dislikeMember.id = :targetMember OR dm.member.id = :targetMember) " +
		"        AND (dm.dislikeMember.id = m.id OR dm.member.id = m.id)" +
		") " +
		"AND NOT EXISTS (" +
		"    SELECT 1 FROM LikeMember lm " +
		"    WHERE (lm.likedMember.id = :targetMember OR lm.member.id = :targetMember) " +
		"        AND (lm.likedMember.id = m.id OR lm.member.id = m.id)" +
		")")
	List<Member> findMembersExcludingLikesAndDislikes(@Param("targetMember") Long targetMember);


}
