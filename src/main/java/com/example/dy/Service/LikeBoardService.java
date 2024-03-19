package com.example.dy.Service;
import com.example.dy.Domain.Article;
import com.example.dy.Domain.LikeBoard;
import com.example.dy.Domain.User;
import com.example.dy.Dto.LikeBoardDto;
import com.example.dy.Repository.ArticleRepository;
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
public class LikeBoardService {
    private final LikeBoardRepository likeBoardRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public LikeBoardService(LikeBoardRepository likeBoardRepository,
                            ArticleRepository articleRepository,
                            UserRepository userRepository) {

        this.articleRepository = articleRepository;
        this.likeBoardRepository = likeBoardRepository;
        this.userRepository = userRepository;
    }

    //1. 특정 게시글에 좋아요를 누른 사용자 조회
//    public List<LikeBoardDto> findLikesByArticleId(Long articleId) {
//        return likeBoardRepository.findByArticleId(articleId).stream()
//                .map(LikeBoardDto::likeBoardDto) // LikeBoard 인스턴스를 LikeBoardDto로 변환
//                .collect(Collectors.toList());
//    }

    public List<LikeBoardDto> findLikesByArticleId(Long articleId) {

        List<LikeBoard> likes = likeBoardRepository.findByArticleId(articleId);
        List<LikeBoardDto> likeBoardDtos = new ArrayList<>();

        for (LikeBoard like : likes) {
            LikeBoardDto dto = LikeBoardDto.likeBoardDto(like);
            likeBoardDtos.add(dto);
        }
        return likeBoardDtos;
    }


    // 2.게시글에 눌린 좋아요 횟수
    @Transactional(readOnly = true)
    public Long countLikeByArticleId(Long articleId) {
        return likeBoardRepository.countByArticleId(articleId);
    }


    // 3. 게시글에 눌린 좋아요 상태 확인(버튼)
    public boolean isLikedByUser(Long articleId, Long userId) {
        return likeBoardRepository.existsByArticleIdAndUserId(articleId, userId);
    }


    // 4.사용자가 누른 좋아요 목록 조회 기능
    @Transactional(readOnly = true)
//    public List<LikeBoardDto> findLikesByUserId(Long userId) {
//        // 소프트 삭제되지 않은 게시물의 좋아요 조회
//        return likeBoardRepository.findActiveLikeByUserId(userId).stream()
//                .map(LikeBoardDto::likeBoardDto)
//                .collect(Collectors.toList());
//    }
    public List<LikeBoardDto> findLikesByUserId(Long userId){

        List<LikeBoard> likes= likeBoardRepository.findActiveLikeByUserId(userId);
        List<LikeBoardDto> likeBoardDtos =new ArrayList<>();

        for (LikeBoard like : likes){
            LikeBoardDto dto = LikeBoardDto.likeBoardDto(like);
            likeBoardDtos.add(dto);
        }

        return likeBoardDtos;
    }



    // 5.좋아요 생성,삭제 (토글)
    @Transactional
    public Optional<LikeBoardDto> toggleLike(Long articleId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id: " + articleId));

        // 5-1.소프트 삭제된 게시물인 경우 좋아요 생성 금지
        if (article.getDeletedAt() != null) {
            throw new IllegalStateException("Cannot like a deleted article");
        }

        // 5-2.좋아요 중복 체크
        Optional<LikeBoard> existingLike = likeBoardRepository.findByUserIdAndArticleId(userId, articleId);


        // 5-3.기존에 좋아요가 있으면, 삭제 후 빈값 Optional 반환
        if (existingLike.isPresent()) {
            likeBoardRepository.delete(existingLike.get());
            return Optional.empty();
            // 5-4.좋아요가 없으면 생성 후 저장 반환
        } else {
            LikeBoard newLike = new LikeBoard(user, article);
            LikeBoard savedLike = likeBoardRepository.save(newLike);
            return Optional.of(LikeBoardDto.likeBoardDto(savedLike));  // 생성된 좋아요의 DTO 반환
        }
    }


    // 6.좋아요 삭제
    @Transactional
    public void delete(Long LikeId) {
        LikeBoard like = likeBoardRepository.findById(LikeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid like Id:" + LikeId));
        likeBoardRepository.delete(like);
    }
}
