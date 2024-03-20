package com.example.dy.Service;
import com.example.dy.Domain.*;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Domain.constant.SearchType;
import com.example.dy.Dto.ArticleDto;
import com.example.dy.Dto.OAuth2.CustomOAuth2UserDto;
import com.example.dy.Dto.Login.UserRequestDto;
import com.example.dy.Dto.Login.UserResponseDto;
import com.example.dy.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final LikeBoardRepository likeBoardRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleService articleService;
    private final BookmarkService bookmarkService;
    private final CommentService commentService;


    //생성자 생성 (o) 필드 주입 x
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ArticleRepository articleRepository,
                       @Lazy ArticleService articleService,
                       BookmarkRepository bookmarkRepository,
                       BookmarkService bookmarkService,
                       CommentRepository commentRepository,
                       @Lazy CommentService commentService,
                       LikeBoardRepository likeBoardRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkService = bookmarkService;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.likeBoardRepository = likeBoardRepository;
    }

    @Transactional
    // 1-0. 회원 가입 메서드(post)
    public UserResponseDto register(UserRequestDto registerRequestDto) {

        boolean isUser= userRepository.findByEmail(registerRequestDto.getEmail()).isPresent();
        log.info("0. 유저 존재 : {}",isUser); // true false

        if (isUser) {
            throw new IllegalStateException("이미 존재 하는 아이디 입니다.");
        }
        log.info("1. 회원 가입 과정 requestDto 요청 전 : {}",registerRequestDto);

        //1.회원 가입 할때 요청 되는 3가지를 request 요청을 보냄. -> Dto를 -> entity로 변환
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());


        if (!isPasswordValid(registerRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 영문 대소문자/숫자/특수문자 중 2가지 이상 조합, 10~16자로 설정해야 합니다.");
        }

        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);        // 기본 Role을 USER로 설정
        user.setLoginType("NORMAL"); // 로그인 유형 설정

        //2.User 안에 dto 로 생성 되어진 객체를 저장
        User saved = userRepository.save(user);
        log.info("2. 회원 가입 과정 requestDto 요청 후: {}",saved);

        //3. ResponseDto 변환
        return UserResponseDto.fromEntity(saved);
    }


    // 1-1. 아이디 검증(중복 확인)
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }



    // 1-2. 비밀번호 검증 로직 구현
    private boolean isPasswordValid(String password) {
        int validCriteria = 0;

        if (password.matches(".*[a-zA-Z].*")) validCriteria++; // 영문자 포함
        if (password.matches(".*[0-9].*")) validCriteria++; // 숫자 포함
        if (password.matches(".*[!@#$%^&*()].*")) validCriteria++; // 특수문자 포함

        return validCriteria >= 2; // 2가지 이상의 조건을 만족해야 true 반환
    }




    // 2. 유저 아이디,이름 조회 서비스(get)

    public Page<UserResponseDto> findAllUsers(String searchType, String searchKeyword, Pageable pageable) {
        Page<User> users ;

        // searchType이 null이거나 빈 문자열일 경우 기본값 "ALL"을 사용
        String effectiveSearchType = (searchType == null || searchType.isBlank()) ? "ALL" : searchType.toUpperCase();

        if (searchKeyword == null || searchKeyword.isBlank()) {
            users = userRepository.findAll(pageable);
        }
        else {
            SearchType type = SearchType.valueOf(effectiveSearchType);

            users = switch (type) {
                case ALL -> userRepository.findByUsernameContainingOrEmailContaining(searchKeyword, searchKeyword, pageable);
                case TITLE -> userRepository.findByUsernameContaining(searchKeyword, pageable);
                case CONTENT -> userRepository.findByEmailContaining(searchKeyword, pageable);
                default -> Page.empty(pageable); // 기본값으로 비어있는 페이지를 반환
            };
        }

        // Page<Article>을 Page<ArticleDto>로 변환
        return users.map(UserResponseDto::fromEntity);
    }

    // 3. 수정
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto dto) {

        // 1.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기

        // 2.유저 조회 및 예외 발생
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정 실패! 대상 없습니다."));


        // 3.현재 로그인한 사용자 비교
        if (!(user.equals(currentUser))) {
            throw new IllegalStateException("조회된 사용자 != 현재 사용자");
        }

        // 4. 수정

        // 로그인 NORMAL
        if ("NORMAL".equals(currentUser.getLoginType())) {
            // 사용자 이름 패스워드: 패스워드 입력란이 비어 있지 않은 경우 에만 실행
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
              // 유효성이 통과 하지 않으면 예외처리,
                if (!isPasswordValid(dto.getPassword())) {
                    throw new IllegalArgumentException("비밀번호는 영문 대소문자/숫자/특수문자 중 2가지 이상 조합, 10~16자로 설정해야 합니다.");
                }
              // 유효성이 통과 하면 비밀번호 변경
                currentUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
                // 로그인 SOCIAL &&
                // 메서드를 통해 가져온 비밀번호 값이 null아니다 &&
                // 비밀번호 문자열이 비어있지 않다.
        } else if (("KAKAO SOCIAL".equals(currentUser.getLoginType()) || "GOOGLE SOCIAL".equals(currentUser.getLoginType()))
                            &&  dto.getPassword() != null
                            && !dto.getPassword().isEmpty()) {
            throw new IllegalStateException("소셜 로그인 사용자는 비밀번호를 변경할 수 없습니다.");
        }

        // 사용자 이름 변경: 이름 입력란이 비어 있지 않은 경우에만 실행
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            currentUser.setUsername(dto.getUsername());
        }
        // 5.DB로 갱신
        User updated = userRepository.save(currentUser);


        // 6.댓글 엔티티를 DTO로 변환 및 반환
        return UserResponseDto.fromEntity(updated);
    }


    // 4. 유저 아이디 탈퇴(delete)
    @Transactional
    public UserResponseDto delete(Long id) {

        // 1.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기
        // 2.유저 조회(및 예외 발생)
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저 삭제 실패! 대상이 없습니다."));

        // 3.유저와 현재 로그인한 유저 비교
        if (!(currentUser.getRole() == Role.ADMIN) && !user.equals(currentUser)) {
            throw new IllegalStateException("유저 삭제 실패! 자신의 아이디만 삭제 할 수 있습니다.");
        }

        // 4.유저와 관련된 게시글 삭제 처리(1)
        List<Article> articles = articleRepository.findByUserId(id);
        articles.forEach(article -> articleService.delete(article.getId())); // ArticleService의 delete 메서드 호출

        // 5. 유저와 관련된 모든 댓글을 삭제 처리(2)
        List<Comment> comments = commentRepository.findByUserId(id);
        comments.forEach(comment-> commentService.delete(comment.getId()));

        // 6. 유저와 관련된  북마크 삭제 처리(3)
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(id);
        bookmarks.forEach(bookmark -> bookmarkService.delete(bookmark.getId()));

        // 7. 유저와 관련된  좋아요 삭제 처리(4)
        List<LikeBoard> likeBoards = likeBoardRepository.findByUserId(id);
        likeBoards.forEach(likeBoard -> bookmarkService.delete(likeBoard.getId()));

        // 8.유저의 deletedAt을 현재 시간으로 설정하고 저장합니다.
        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(now);
        userRepository.save(user);

        // 9.이메일 변경을 별도의 메서드로 분리
        updateUserEmail(user);

        // 10.유저 삭제
        userRepository.delete(user);

        // 11.삭제 댓글을 DTO로 반환
        return UserResponseDto.fromEntity(user);
    }


    // 4. 즉시 변경 저장
    @Transactional
    public void updateUserEmail(User user) {
        String uniqueEmail = user.getEmail() + "_Deactivated_" + System.currentTimeMillis();
        user.setEmail(uniqueEmail);
        userRepository.saveAndFlush(user); // 변경사항 저장 및 즉시 플러시
    }


    //5. 현재 아이디 판별(일반 ,카카오)
    public User getCurrentUser() {

        //세션 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        // A instanceof B :
        // 검사할 객체 A
        // 객체가 속할거같은 클래스의 이름 B

            // OAuth2 로그인 처리
        if (authentication.getPrincipal() instanceof CustomOAuth2UserDto) {
            CustomOAuth2UserDto customOAuth2UserDto = (CustomOAuth2UserDto) authentication.getPrincipal();

            log.info(" 1: {}", customOAuth2UserDto);

            String email = customOAuth2UserDto.getName();

            log.info(" 2: {}",email);

            User currentUserOAuth2 = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

            return currentUserOAuth2;


        } else {
            // 일반 로그인 처리
            String email = authentication.getName(); // 일반 로그인 시 이메일

            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));


            return currentUser;
        }
    }

}