package com.example.dy.Service;

import com.example.dy.Domain.Article;
import com.example.dy.Domain.Bookmark;
import com.example.dy.Domain.User;
import com.example.dy.Dto.BookmarkDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Repository.BookmarkRepository;
import com.example.dy.Repository.LikeBoardRepository;
import com.example.dy.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final LikeBoardRepository likeBoardRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           ArticleRepository articleRepository,
                           UserRepository userRepository,
                           LikeBoardRepository likeBoardRepository) {

        this.articleRepository = articleRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.likeBoardRepository = likeBoardRepository;
    }


    // 1.사용자 별 북마크 목록 조회 기능
    @Transactional(readOnly = true)
//    public List<BookmarkDto> findBookmarksByUserId(Long userId) {
//        // 소프트 삭제되지 않은 게시물의 북마크만 조회
//        return bookmarkRepository.findActiveBookmarksByUserId(userId).
//                stream()
//                .map(BookmarkDto::bookmarkDto)
//                .collect(Collectors.toList());
//    }
    public List<BookmarkDto> findBookmarksByUserId(Long userId) {

        List<Bookmark> bookmarks = bookmarkRepository.findActiveBookmarksByUserId(userId);
        List<BookmarkDto> bookmarkDtos = new ArrayList<>();

        for (Bookmark bookmark : bookmarks){
            BookmarkDto dto = BookmarkDto.bookmarkDto(bookmark);
            bookmarkDtos.add(dto);
        }

        return bookmarkDtos;
    }



    // 2.북마크 생성,삭제 (토글)
    @Transactional
    public Optional<BookmarkDto> toggleBookmark(Long articleId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id: " + articleId));

        // 2-1.소프트 삭제된 게시물인 경우 북마크 생성 금지
        if (article.getDeletedAt() != null) {
            throw new IllegalStateException("Cannot bookmark a deleted article");
        }

        // 2-2.북마크 중복 체크
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserIdAndArticleId(userId, articleId);


        // 2-3.기존에 북마크가 있으면, 삭제 후 빈값 Optional 반환
        if (existingBookmark.isPresent()) {
            bookmarkRepository.delete(existingBookmark.get());
            return Optional.empty();
        // 2-4.북마크가 없으면 생성 후 저장 반환
        } else {
            Bookmark newBookmark = new Bookmark(user, article);
            Bookmark savedBookmark = bookmarkRepository.save(newBookmark);
            return Optional.of(BookmarkDto.bookmarkDto(savedBookmark));  // 생성된 북마크의 DTO 반환
        }
    }


    // 3.북마크 삭제
    @Transactional
    public void delete(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bookmark Id:" + bookmarkId));
        bookmarkRepository.delete(bookmark);
    }

}
