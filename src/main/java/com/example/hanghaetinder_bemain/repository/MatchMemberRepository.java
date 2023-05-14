package com.example.hanghaetinder_bemain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.entity.MatchMember;
import com.example.hanghaetinder_bemain.entity.Member;

@Repository
public interface MatchMemberRepository extends JpaRepository<MatchMember, Long> {
	MatchMember findByMemberAndMatchedMember(Member member, Member matchMember);

	@Query("SELECT distinct MM.id From MatchMember MM Where MM.member.id = :id or  MM.matchedMember = :id")
	Optional<MatchMember> findMatchmember(@Param("id") Long id);
}
