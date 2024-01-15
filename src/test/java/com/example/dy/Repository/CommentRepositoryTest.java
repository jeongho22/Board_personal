package com.example.dy.Repository;

import com.example.dy.Domain.Article;
import com.example.dy.Domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest                    //repository test 진행할때는 datajpatest가 더 적합
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Test
    @DisplayName("특정 게시글의 모든 댓글 조회")
    void findByArticleId() {
        /* Case 1: 1번 게시글의 모든 댓글 조회 */
        {
            // 준비
            Long articleId = 1L;
            // 수행
            List<Comment> comments = commentRepository.findByArticleId(articleId);
            // 예상
            Article article = new Article(1L, "헉", "헉");
            Comment a = new Comment(17L, article, "김정호", "1");
            Comment b = new Comment(18L, article, "권유정", "2");
            Comment c = new Comment(19L, article, "코코", "3");
            List<Comment> expected = Arrays.asList(a, b, c);
            // 검증
            assertEquals(expected.toString(), comments.toString(), "1번 글의 모든 댓글을 출력!");
        }
        /* Case 2: 1번 게시글의 모든 댓글 조회 */
        {
            // 준비
            Long articleId = 19L;
            // 수행
            List<Comment> comments = commentRepository.findByArticleId(articleId);
            // 예상
            Article article = new Article(19L, "가가가가", "1111");
            List<Comment> expected = Arrays.asList();
            // 검증
            assertEquals(expected.toString(), comments.toString(), "19번 글은 댓글이 없음");
        }
    }
    @Test
    @DisplayName("특정 닉네임의 모든 댓글 조회")
    void findByNickname() {
        /* Case 1: "Park"의 모든 댓글 조회 */
        {
            // 준비
            String nickname = "김정호";
            // 수행
            List<Comment> comments = commentRepository.findByNickname(nickname);
            // 예상
            Comment a = new Comment(1L, new Article(17L, "헉", "헉"), nickname, "1");
            Comment b = new Comment(15L, new Article(20L, "데이터 전송", "전송ㅋㅋㅋ"), nickname, "2");
            Comment c = new Comment(19L, new Article(21L, "유다이렉트", "그런가k"), nickname, "3");
            List<Comment> expected = Arrays.asList(a, b, c);
            // 검증
            assertEquals(expected.toString(), comments.toString(), "김정호의 모든 댓글을 출력!");
        }
    }
}