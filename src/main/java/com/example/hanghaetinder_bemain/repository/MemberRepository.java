package com.example.hanghaetinder_bemain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanghaetinder_bemain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
