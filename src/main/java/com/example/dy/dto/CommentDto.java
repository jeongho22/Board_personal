//package com.example.dy.dto;
//
//import lombok.Data;
//
//@Data // Lombok 라이브러리의 어노테이션입니다. getter, setter, equals, hashCode, toString 메서드를 자동으로 생성해줍니다.
//public class CommentDto {
//    private String author; // 댓글 작성자의 이름을 저장하는 필드입니다.
//    private String content; // 댓글의 내용을 저장하는 필드입니다.
//    private Integer boardId; // 이 댓글이 어떤 게시판에 속해있는지를 나타내는 게시판 ID를 저장하는 필드입니다. 이 필드를 통해 댓글이 어떤 게시판에 속해있는지 알 수 있습니다.
//}
//
//// CommentDto는 사용자와 서버 간에 데이터를 전달하는 매개체 역할을 하며,
//// CommentController와 CommentService는 이를 통해 데이터를 주고받으며 비즈니스 로직을 수행합니다.