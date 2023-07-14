package com.example.dy.service;

import com.example.dy.entity.Board;
import com.example.dy.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;



    // 모든 게시글을 가져오는 메소드
    @Transactional
    public Page<Board> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        boards.getContent().forEach(board -> board.getComments().size()); // This will load the comments due to the FetchType.LAZY
        return boards;
    }


    // ID로 게시글을 가져오는 메소드
    @Transactional
    public Board getBoardById(Integer id) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            board.getComments().size(); // This will load the comments due to the FetchType.LAZY
        }
        return board;
    }


    // 이름으로 게시글을 가져오는 메소드
    public Page<Board> getBoardsByName(String name, Pageable pageable) {       //이름 검색어를 레포지터리로 반환
        return boardRepository.findByNameContaining(name, pageable);
    }


    // 가격으로 게시글을 가져오는 메소드
    public Page<Board> getBoardsByJob(String job, Pageable pageable) {      //가격 검색어를 레포지터리로 반환
        return boardRepository.findByJobContaining(job, pageable);
    }


    // 새로운 게시글을 생성하는 메소드
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }


    // 게시글을 수정하는 메소드
    public Board updateBoard(Integer id, Board boardDetails) {
        Board board = boardRepository.findById(id).orElse(null);

        if (board != null) {
            board.setName(boardDetails.getName());
            board.setJob(boardDetails.getJob());
            return boardRepository.save(board);
        }
        return null;
    }


    // 게시글을 삭제하는 메소드
    public void deleteBoard(Integer id) {
        boardRepository.deleteById(id);
    }
}
