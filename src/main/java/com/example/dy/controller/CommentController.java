package com.example.dy.controller;

import com.example.dy.entity.Board;
import com.example.dy.entity.Comment;
import com.example.dy.serivce.BoardService;
import com.example.dy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;

    @PostMapping("/boardview/{boardId}")
    public String addComment(@ModelAttribute Comment comment, @PathVariable("boardId") Integer boardId) {
        Board board = boardService.boardView(boardId);  // 해당하는 Board를 가져옴
        comment.setBoard(board);  // Comment가 어떤 Board에 속하는지 설정
        commentService.save(comment);
        return "redirect:/boardview/" + boardId;
    }

    @PostMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Integer commentId) {
        Comment comment = commentService.findById(commentId);
        Integer boardId = comment.getBoard().getId();
        commentService.deleteById(commentId);
        return "redirect:/boardview/" + boardId;
    }







}
