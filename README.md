# 프로젝트 제목

## 1. 개요

* SpringFramework를 이용한 게시판 웹 프로젝트
![스크린샷_2024-03-08_000722](https://github.com/jeongho22/Board_personal/assets/96859291/44d69743-010f-4b1f-9c92-298bfd31d8d5)

* 배포 URL : http://jeonghologin2.link/login
* Test ID : user
* Test PW : 12345!@#$%



일정
* 24.01.01 ~ 24.03.01
* 참여도 : 100% (개인 프로젝트)

## 2. 사용 기술 및 개발 환경

## Tech Stack


### Server
- ![Amazon EC2](https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white) - Cloud computing platform.

### Database
- ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white) - Relational database management system.
- ![AWS RDS](https://img.shields.io/badge/AWS%20RDS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) - Managed relational database service.

### Framework/Platform
- ![Spring Boot](https://img.shields.io/badge/springboot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=FFFFFF) - Framework for building web applications.
- ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white) - Framework for authentication and access control.
- ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white) - Server-side Java template engine.
- ![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white) - Front-end framework.

### Programming Languages
- ![Java](https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white) - Main backend development language.
- ![JavaScript](https://img.shields.io/badge/javascript-F7DF1E.svg?&style=for-the-badge&logo=javascript&logoColor=FFFFFF) - Used for client-side scripting.
- ![HTML5](https://img.shields.io/badge/html5-E34F26.svg?&style=for-the-badge&logo=html5&logoColor=FFFFFF) - Standard markup language for Web pages.
- ![CSS3](https://img.shields.io/badge/css3-1572B6.svg?&style=for-the-badge&logo=css3&logoColor=FFFFFF+) - Used for styling web pages.

### Tools
- ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellijidea&logoColor=white) - Integrated development environment.
- ![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white) - Platform for version control and collaboration.
- ![DBeaver](https://img.shields.io/badge/DBeaver-A1A1A1?style=for-the-badge&logo=DBeaver&logoColor=white) - SQL database tool.



## 3. 내용

게시판을 구성 하는 기초적인 요소를 통해 나만의 게시판을 구성 해보았습니다.

이 섹션에서는 프로젝트의 주요 기능, 구성 요소, 사용 방법 등을 설명합니다. 

### 설명

- **기능 1:**  로그인
  
Spring Security dependency , oauth2-client 주입


	dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'}

로그인, 로그아웃 설정

  		// Login 창 커스텀 마이징 
                .formLogin(form -> form
                        .loginPage("/login") 
                        .loginProcessingUrl("/login")  
                        .failureHandler(customAuthenticationFailureHandler()) 
                        .defaultSuccessUrl("/articles", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                )

  
  
- **기능 2:** (기능에 대한 설명)
- **기능 3:** (기능에 대한 설명)


























### 사용 방법

사용자가 프로젝트를 어떻게 시작하고 사용할 수 있는지 단계별로 설명합니다. 설치 방법, 설정 가이드, 실행 방법 등이 포함됩니다.

#### 설치 및 실행 가이드

1. 의존성 설치:

    ```bash
    npm install
    ```

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
