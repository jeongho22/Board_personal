package com.example.dy.Repository;

import com.example.dy.Domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;

@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article,Long> {   // 관리대상 Aricle 저장, 대표값 저장 Long

    @Modifying // 변경쿼리에 사용
    @Query("UPDATE Article a SET a.view = a.view + 1 WHERE a.id = :id")
    void incrementViewCount(@Param("id") Long id);
    // 이 메소드를 사용할 때 Article 엔티티의 인스턴스는 변경되지 않습니다.
    // 즉, JPA의 엔티티 상태 관리(context) 밖에서 작업이 수행되기 때문에,
    // Article 엔티티의 @LastModifiedDate 어노테이션이 적용되는 modifiedAt 필드가 자동으로 업데이트되는 것을 방지할 수 있습니다.
    // 이는 특히 Article 엔티티의 다른 중요한 정보가 변경되지 않았음에도 불구하고
    // 단순히 조회수가 증가하는 경우에 modifiedAt 필드가 업데이트되는 것을 원치 않을 때 유용합니다.


    // 페이지 형식으로 Article 모든 정보 찾는다.
    Page<Article> findAll(Pageable pageable);
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);


}