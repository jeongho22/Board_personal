package com.example.dy.repository;


import com.example.dy.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer > { //첫번째 자리는 엔티티 들어가는곳 , 두번째 자리는 컬럼의 타입 지정

    Page<Board> findByNameContaining(String searchKeyword, Pageable pageable);
    Page<Board> findByJobContaining(String searchKeyword, Pageable pageable);
    Page<Board> findByNameContainingOrJobContaining(String name, String job, Pageable pageable);

}


// 정보를 저장해놓는곳