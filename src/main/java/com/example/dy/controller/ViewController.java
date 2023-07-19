package com.example.dy.controller;


import com.example.dy.entity.Board;
import com.example.dy.entity.Comment;
import com.example.dy.serivce.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;


@Controller  // 이 클래스가 Spring MVC의 Controller임을 나타내는 어노테이션입니다.
public class ViewController {

    @GetMapping("/write")  // GET 요청을 "/write" URL로 매핑하는 어노테이션입니다.
    public String boardwriteform(){
        return "boardwrite";  // "boardwrite"라는 이름의 뷰(view)를 반환합니다.
    }

    @Autowired  // Spring 컨테이너에서 BoardService 타입의 자동으로 주입해줍니다.
    private BoardService boardService;

    @PostMapping("/boardwriting")
    public String boardwrite(Board board, Model model, Principal principal) { // Principal 객체를 파라미터로 추가합니다.
        board.setTime(LocalDateTime.now());
        boardService.write(board, principal.getName());  // 로그인한 사용자의 이름을 함께 넘겨줍니다.
        model.addAttribute("message","글작성이 완료 되었습니다.");
        model.addAttribute("url","/list");
        return "message";
    }





    @GetMapping("/list")  // GET 요청을 "/list" URL로 매핑하는 어노테이션입니다.


    public String boardList(Model model,
                            @PageableDefault(page =0, size =10, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword, String searchType, Principal principal) {
        // 페이지 정보와 검색 키워드, 검색 유형을 인자로 받습니다.

        Page<Board> list = null;  // 게시글 리스트를 담을 변수를 선언합니다.


        // 검색 키워드가 없으면 모든 게시글을, 있으면 해당 키워드로 검색하여 게시글을 가져옵니다.
        if(searchKeyword == null){
            list = boardService.boardList(pageable);
        } else {
            switch (searchType) {
                case "name":
                    list = boardService.boardSearchByName(searchKeyword, pageable);
                    break;
                case "job":
                    list = boardService.boardSearchByJob(searchKeyword, pageable);
                    break;
                case "all":
                default:
                    list = boardService.boardSearchByNameOrJob(searchKeyword, searchKeyword, pageable);
                    break;
            }
        }

        // 페이징 처리를 위한 변수를 설정합니다.


        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 1, 1);
        int endPage = Math.min(nowPage + 2, list.getTotalPages());

// 이전 페이지 버튼을 보여줄지 결정하는 조건
        boolean showPrevious = nowPage > 1;

// 다음 페이지 버튼을 보여줄지 결정하는 조건
        boolean showNext = nowPage < list.getTotalPages();


        // Model에 게시글 리스트와 페이징 처리를 위한 변수를 추가합니다.
        model.addAttribute("list",list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("showPrevious", showPrevious);
        model.addAttribute("showNext", showNext);
        model.addAttribute("username", principal.getName());



        return "boardList";  // "boardList"라는 이름의 뷰(view)를 반환합니다.
    }



    // 조회수 증가 컨트롤러


    @GetMapping("/boardview/{id}")  // GET 요청을 "/boardview/{id}" URL로 매핑하는 어노테이션입니다.
    public String boardview(@PathVariable("id") Integer id,
                            @RequestParam(value="page", defaultValue = "0") int nowPage,
                            @RequestParam(value="searchType", defaultValue = "") String searchType,
                            @RequestParam(value="searchKeyword", defaultValue = "") String searchKeyword,
                            Model model) {
        Board board = boardService.getBoardAndIncreaseView(id);  // 해당 id의 게시글을 가져오고, 조회수를 증가시킵니다.
        model.addAttribute("board", board);  // Model에 "board"라는 이름으로 게시글을 추가합니다.
        model.addAttribute("comments", board.getComments());  // Model에 "comments"라는 이름으로 게시글의 댓글들을 추가합니다.
        model.addAttribute("comment", new Comment()); // New Comment object for the form
        model.addAttribute("nowPage", nowPage); // 현재 페이지 번호를 모델에 추가
        model.addAttribute("searchType", searchType); // 검색 유형을 모델에 추가
        model.addAttribute("searchKeyword", searchKeyword); // 검색 키워드를 모델에 추가
        return "/boardview";  // "/boardview"라는 이름의 뷰(view)를 반환합니다.
    }




    @GetMapping("/delete")
    public String boardDelete(Integer id, Principal principal){  // Principal 객체를 파라미터로 추가합니다.
        boardService.boardDelete(id, principal.getName());  // 로그인한 사용자의 이름을 함께 넘겨줍니다.
        return "redirect:/list";
    }





    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id,
                         @RequestParam(value="page", defaultValue = "0") int nowPage,
                         @RequestParam(value="searchType", defaultValue = "") String searchType,
                         @RequestParam(value="searchKeyword", defaultValue = "") String searchKeyword,
                         Model model, HttpSession session) {
        Board board = boardService.boardView(id);
        model.addAttribute("board", board);
        session.setAttribute("viewCount", board.getViews()); // 현재 조회수를 세션에 저장
        model.addAttribute("nowPage", nowPage); // 현재 페이지 번호를 모델에 추가
        model.addAttribute("searchType", searchType); // 검색 유형을 모델에 추가
        model.addAttribute("searchKeyword", searchKeyword); // 검색 키워드를 모델에 추가

        // Save search conditions and page number to session
        session.setAttribute("searchType", searchType);
        session.setAttribute("searchKeyword", searchKeyword);
        session.setAttribute("nowPage", nowPage); // Save current page number to session

        return "boardmodify";  // "boardmodify"라는 이름의 뷰(view)를 반환합니다.
    }




    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         Board board,
                         @RequestParam(value="page", defaultValue = "0") int page,
                         @RequestParam(value="searchType", required = false) String searchType,
                         @RequestParam(value="searchKeyword", required = false) String searchKeyword,
                         Principal principal) { // Principal 객체를 파라미터로 추가합니다.

        String encodedSearchKeyword;
        try {
            encodedSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            // Handle the exception as you see fit
            throw new RuntimeException("Encoding failed", e);
        }

        board.setId(id); // Ensure that the board object has the correct id

        Board updatedBoard = boardService.updateBoard(board, principal.getName()); // 로그인한 사용자의 이름을 함께 넘겨줍니다.

        return "redirect:/boardview/" + id + "?page=" + page + "&searchType=" + searchType + "&searchKeyword=" + encodedSearchKeyword;
    }







    @ControllerAdvice
    public class ErrorController {

        @ExceptionHandler(RuntimeException.class)
        public String handleRuntimeException(RuntimeException e, Model model) {
            model.addAttribute("message", e.getMessage());

            return "access-denied";
        }
    }











}