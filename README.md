# SpringFramework 게시판 웹 프로젝트

## 1. 개요

* SpringFramework를 이용한 게시판 웹 프로젝트
* 배포 URL : http://jeonghologin2.link/login
* Test ID : user
* Test PW : 12345!@#$%



개발기간
* 개발 기간: 2024년 1월 1일 ~ 2024년 3월 1일
* 참여도 : 100% (개인 프로젝트)

  

## 2. 사용 기술 및 개발 환경


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


## 3. API 명세서 ERD


게시판을 구성 하는 기초적인 요소를 통해 나만의 게시판을 구성 해보았습니다.

이 섹션에서는 프로젝트의 주요 기능, 구성 요소, 사용 방법 등을 설명합니다. 

📃 API 명세서
https://www.notion.so/dedb4763060946aca67dceaea3740027?v=f42f885a1d854df9ac282f4d382c648e


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




## 4. 내용

게시판을 구성 하는 기초적인 요소를 통해 나만의 게시판을 구성 해보았습니다.

이 섹션에서는 프로젝트의 주요 기능, 구성 요소, 사용 방법 등을 설명합니다. 



- **기능 1:**  회원가입


  
- **기능 2:**  로그인



- **기능 3:** 소셜 로그인 (카카오톡,구글)



- **기능 4:** 게시글 CRUD



- **기능 5:** 댓글 CRUD



- **기능 6:** 대댓글(답글) CRUD



- **기능 7:** 회원 아이디 (CRUD)



- **기능 8:** 게시글 북마크(CRD)



- **기능 9:** 게시글 좋아요(CRD)























### 사용 방법

사용자가 프로젝트를 어떻게 시작하고 사용할 수 있는지 단계별로 설명합니다. 설치 방법, 설정 가이드, 실행 방법 등이 포함됩니다.

#### 의존성 추가 

1. 의존성 설치:

    ```bash
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


2. 프로젝트 실행:

    ```bash
    npm start
    ```

## 기여 방법

오픈 소스 프로젝트의 경우, 다른 사람들이 프로젝트에 기여할 수 있는 방법을 설명합니다. 이슈 제기, 풀 리퀘스트 생성, 코딩 규칙 등에 대한 안내가 포함될 수 있습니다.

## 라이선스

프로젝트 라이선스에 대한 정보를 포함합니다. 일반적으로 사용되는 라이선스는 MIT, Apache 2.0 등입니다. 라이선스 파일의 링크를 포함할 수 있습니다.

## 연락처

프로젝트 관련 문의사항이나 피드백을 위한 연락처 정보를 제공합니다. 이메일, 소셜 미디어 링크 등을 포함할 수 있습니다.
