package com.example.hanghaetinder_bemain.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.domain.member.entity.DislikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;

public interface DislikeMemberRepository extends JpaRepository<DislikeMember,Long> {

	List<DislikeMember> findAllByMember(Member member);

	List<DislikeMember> findAllByDislikeMember(Member member);
}
