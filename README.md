# backend

# 📚 AIVLE Book Service

**Spring Boot 3 + JPA + WebFlux + H2** 로 구현한 간단한 “책 공유 플랫폼” 백엔드입니다.  
회원 인증부터 책 CRUD, 그리고 OpenAI Images API를 이용한 **자동 표지 생성** 기능까지 포함되어 있습니다.

---

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

---
## 🖥️ 세부 코드
## domain/book
도서(Book) 도메인 관련 기능을 담당합니다.
도서 등록, 조회, 수정, 삭제 및 표지 이미지 생성을 제공합니다.

### controller/BookController.java
도서 관련 API를 제공합니다.
도서 등록 (addBook)
도서 조회 (findBook, findBooks)
도서 수정 (updateBook)
도서 삭제 (deleteBook)
도서 표지 생성 (generateCover)

### controller/dto
- BookRequest.java
도서 등록/수정 요청 DTO (제목, 저자, 내용, 표지 이미지 URL)
- BookResponse.java
도서 상세 조회 응답 DTO (도서 ID, 회원 ID, 제목, 저자, 내용, 등록일자, 수정일자)
- BookSummaryResponse.java
도서 목록 조회 응답 DTO (도서 ID, 제목, 저자, 등록일자, 표지 이미지 URL)
- CoverRequest.java
표지 이미지 생성을 위한 제목 및 내용 요청 DTO
- CoverResponse.java
표지 이미지 생성 결과 DTO (성공 여부, 메시지, 이미지 URL)

### entity/Book.java
도서 엔티티. 제목, 저자, 내용, 표지 이미지 URL과 작성자(회원) 연관관계 포함.

### repository/BookRepository.java
JpaRepository<Book, Integer>
키워드(제목, 저자명) 기반 도서 검색 지원

### service/BookService.java & BookServiceImpl.java
도서 등록, 조회, 수정, 삭제 비즈니스 로직.
  -findBook(Integer bookId): 도서 단건 조회
  -findBooks(String keyword): 도서 리스트 조회
  -addBook(BookRequest, HttpSession): 도서 등록
  -updateBook(Integer bookId, BookRequest, HttpSession): 도서 수정 (본인만 가능)
  -deleteBook(Integer bookId, HttpSession): 도서 삭제 (본인만 가능)

### service/CoverService.java & CoverServiceImpl.java
책 제목과 내용을 기반으로 AI 커버 이미지를 생성.
-generateCover(CoverRequest): 표지 이미지 생성

## Domain/member
### member/controller/MemberController.java
회원 관련 API를 제공합니다.

### member/dto/LoginRequest.java & LoginResponse.java
회원 가입/로그인 요청 DTO (로그인 ID, 비밀번호)
회원 가입/로그인 응답 DTO (회원 ID)

### member/entity/Member.java
회원 엔티티. 로그인 ID, 비밀번호, 생성일자, 수정일자 관리.

### mamber/repository/MemberRepository.java
JpaRepository<Member, Integer>
findByLoginId(String loginId): 로그인 ID로 회원 조회

### member/service/MemberService.java & MemberServiceImpl.java
회원 가입 및 로그인 로직.
signup(LoginRequest): 중복 로그인 ID 검사 후 회원 등록
login(LoginRequest): 로그인 ID, 비밀번호 확인
findMember(Integer memberId): ID로 회원 조회

## Global
### base/entity/BaseEntity.java
공통적으로 사용하는 베이스 엔티티 클래스입니다.
@CreatedDate와 @LastModifiedDate를 통해 생성일자, 수정일자 자동 관리.

### openai/AiCoverClient.java
- 역할 : 제목·내용을 받아 OpenAI Images API(POST /v1/images/generations)를 호출하고, 생성된 표지 이미지 URL을 돌려줌.
- 주요 흐름
  1. WebClient 초기화 — Bearer {API-KEY} 헤더 자동 포함.
  2.  프롬프트 빌드 → 모델(dall-e-2), 크기(1024×1024)와 함께 JSON 요청 전송.
  3. 응답 JSON data[0].url 추출 후 반환.
- 내부 DTO : OpenAiImageRequest, OpenAiImageResponse (record)로 직렬화/역직렬화.
- 의존성 : Spring WebFlux(WebClient) ‧ Jackson.

### global/response
| 파일                   | 역할                             | 주요 포인트                             |
|----------------------|--------------------------------|------------------------------------|
| CustomException.java | 모든 도메인 예외의 공통 부모 | ErrorCode, message 보관 및 신규 예외 추가   |
| GlobalExceptionHandler.java      | 전역 예외 처리기 (@RestControllerAdvice)	       | 검증오류, CustomException 등 공통 핸들링     |
| Response.java                 | API 표준 래퍼    | success() / error() 팩토리 메서드로 간편 생성 |
| SuccessCode.java                  | 성공 메시지 사전 (enum)	     | OK(200, "성공입니다") -> 모든 성공 응답에서 재사용 |
| ErrorCode.java              | 기능별 오류 사전 (enum)      | 각 항목 = HTTP 상태 + 기본 메시지            |


---


## 🔑 API Key / 오류 처리
| 상태  | 예외                              | 설명                     |
|-----|---------------------------------|------------------------|
| 400 | `UnsupportedParameterException` | 모델·사이즈 등 지원되지 않는 파라미터  |
| 401 | `InvalidApiKeyException`        | 잘못된 / 누락된 OpenAIAPI 키 |
| 403 | `OrganizationAuthException`     | 조직(Org) 권한 부족          |
| 5XX | `CoverGenerationException`      | 네트워크·타임아웃 등 기타 표지 생성 실패                     |


---

## 🏗️ 기술 스택

- **Java 17**, **Spring Boot 3.5.0**
- **Spring Data JPA (Hibernate 6)** — H2 인메모리 DB
- **Spring WebFlux WebClient** — OpenAI 호출
- **Lombok**, **MapStruct**, **Jakarta Validation**
- **Gradle**

---

## ⚙️ 환경 설정

`src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: update

openai:
  api-key: sk-************************               
  image:
    model: dall-e-2
    size: 1024x1024
