package com.project.befree.repository;

import com.project.befree.domain.Place;
import com.project.befree.domain.Trip;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    // 이메일로 리스트 조회
    @Query("select t from Trip t where t.member.email = :email")
    List<Trip> findAllByEmail(@Param("email") String email);

    List<Trip> findAllBySharedIsTrue();

}
