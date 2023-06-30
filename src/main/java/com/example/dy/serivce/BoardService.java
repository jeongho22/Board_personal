package com.example.dy.serivce;


import com.example.dy.entity.Board;
import com.example.dy.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board) {




        board.setViews(0);
        boardRepository.save(board);

    }

    //게시글 리스트 처리

    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);   // 전부다 찾는다...
    }


    //특정 게시글 불러오기

    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }


    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);

    }

    // 검색기능
    public Page<Board> boardSearchByName(String searchKeyword, Pageable pageable) {
        return boardRepository.findByNameContaining(searchKeyword, pageable);
    }

    public Page<Board> boardSearchByJob(String searchKeyword, Pageable pageable) {
        return boardRepository.findByJobContaining(searchKeyword, pageable);
    }

    public Page<Board> boardSearchByNameOrJob(String name, String job, Pageable pageable) {
        return boardRepository.findByNameContainingOrJobContaining(name, job, pageable);
    }

    public Board getBoardAndIncreaseView(Integer id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        board.setViews(board.getViews() + 1); // 조회수 증가
        boardRepository.save(board); // 변경된 조회수 저장
        return board;
    }

}

// 해당 서비스는 다시 컨트롤러에서 사용한다.
// 또한 boardRepository에 저장하는 서비스를 해줌