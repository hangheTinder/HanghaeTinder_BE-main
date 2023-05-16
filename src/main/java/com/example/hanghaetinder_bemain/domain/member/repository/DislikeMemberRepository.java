package com.example.hanghaetinder_bemain.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hanghaetinder_bemain.domain.member.entity.DislikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;

public interface DislikeMemberRepository extends JpaRepository<DislikeMember,Long> {

	List<DislikeMember> findAllByMember(Member member);

	@Query("SELECT distinct dm from DislikeMember dm where dm.dislikeMember.id = :id")
	List<DislikeMember> findAllByDislikeMember(@Param("id") Long id);
}
