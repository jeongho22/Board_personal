# 프로젝트 제목

## 1. 개요

* SpringFramework를 이용한 게시판 웹 프로젝트
  
일정
* 24.01.01 ~ 24.03.01
* 참여도 : 100% (개인 프로젝트)

## 2. 사용 기술 및 개발 환경

* Server : AWS EC2
* DB : MySQL, AWS RDS
* Framework/Flatform : SpringBoot , SpringSecurity, Thymeleaf , Bootstrap, 
* Language : <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />, <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=JavaScript&logoColor=white" /> , <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white" />, <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white" />
* Tool : Intellj, GitHub, DBeaver



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
