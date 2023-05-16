package com.example.hanghaetinder_bemain.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.domain.member.entity.LikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;

public interface LikeMemberRepository extends JpaRepository<LikeMember,Long> {

	List<LikeMember> findAllByMember(Member member);

	List<LikeMember> findAllByLikedMember(Member member);

}
