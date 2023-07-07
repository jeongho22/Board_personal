package com.example.dy.serivce;


import com.example.dy.entity.Board;
import com.example.dy.repository.BoardRepository;
import com.example.dy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@Service
public class BoardService {





    @Autowired // 의존성 주입
    private BoardRepository boardRepository;

    private final CommentRepository commentRepository; // CommentRepository 인스턴스를 추가합니다.






    //글 작성 처리
    public void write(Board board) {




        board.setViews(0);
        boardRepository.save(board);

    }

    //게시글 리스트 처리

    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }


    //특정 게시글 불러오기

    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }


    @Autowired
    public BoardService(BoardRepository boardRepository, CommentRepository commentRepository) { // CommentRepository를 주입받습니다.
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository; // CommentRepository 인스턴스를 초기화합니다.
    }

    // 위 코드는 BoardService 클래스의 일부입니다. BoardService 클래스는 BoardRepository와 CommentRepository를 필요로 합니다.
    // 이 두 인스턴스는 생성자를 통해 외부에서 주입됩니다. 이렇게 하면 BoardService 클래스는 데이터 접근 로직을 직접 구현하지 않아도 됩니다.





//     트랜잭션 처리 게시글에 댓글을 삭제하고 게시글 삭제

    @Transactional
    public void boardDelete(Integer id) {


        commentRepository.deleteByBoardId(id); // 먼저 게시글에 연결된 댓글들을 삭제합니다.
        System.out.println("댓글이 먼저 삭제되었습니다.");


//        if (true) { // 특정 조건
//            throw new RuntimeException("롤백이 발생되어 댓글 삭제가 취소되었습니다 !");
//        }

        boardRepository.deleteById(id); // 이후에 게시글을 삭제합니다.
        System.out.println("이후에 게시물도 같이 삭제되었습니다.");






    }





    //@Transactional 어노테이션을 사용하여 트랜잭션 안에서 동작합니다.
    // 먼저 commentRepository.deleteByBoardId(id); 코드를 통해 해당 게시글에 연결된 모든 댓글을 삭제하고,
    // 그 다음에 boardRepository.deleteById(id); 코드를 통해 게시글 자체를 삭제합니다.





    //초등학생 수준으로 설명하자면, 이 코드는 컴퓨터가 이해할 수 있는 방식으로 "게시글과 그 게시글에 달린 댓글을 함께 지우는 방법"을 설명하는 것입니다.
    //우리가 박스를 정리할 때, 박스 안에 들어있는 작은 상자들부터 먼저 정리하고, 그 다음에 큰 박스를 정리하는 것과 비슷합니다. 이 코드에서는 작은 상자가 댓글이고, 큰 박스가 게시글입니다.
    //또한, 이 일련의 작업이 모두 끝나야만 실제로 정리가 완료되는 것을 보장합니다. 만약 작은 상자를 정리하다가 문제가 생기면, 큰 박스를 정리하는 것은 취소하고, 다시 처음부터 시작합니다.
    // 이렇게 하면 박스 정리 과정에서 문제가 생겨도 안전하게 처리할 수 있습니다.
    //마지막으로, 이 작업을 할 때 필요한 도구들 (여기서는 BoardRepository와 CommentRepository)은 외부에서 가져옵니다. 이렇게 하면, 만약 다른 종류의 도구를 사용해야 할 경우에도 쉽게 바꿀 수 있습니다.
    // 이것은 마치 작업 도구가 고장 났을 때, 새 도구를 가져와서 계속 작업할 수 있는 것과 비슷합니다.




    // 이름 검색기능
    public Page<Board> boardSearchByName(String searchKeyword, Pageable pageable) {
        return boardRepository.findByNameContaining(searchKeyword, pageable);
    }

    // 직업 검색 기능

    public Page<Board> boardSearchByJob(String searchKeyword, Pageable pageable) {
        return boardRepository.findByJobContaining(searchKeyword, pageable);
    }

    // 전체 검색 기능

    public Page<Board> boardSearchByNameOrJob(String name, String job, Pageable pageable) {
        return boardRepository.findByNameContainingOrJobContaining(name, job, pageable);
    }


    // 조회수 저장 기능

    public Board getBoardAndIncreaseView(Integer id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        board.setViews(board.getViews() + 1); // 조회수 증가
        boardRepository.save(board); // 변경된 조회수 저장
        return board;
    }


    // 수정 후에도 조회수 변경금지
    public Board updateBoard(Board updatedBoard) {
        Board existingBoard = boardRepository.findById(updatedBoard.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + updatedBoard.getId()));

        existingBoard.setName(updatedBoard.getName()); // Update fields as necessary
        existingBoard.setJob(updatedBoard.getJob());
        existingBoard.setAge(updatedBoard.getAge());
        existingBoard.setViews(existingBoard.getViews()); // This assumes views should not be updated.

        return boardRepository.save(existingBoard);
    }


}

// 해당 서비스는 다시 컨트롤러에서 사용한다.
// 또한 boardRepository에 저장하는 서비스를 해줌