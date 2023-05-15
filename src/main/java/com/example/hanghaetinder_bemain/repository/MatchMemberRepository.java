package com.example.hanghaetinder_bemain.repository;

import java.util.List;
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

	@Query("SELECT MM.chatRoom FROM MatchMember MM LEFT join MM.chatRoom.messages m WHERE MM.member.id = :id OR MM.matchedMember = :id GROUP BY MM.chatRoom ORDER BY m.createdAt DESC")
	List<ChatRoom> findMatchmember(@Param("id") Long id);
}
