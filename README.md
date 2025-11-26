# JWT Authentication Lab (Spring MVC + Core Module)
> 순수 Java + Spring MVC 기반 JWT 인증 시스템 실습 프로젝트
Access Token / Refresh Token 발급, 검증, 재발급, 전역 예외처리 및 MockMvc 테스트까지 구현 완료.

## 프로젝트 개요
| 구분        | 내용                                                                 |
| --------- | ------------------------------------------------------------------ |
| **프로젝트명** | JWT Authentication Lab                                             |
| **주요 목표** | Spring MVC 기반 JWT 발급 및 검증 구조 직접 구현                                 |
| **구성**    | `jwtcore` (핵심 로직) + `jwtmvc` (웹 API 계층)                            |
| **상태**    | ✅ v1 완성 — JWT 핵심 기능 + 통합 예외처리 + 테스트 완료                             |
| **확장 예정** | Step 7 - 로깅 / traceId / timestamp 추가 → Step 8 - Spring Security 통합 |

## 모듈 구조
```text
root/
 ├── pom.xml               # 멀티 모듈 루트 설정
 ├── jwtcore/              # JWT 핵심 로직 모듈
 │    ├── model/           # User, Token DTO
 │    ├── util/            # JwtUtil (토큰 생성/검증)
 │    ├── service/         # AuthService (비즈니스 로직)
 │    └── store/           # RefreshStore (임시 저장소)
 └── jwtmvc/               # Spring MVC 모듈
      ├── controller/      # AuthController (REST API)
      ├── common/          # ApiResponse, GlobalExceptionHandler
      ├── JwtMvcApplication.java  # Spring Boot 실행 진입점
      └── test/            # JUnit 테스트 (MockMvc 기반)
```
## 기술 스택
| 구분             | 사용 기술                                     |
| -------------- | ----------------------------------------- |
| Language       | Java 21                                   |
| Framework      | Spring Boot 3.2 (MVC only)                |
| Build Tool     | Maven                                     |
| JWT Library    | `io.jsonwebtoken:jjwt-api:0.11.5`         |
| Test Framework | JUnit5 + MockMvc                          |
| Lombok         | `@Getter`, `@AllArgsConstructor` 등 코드 간결화 |
## 주요 기능 요약
| 구분              | 기능                   | 설명                                 |
| --------------- | -------------------- | ---------------------------------- |
| **Login API**   | `/api/auth/login`    | 사용자 로그인 시 Access/Refresh Token 발급  |
| **Refresh API** | `/api/auth/refresh`  | Refresh Token으로 Access Token 재발급   |
| **Secure API**  | `/api/auth/secure`   | JWT 인증이 필요한 보호된 리소스 접근             |
| **전역 예외 처리**    | `@ControllerAdvice`  | JWT 예외, 만료 예외, 일반 예외 일괄 처리         |
| **응답 통일**       | `ApiResponse<T>`     | `{ success, data, message }` 구조 통일 |
| **테스트**         | MockMvc 기반 단위/통합 테스트 | /login, /refresh, /secure 자동 검증    |
## 예시 API 요청/응답
### 로그인 성공 - POST /api/auth/login
```json
// Request
{
  "id": "user",
  "password": "pass",
  "name": "예린"
}

// Response
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "message": null
}
```
### 보호된 API 요청 - GET /api/auth/secure
```json
// Header
Authorization: Bearer <AccessToken>

// Response
{
"success": true,
"data": "접근 허용됨 (Protected Resource)",
"message": null
}

// 토큰 만료 또는 유효하지 않음
{
"success": false,
"data": null,
"message": "Access Token이 만료되었습니다."
}
```
### JUnit 테스트 (MockMvc)
| 테스트명                 | 목적                            |
| -------------------- | ----------------------------- |
| `loginSuccessTest()` | 로그인 성공 시 토큰 발급 검증             |
| `loginFailTest()`    | 잘못된 비밀번호 시 401 응답             |
| `secureAccessTest()` | Access Token 유효 시 접근 허용       |
| `refreshTokenFlow()` | Refresh Token으로 Access 재발급 검증 |
### 학습 포인트
| 주제           | 학습 내용                              |
| ------------ | ---------------------------------- |
| JWT 핵심 구조    | Header / Payload / Signature 직접 구성 |
| Stateless 인증 | 서버 세션 없이 토큰으로 인증 상태 유지             |
| 예외처리         | `@ControllerAdvice` 기반 전역 오류 핸들링   |
| 테스트 전략       | MockMvc로 REST API 전체 시나리오 검증       |
| 구조 분리        | core / mvc 모듈로 재사용 가능한 설계 패턴 구현    |
## 실행 방법
```text
# 1️⃣ 멀티모듈 빌드
mvn clean install

# 2️⃣ Spring MVC 실행
cd jwtmvc
mvn spring-boot:run

# 3️⃣ API 테스트 (Postman / cURL)
POST http://localhost:8080/api/auth/login

```