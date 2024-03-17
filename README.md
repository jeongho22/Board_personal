# SpringFramework 게시판 웹 프로젝트

# 1. 개요

* SpringFramework를 이용한 게시판 웹 프로젝트
* 배포 URL : http://jeonghologin2.link/login
* Test ID : user
* Test PW : 12345!@#$%



개발기간
* 개발 기간: 2024년 1월 1일 ~ 2024년 3월 1일
* 참여도 : 100% (개인 프로젝트)

  

# 2. 사용 기술 및 개발 환경


## Tech Stack



### Server
- ![Amazon EC2](https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white)
  
### Database
- ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white) 
- ![AWS RDS](https://img.shields.io/badge/AWS%20RDS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) 

### Framework/Platform
- ![Spring Boot](https://img.shields.io/badge/springboot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=FFFFFF) 
- ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white) 
- ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white) 
- ![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white) 

### Programming Languages
- ![Java](https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white) 
- ![JavaScript](https://img.shields.io/badge/javascript-F7DF1E.svg?&style=for-the-badge&logo=javascript&logoColor=FFFFFF) 
- ![HTML5](https://img.shields.io/badge/html5-E34F26.svg?&style=for-the-badge&logo=html5&logoColor=FFFFFF) 
- ![CSS3](https://img.shields.io/badge/css3-1572B6.svg?&style=for-the-badge&logo=css3&logoColor=FFFFFF+) 

### Tools
- ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellijidea&logoColor=white) 
- ![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white) 
- ![DBeaver](https://img.shields.io/badge/DBeaver-A1A1A1?style=for-the-badge&logo=DBeaver&logoColor=white) 

![image](https://github.com/jeongho22/Board_personal/assets/96859291/d11ce0c2-1744-49aa-9065-7c4ad9c0b11b)


### 의존성 추가 

    dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-data-rest'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
	implementation 'org.springframework.boot:spring-boot-starter-validation'

	runtimeOnly 'mysql:mysql-connector-java'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'}



# 3. API 명세서 ERD


📃 API 명세서 : https://www.notion.so/dedb4763060946aca67dceaea3740027?v=f42f885a1d854df9ac282f4d382c648e
![스크린샷 2024-03-17 160921](https://github.com/jeongho22/Board_personal/assets/96859291/7c064735-e630-46c6-b4d2-ae3fa1b78c6c)




📌 ERD

![스크린샷 2024-03-16 015451](https://github.com/jeongho22/Board_personal/assets/96859291/5593c76a-b7cc-41f0-a2a3-035dbd1945ca)


* Users
사용자(User) 엔티티는 시스템의 사용자 정보를 보여준다 . 사용자 이름, 비밀번호, 이메일, 역할, 로그인 유형 등의 정보를 포함합니다. 이메일은 고유해야 하며([unique]), 사용자는 여러 게시글, 댓글, 북마크, 좋아요를 가질 수 있습니다.

* Articles
게시글(Article) 엔티티는 사용자가 생성한 게시글을 보여준다. 제목, 내용, 작성자 닉네임, 조회수를 포함하며, 각 게시글은 한 명의 사용자에게 속하고(user_id 외래키 참조), 여러 댓글, 북마크, 좋아요를 가질 수 있습니다.

* Comments
댓글(Comment) 엔티티는 게시글에 대한 댓글을 나타냅니다. 댓글 본문과 닉네임을 포함하며, 각 댓글은 특정 게시글(article_id 외래키 참조)과 사용자(user_id 외래키 참조)에 속합니다. 댓글은 대댓글 구조를 지원하기 위해 자기 참조(parent_id)를 포함한다.

* Bookmarks
북마크(Bookmark) 엔티티는 사용자가 게시글을 북마크한 정보를 나타냅니다. 각 북마크는 하나의 게시글(article_id 외래키 참조)과 하나의 사용자(user_id 외래키 참조)에 연결됩니다.

* LikeBoards
좋아요(LikeBoard) 엔티티는 사용자가 게시글에 좋아요를 표시한 정보를 나타냅니다. 각 좋아요는 하나의 게시글(article_id 외래키 참조)과 하나의 사용자(user_id 외래키 참조)에 연결됩니다.

* AuditingFields
감사 필드(AuditingFields) 는 생성 시간, 수정 시간, 삭제 시간(소프트 삭제를 위한)을 관리하기 위해 모든 엔티티에 포함된 공통 필드입니다. 이는 추상 클래스로, 실제 데이터베이스 테이블에는 직접 나타나지 않지만, 상속을 통해 각 엔티티에 이 필드들이 포함됩니다.




# 4. 내용

게시판을 구성 하는 기초적인 요소를 통해 나만의 게시판을 구성 해보았습니다. 이 섹션에서는 프로젝트의 주요 기능, 구성 요소, 사용 방법 등을 설명합니다. 

### 요구사항 분석

기능 요구사항


### **기능 1:**  회원가입
 - 모든 데이터는 NOTNULL로 지정. 빈칸으로 제출 시 "회원정보를 확인해 주세요." 메시지를  출력한다.
 - 비밀번호는 특수문자, 대소문자, 숫자 중 2개이상의 조합으로 10~16자로 설정 해야한다.
 - 비밀번호, 비밀번호 확인 란의 두 데이터가 일치하는지 확인하고, 불일치 시 "비밀번호가 일치하지 않습니다.." 메시지 출력
 - 데이터베이스에 존재하는 아이디를 제출 시 "이미 존재하는 아이디 입니다." 메시지 출력  


  
### **기능 2:** 일반 로그인

 - 로그인 페이지에 가입 되지않는 아이디로 접근할려고 하면 "아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요." 메시지 추가로 출력



### **기능 3:** 소셜 로그인 (카카오톡,구글)

  - 로그인 페이지에서 카카오 계정으로 로그인을 누르면 소셜로그인 으로 로그인 가능
  - 로그인 페이지에서 구글 계정으로 로그인을 누르면 소셜로그인 으로 로그인 가능 



### **기능 4:** 메인 페이지 

- 4-1.게시판 메인 페이지
  - 게시판 메인 페이지 시간으로 정렬 될것
  - 게시글 제목 클릭 시 특정 게시글로 이동할 수있도록 한다.
  - 페이지는 한 페이지에 10개씩 게시글이 보이도록 한다.

- 4-2.메뉴
  - 로그인 시 좌측 상단 메뉴에 북마크, 마이페이지 ,로그아웃이 나타남
  - 비 로그인시 좌측 상단 메뉴에 login 버튼만 나타남 

- 4-3.검색
  - 전체 : 제목 또는 내용에 검색어가 포함된 게시글 모두 조회
  - 제목 검색 : 제목에 해당하는 검색어가 포함된 게시글 모두 조회
  - 내용 검색 : 내용에 해당하는 검색어가 포함된 게시글 모두 조회


- 4-4.인기글
  - 인기글 : 좋아요 3개 , 조회수 30이 넘는 특정 게시물들이 인기글를 클릭했을때 조회 




### **기능 5:** 게시글 CRUD

- 5-1.게시글 생성
  제목 또는 내용이 빈칸일 경우 alert 메세지 출력
  
- 5-2.게시글 읽기
  제목 내용 작성자 생성시간 읽기
  
- 5-3.게시글 수정
  로그인 이용자가 자기 자신의 게시물을 조회 할 경우에 수정,삭제 버튼이 나타남
  
- 5-4.게시글 삭제
  로그인 이용자가 자기 자신의 게시물을 조회 할 경우에 수정,삭제 버튼이 나타남


### **기능 6:** 댓글 CRUD

- 6-1.댓글 생성
  내용이 빈칸일 경우 alert 메세지 출력
  
- 6-2.댓글 읽기
  내용 작성자 생성시간 읽기
  
- 6-3.댓글 수정
  로그인 이용자가 자기 자신의 댓글을 조회 할 경우에 수정,삭제 버튼이 나타남
  
- 6-4.댓글 삭제
  로그인 이용자가 자기 자신의 댓글을 조회 할 경우에 수정,삭제 버튼이 나타남



### **기능 7:** 대댓글(답글) CRUD

- 7-1.대댓글 생성
  내용이 빈칸일 경우 alert 메세지 출력
  
- 7-2.대댓글 읽기
  내용 작성자 생성시간 읽기
  
- 7-3.대댓글 수정
  로그인 이용자가 자기 자신의 대댓글을 조회 할 경우 위에 댓글 기능과 같이 수정,삭제 버튼이 나타남
  
- 7-4.대댓글 삭제
  로그인 이용자가 자기 자신의 대댓글을 조회 할 경우 위에 댓글 기능과 같이 수정,삭제 버튼이 나타남



### **기능 8:** 회원 아이디 (CRUD)

- 8-1.회원 생성
  위의 회원 가입 기능과 동일
  
- 8-2.회원 읽기
  메뉴->마이페이지 클릭 후 이름,아이디,권한,가입일, 로그인 유형 확인 
  
- 8-3.회원 수정
  일반로그인 : 메뉴->마이페이지 클릭 -> 수정 클릭 -> 이름,패스워드 수정
  소셜로그인 : 메뉴->마이페이지 클릭 -> 수정 클릭 -> 이름 수정
  
- 8-4.회원 탈퇴 
  메뉴 -? 마이페이지 클릭 후 -> 탈퇴 클릭 -> 기존 로그인 페이지로 이동 



### **기능 9:** 게시글 북마크(CRD)

- 9-1.북마크 생성
  게시글로 가서 북마크 클릭 -> 북마크가 등록 되었습니다.
  
- 9-2.북마크 읽기
  메뉴->북마크 클릭 -> 내가 생성한 게시글의 번호, 제목, 북마크 저장시간 조회 가능 
  
- 9-3.북마크 삭제  
  게시글로 가서 생성한 북마크 클릭 재 클릭-> 북마크가 삭제 되었습니다.



### **기능 10:** 게시글 좋아요(CRD)

- 10-1.좋아요 생성
  게시글로 가서 좋아요 클릭 -> 하트 안이 빨간색으로 디자인 변경 
  
- 10-2.좋아요 읽기
  좋아요 옆 숫자 클릭 -> 게시글에 좋아요를 클릭한 회원들 조회  
  
- 10-3.좋아요 삭제  
  게시글로 가서 생성한 좋아요 클릭 재 클릭-> 하트 안이 빈칸으로 디자인 변경.




## 라이선스

프로젝트 라이선스에 대한 정보를 포함합니다. 

