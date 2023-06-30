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

import java.time.LocalDateTime;

@Controller //
public class ViewController {

    @GetMapping("/write")
    public String boardwriteform(){
        return "boardwrite";
    }



    @Autowired
    private BoardService boardService;
    @PostMapping("/boardwritepro")
    public String boardwrite(Board board, Model model) {


        board.setTime(LocalDateTime.now());

        boardService.write(board);

        model.addAttribute("message","글작성이 완료 되었습니다.");
        model.addAttribute("url","/list");

        return "message";
    }

    @GetMapping("/list")
    public String boardList(Model model,
                            @PageableDefault(page =0, size =10, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword, String searchType) {

        Page<Board> list = null;

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


        int nowPage= list.getPageable().getPageNumber() +1 ;
        int startPage = Math.max(nowPage - 4,1);
        int endPage= Math.min(nowPage + 5, list.getTotalPages()) ;

        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);


        return "boardList";
    }


    // 조회수 증가 컨트롤러
    @GetMapping("/boardview/{id}")

    public String boardview(@PathVariable("id") Integer id, Model model) {
        Board board = boardService.getBoardAndIncreaseView(id);
        model.addAttribute("board", board);
        model.addAttribute("comments", board.getComments());
        model.addAttribute("comment", new Comment()); // New Comment object for the form
        return "/boardview";
    }



    @GetMapping("/delete")
    public String boardDelete(Integer id, Model model){

        boardService.boardDelete(id);

        return "redirect:/list";

    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Model model){

        model.addAttribute("board",boardService.boardView(id));


        return "boardmodify";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, Board board){

        Board boardTemp = boardService.boardView(id);
        boardTemp.setName(board.getName());
        boardTemp.setJob(board.getJob());

        boardService.write(boardTemp);

        return "redirect:/list";


    }


}
