package com.example.hanghaetinder_bemain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.entity.MatchMember;
import com.example.hanghaetinder_bemain.entity.Member;

@Repository
public interface MatchMemberRepository extends JpaRepository<MatchMember, Long> {
	MatchMember findByMemberAndMatchMember(Member member, Member matchMember);
}
