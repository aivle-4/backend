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
