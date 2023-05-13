package com.example.hanghaetinder_bemain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByNickname(String nickname);

}
