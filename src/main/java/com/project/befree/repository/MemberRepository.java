package com.project.befree.repository;

import com.project.befree.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberRepository extends JpaRepository<Member, String> {

    @Modifying
    @Query(value = "UPDATE Member SET email = :newId WHERE email = :oldId", nativeQuery = true)
    void updateMemberEmail(@Param("oldId") String oldId, @Param("newId") String newId);
}
