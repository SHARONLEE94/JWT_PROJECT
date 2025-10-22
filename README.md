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