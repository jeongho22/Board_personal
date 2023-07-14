package com.example.dy.repository;


import com.example.dy.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository // 이 인터페이스가 "Repository"(데이터 액세스 오브젝트)라는 것을 나타냅니다. 주로 Spring Data JPA에서 DAO를 표시하는 데 사용됩니다.
public interface BoardRepository extends JpaRepository<Board,Integer > {
    // 이 인터페이스는 JpaRepository를 확장하므로 데이터베이스 액세스 및 조작을 위한 Spring Data JPA의 메소드를 사용할 수 있습니다.
    // 두 매개변수는 'Board'와 'Integer'인데,
    // 'Board'는 이 레포지토리와 연결된 엔티티 클래스이고,
    // 'Integer'는 'Board' 엔티티의 기본 키 유형입니다.

    Page<Board> findByNameContaining(String searchKeyword, Pageable pageable);
    // 이 메서드는 Spring Data JPA가 자동으로 구현하는 사용자 정의 메서드입니다.
    // 제공된 'searchKeyword'를 포함하는 'name' 속성을 가진 'Board' 엔티티의 Page를 반환합니다.
    // 'Pageable' 매개변수는 페이지 정보(어떤 페이지를 검색하고 페이지당 레코드 수 등)를 제공하는 데 사용됩니다.


    Page<Board> findByJobContaining(String searchKeyword, Pageable pageable);
    // 위 메서드와 비슷하게, 이 메서드는 제공된 'searchKeyword'를 포함하는 'job' 속성을 가진 'Board' 엔티티의 Page를 반환합니다.
    // 또한 'Pageable'를 페이징에 사용합니다.

    Page<Board> findByNameContainingOrJobContaining(String name, String job, Pageable pageable);
    // 이 메서드는 'name' 문자열을 포함하는 'name' 속성이 있거나 'job' 문자열을 포함하는 'job' 속성을 가진 'Board' 엔티티의 Page를 반환합니다.
    // 다른 메서드처럼 'Pageable'을 페이징에 사용합니다.
}



// 정보를 저장해놓는곳