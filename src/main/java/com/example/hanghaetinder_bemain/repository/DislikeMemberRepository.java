package com.example.hanghaetinder_bemain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.entity.DislikeMember;
import com.example.hanghaetinder_bemain.entity.Member;

public interface DislikeMemberRepository extends JpaRepository<DislikeMember,Long> {

	List<DislikeMember> findAllByMember(Member member);

	List<DislikeMember> findAllByDislikeMember(Member member);
}
