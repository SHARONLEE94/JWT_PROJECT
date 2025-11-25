# Spring MVC + Java + Maven 기반 JWT 발급 & 검증 단순 구현 예제
> 목표: 회원 로그인 → JWT 생성 → 이후 요청 시 헤더에 JWT 담아서 인증 확인

## 기술 스택
| 구분    | 선택                | 설명                                            |
| ----- |-------------------| --------------------------------------------- |
| 언어    | Java 21+          | 최신 문법, record/class 기반 설계 용이                  |
| 라이브러리 | **jjwt 0.11.5**   | io.jsonwebtoken 패키지 — Spring 없이도 JWT 서명·검증 가능 |
| 빌드도구  | Maven             | 추후 Spring 연동 시 그대로 확장 가능                      |
| IDE   | IntelliJ / VSCode | 둘 다 무방                                        |

## 패키지 구조 설계
```lessss
src/
└── main/java/com/example/jwtcore/
    ├── model/
    │   └── User.java           // 토큰 발급에 필요한 사용자 정보
    ├── util/
    │   └── JwtUtil.java        // JWT 생성/검증 로직
    ├── service/
    │   └── AuthService.java    // 로그인, 토큰 생성/검증 담당
    └── Main.java               // 실행 진입점 (테스트용)
```

### Spring MVC로 확장 구조
```less
controller/ → AuthController.java  (API endpoint)
service/     → AuthService.java     (비즈니스 로직)
util/        → JwtUtil.java         (공통 JWT 모듈)
```

### 전체 구조 요약
```text 
src/
└── main/java/com/lab/
├── jwtcore/               ← JWT 핵심 로직 (Spring에 의존하지 않음)
│    ├── model/            ← User, AuthTokens 등 데이터 객체
│    ├── service/          ← AuthService (로그인/토큰검증/재발급)
│    └── util/             ← JwtUtil (토큰 생성/검증)
│
├── jwtmvc/                ← Spring MVC 기반 웹 서비스
│    ├── controller/       ← AuthController(API 엔드포인트)
│    ├── config/           ← (필요시) WebConfig, Filter 등
│    └── JwtMvcApplication ← 스프링 부트 진입점 (서버 실행)
│
└── Main.java              ← 콘솔용 테스트 실행 진입점
```

### 역할 정리
| 클래스                                | 위치                          | 역할                                                                  |
| ---------------------------------- | --------------------------- | ------------------------------------------------------------------- |
| **Main.java**                      | `com.lab`                   | 콘솔 기반 실행 (Spring 없이 순수 Java 실행)<br> → 토큰 생성, 검증, 만료 테스트 등 간단 실행용    |
| **JwtMvcApplication.java**         | `com.lab.jwtmvc`            | Spring Boot 서버 실행 진입점<br> → 실제 HTTP 요청(`/api/auth/...`)을 처리         |
| **AuthController.java**            | `com.lab.jwtmvc.controller` | `/api/auth/login`, `/api/auth/refresh`, `/api/auth/secure` 엔드포인트 정의 |
| **AuthService.java**               | `com.lab.jwtcore.service`   | 로그인, 토큰 재발급 등 비즈니스 로직 담당                                            |
| **JwtUtil.java**                   | `com.lab.jwtcore.util`      | JWT 생성/검증 유틸리티 (Spring에 의존 X)                                       |
| **User.java**, **AuthTokens.java** | `com.lab.jwtcore.model`     | 요청/응답 데이터 객체 (DTO 개념)                                               |

### 실행 방법
①. Main.java (순수 Java 실행)
- 서버 없음 (콘솔 출력만)
- 학습용 / 단위 테스트용
예: 토큰 생성 후 3초 대기 → 검증 테스트
```css
Main → AuthService → JwtUtil
```
②. JwtMvcApplication (Spring MVC 실행)
- Spring Boot 내장 Tomcat으로 HTTP 요청 처리
- Postman 등에서 /api/auth/login 요청 가능
- 이후 /api/auth/secure 호출 시 Authorization 헤더를 검사
```css
Client(POST /login)
   ↓
AuthController
   ↓
AuthService
   ↓
JwtUtil
   ↓
ResponseEntity<AuthTokens>
```