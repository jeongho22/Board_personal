package com.example.dy.repository;

import com.example.dy.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByNameContaining(String name, Pageable pageable); // 이름 포함 검색어 메소드
    Page<Board> findByJobContaining(String job, Pageable pageable); // 가격 포함 검색어 메소드



}
