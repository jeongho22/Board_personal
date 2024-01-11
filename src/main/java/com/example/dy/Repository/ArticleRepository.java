package com.example.dy.Repository;

import com.example.dy.Domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;

@RepositoryRestResource                                                    // hal api 어노테이션 추가시켜야함...
public interface ArticleRepository extends CrudRepository<Article,Long> {   // 관리대상 Aricle 저장, 대표값 저장 Long
    @Override
    ArrayList<Article> findAll();
}
