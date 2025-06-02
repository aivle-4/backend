# 📚 AIVLE Book Service

**Spring Boot 3 + JPA + WebFlux + H2** 로 구현한 간단한 “책 공유 플랫폼” 백엔드입니다.  
회원 인증부터 책 CRUD, 그리고 OpenAI Images API를 이용한 **자동 표지 생성** 기능까지 포함되어 있습니다.

</br>

## 📍 아키텍쳐 
![Image](https://github.com/user-attachments/assets/37df8092-0192-4842-a479-030979f8fe20)

</br>

## 🌟 제공 기능

| 구분 | 설명                        | HTTP                         |
|------|---------------------------|------------------------------|
| 회원 | 회원가입 / 로그인                | `POST /api/members/**`       |
| 책 목록 | 전체·내 서재·페이징               | `GET /api/books`             |
| 책 검색 | 제목·저자 키워드                 | `GET /api/books/?keyword=`   |
| 책 상세 | 단일 조회                     | `GET /api/books/{bookId}`    |
| 책 작성 | 제목·내용·표지 자동 생성            | `POST /api/books`            |
| 책 수정 | 제목·내용 수정                  | `PUT /api/books/{bookId}`    |
| 책 삭제 | 선택한 책 삭제                  | `DELETE /api/books/{bookId}` |
| **표지 생성** | OpenAI(DALL-E 2) · 1024×1024 | `POST /api/books/cover`      |

> **표지 엔드포인트 바디**
> ```json
> {
>   "title": "Walking Library",
>   "content": "A shy student walks into pages that become city streets…"
> }
> ```

</br>

## 🔧 기술 스택

- **언어**: Java 17
- **프레임워크**: Spring Boot 3.x
  - Spring Web, Spring WebFlux (WebClient), Spring Data JPA, Spring Security (JWT)
- **데이터베이스**: H2 
- **빌드 도구**: Gradle
- **사용 라이브러리**:
  - OpenAI Images API 연동 → Spring WebFlux(WebClient), Jackson
  - JWT 발급/검증 → spring-security-jwt
- **운영 환경**: AWS EC2

</br>

## ⚙️ 환경 설정

`src/main/resources/application.yml`

```yaml
spring:
  jackson:
    time-zone: Asia/Seoul

  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:testdb;
    username: sa
    password: ********

  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        use_sql_comments: true
        default_batch_fetch_size: 1000
    open-in-view: false

jwt:
  secret: sGk+***************************

openai:
  api-key: sk-**************************
  image:
    model: dall-e-2
    size: 1024x1024
```

</br>

## 📁 프로젝트 구조
```
└── main
    ├── java
    │   └── com.example.aivle
    │       ├── AivleApplication.java                 // 메인 애플리케이션 클래스
    │       ├── domain
    │       │   └── book
    │       │       ├── controller
    │       │       │   └── BookController.java        // 도서 API 엔드포인트
    │       │       ├── dto
    │       │       │   ├── BookRequest.java            // 도서 등록/수정 요청 DTO
    │       │       │   ├── BookResponse.java           // 도서 상세 조회 응답 DTO
    │       │       │   ├── BookSummaryResponse.java    // 도서 목록 조회 응답 DTO
    │       │       │   ├── CoverRequest.java           // 표지 생성 요청 DTO
    │       │       │   └── CoverResponse.java          // 표지 생성 응답 DTO
    │       │       ├── entity
    │       │       │   └── Book.java                   // 도서 엔티티
    │       │       ├── repository
    │       │       │   └── BookRepository.java         // JpaRepository<Book, Integer>
    │       │       └── service
    │       │           ├── BookService.java            // 도서 서비스 인터페이스
    │       │           └── BookServiceImpl.java        // 도서 서비스 구현체
    │       ├── member
    │       │   ├── presentation
    │       │   │   └── MemberController.java           // 회원 API 엔드포인트
    │       │   ├── dto
    │       │   │   ├── LoginRequest.java               // 회원 가입/로그인 요청 DTO
    │       │   │   └── LoginResponse.java              // 회원 가입/로그인 응답 DTO
    │       │   ├── entity
    │       │   │   └── Member.java                     // 회원 엔티티
    │       │   ├── repository
    │       │   │   └── MemberRepository.java           // JpaRepository<Member, Integer>
    │       │   └── service
    │       │       ├── MemberService.java              // 회원 서비스 인터페이스
    │       │       └── MemberServiceImpl.java          // 회원 서비스 구현체
    │       └── global
    │           ├── base
    │           │   └── BaseEntity.java                 // 생성일자·수정일자 자동 관리
    │           ├── config
    │           │   ├── JpaConfig.java                  // JPA 설정
    │           │   ├── SecurityConfig.java             // Spring Security 설정 (JWT 포함)
    │           │   └── WebConfig.java                  // CORS 등 웹 설정
    │           ├── exception
    │           │   ├── CoverGenerationException.java   // 표지 생성 실패 예외
    │           │   ├── InvalidApiKeyException.java     // 잘못된/누락된 OpenAI API 키
    │           │   ├── OrganizationAuthException.java  // 조직(Org) 권한 부족 예외
    │           │   └── UnsupportedParameterException.java // 지원하지 않는 파라미터 예외
    │           ├── openai
    │           │   └── AiCoverClient.java               // OpenAI Images API 연동 클라이언트
    │           ├── response
    │           │   ├── CustomException.java            // 도메인별 커스텀 예외 공통 부모
    │           │   ├── ErrorCode.java                  // 기능별 오류 코드(enum)
    │           │   ├── GlobalExceptionHandler.java     // 전역 예외 처리기 (@RestControllerAdvice)
    │           │   ├── Response.java                   // API 응답 표준 래퍼
    │           │   └── SuccessCode.java                // 성공 메시지 코드(enum)
    │           └── util
    │               └── jwt
    │                   ├── JwtTokenFilter.java         // JWT 인증/인가 필터
    │                   ├── JwtTokenUtils.java          // JWT 발급/검증 유틸
    │                   └── ResponseUtils.java          // 공통 응답 생성 유틸
    └── resources
        ├── static                                    
        ├── templates                                 
        ├── application.yml                           // 공통 설정
        ├── application-dev.yml                       // 개발 환경 설정
        └── application-local.yml                     // 로컬 환경 설정

```

</br>

## 🖥️ 프로젝트 세부 구조
### 📌 Domain → member
회원(Member) 도메인 관련 기능을 담당합니다.
- 주요 기능
  - 회원 가입(Signup), 로그인(Login)
  - JWT 기반 인증 및 세션 관리
```
domain/member
├─ controller
│   └─ MemberController.java            ▶ 회원 관련 API 제공
│       • signup()         : 회원 가입
│       • login()          : 로그인 (JWT 발급)
│       • findMember()     : 회원 조회
│
├─ dto
│   ├─ LoginRequest.java                 ▶ 회원 가입/로그인 요청 DTO
│   │   • 필드: loginId, password
│   │
│   └─ LoginResponse.java                ▶ 회원 가입/로그인 응답 DTO
│       • 필드: memberId
│
├─ entity
│   └─ Member.java                      ▶ 회원 엔티티
│       • 필드: id, loginId, password  
│       • BaseEntity 상속(생성일/수정일 자동 관리)
│
├─ repository
│   └─ MemberRepository.java            ▶ `JpaRepository<Member, Integer>`
│       • 메서드: findByLoginId(String loginId) → 회원 조회
│
└─ service
    ├─ MemberService.java               ▶ 회원 서비스 인터페이스
    │   • signup(LoginRequest)            : 회원 가입 (중복 검사 후 저장)
    │   • login(LoginRequest)             : 로그인 (ID/PW 확인 → JWT 발급)
    │   • findMember(Integer memberId)     : 회원 조회
    │
    └─ MemberServiceImpl.java           ▶ 회원 서비스 구현체
        • 비즈니스 로직 실제 구현 (중복 검사, 암호화, 토큰 생성 등)
```

### 📌 Domain → book
도서(Book) 도메인 관련 기능을 담당합니다.
- 주요 기능
  - 도서 등록, 조회, 수정, 삭제
  - 표지 이미지(AI) 생성
```
domain/book
├─ controller
│   └─ BookController.java              ▶ 도서 관련 API 제공
│       • addBook()          : 도서 등록
│       • findBook() / findBooks() : 도서 조회
│       • updateBook()       : 도서 수정
│       • deleteBook()       : 도서 삭제
│       • generateCover()    : 도서 표지(AI) 생성
│
├─ controller/dto
│   ├─ BookRequest.java                 ▶ 도서 등록/수정 요청 DTO
│   │   • 필드: title, author, content, coverImageUrl
│   │
│   ├─ BookResponse.java                ▶ 도서 상세 조회 응답 DTO
│   │   • 필드: bookId, memberId, title, author, content, createdAt, updatedAt
│   │
│   ├─ BookSummaryResponse.java         ▶ 도서 목록 조회 응답 DTO
│   │   • 필드: bookId, title, author, createdAt, coverImageUrl
│   │
│   ├─ CoverRequest.java                ▶ 표지 이미지 생성 요청 DTO
│   │   • 필드: title, content
│   │
│   └─ CoverResponse.java               ▶ 표지 이미지 생성 결과 DTO
│       • 필드: success, message, imageUrl
│
├─ entity
│   └─ Book.java                        ▶ 도서 엔티티
│       • 필드: id, title, author, content, coverImageUrl  
│       • 관계: 작성자(Member)와 @ManyToOne 연관관계
│
├─ repository
│   └─ BookRepository.java              ▶ `JpaRepository<Book, Integer>`
│       • 키워드(제목, 저자) 기반 검색 메서드 제공 (`findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase`)
│
└─ service
    ├─ BookService.java                 ▶ 도서 서비스 인터페이스
    │   • findBook(Integer bookId)           : 단건 조회
    │   • findBooks(String keyword)          : 리스트 조회 (키워드 검색)
    │   • addBook(BookRequest, HttpSession)  : 등록
    │   • updateBook(Integer, BookRequest, HttpSession) : 수정 (본인만 가능)
    │   • deleteBook(Integer, HttpSession)    : 삭제 (본인만 가능)
    │
    └─ BookServiceImpl.java             ▶ 도서 서비스 구현체
        • 비즈니스 로직 실제 구현 (Repository 호출, 권한 검증 등)
```

### 📌 Global(공통 영역)
프로젝트 전반에서 사용되는 공통 기능, 설정, 예외 처리, OpenAI 연동 등을 담당합니다.
```
global
├─ base
│   └─ BaseEntity.java                  ▶ 공통 베이스 엔티티
│       • @MappedSuperclass  
│       • 필드: createdAt(@CreatedDate), updatedAt(@LastModifiedDate)
│       • 모든 엔티티가 상속받아 생성/수정 일자 자동 관리
│
├─ openai
│   └─ AiCoverClient.java               ▶ OpenAI Images API 호출 클라이언트
│       • 역할: 도서 제목/내용을 받아 표지 생성 요청  
│       • WebClient 초기화 → Bearer {API-KEY} 헤더 포함  
│       • Prompt 구성 → 모델(dall-e-2), 크기(1024×1024) JSON 전송  
│       • 응답 JSON → data[0].url 추출 후 반환  
│       • 내부 DTO: OpenAiImageRequest, OpenAiImageResponse (record)
│       • 의존성: Spring WebFlux(WebClient), Jackson
│
├─ response
│   ├─ CustomException.java              ▶ 커스텀 예외 공통 부모
│   │   • 필드: ErrorCode, message  
│   │   • 도메인별 예외가 상속하여 사용
│   │
│   ├─ ErrorCode.java                    ▶ 기능별 오류 코드(enum)
│   │   • 각 코드: HTTP 상태 + 기본 메시지  
│   │
│   ├─ GlobalExceptionHandler.java       ▶ 전역 예외 처리기 (@RestControllerAdvice)
│   │   • CustomException, 검증 오류(MethodArgumentNotValidException) 등 처리  
│   │
│   ├─ Response.java                      ▶ API 응답 표준 래퍼
│   │   • success(), error() 팩토리 메서드 제공  
│   │   • 공통 응답 JSON 구조 통일
│   │
│   └─ SuccessCode.java                   ▶ 성공 메시지 코드(enum)
│       • OK(200, "성공입니다") 등 재사용 가능한 성공 메시지
│
└─ util
    └─ jwt
        ├─ JwtTokenFilter.java             ▶ JWT 인증/인가 필터
        │   • 요청 헤더의 Bearer 토큰 검증  
        │   • 유효 시 SecurityContext에 인증 정보 저장
        │
        ├─ JwtTokenUtils.java              ▶ JWT 유틸리티 클래스
        │   • AccessToken / RefreshToken 생성  
        │   • 토큰 검증, 만료 시간 설정 등
        │
        └─ ResponseUtils.java              ▶ 공통 응답 생성 유틸
            • 컨트롤러에서 간단하게 `Response.success(...)` 호출 가능
```
