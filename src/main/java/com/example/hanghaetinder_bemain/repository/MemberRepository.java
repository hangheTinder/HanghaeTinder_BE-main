package com.example.hanghaetinder_bemain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.entity.Favorite;
import com.example.hanghaetinder_bemain.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUserId(String userId);

	boolean existsByUserId(String userId);


}
