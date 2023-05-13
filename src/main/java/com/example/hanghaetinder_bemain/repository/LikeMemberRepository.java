package com.example.hanghaetinder_bemain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.entity.LikeMember;
import com.example.hanghaetinder_bemain.entity.Member;

public interface LikeMemberRepository extends JpaRepository<LikeMember,Long> {

	List<LikeMember> findAllByMember(Member member);

	List<LikeMember> findAllByLikedMember(Member member);

}
