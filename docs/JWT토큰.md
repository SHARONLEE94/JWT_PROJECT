# 인증의 목적
- 정상적인 고객이 맞는지?
- 위변조 되지 않은, 정식 앱을 통한 접근이 맞는지?
- 마지막 로그인으로부터, 특정 기간 미사용하지는 않았는지?

# JWT 토큰(JSON Web Token)
> "JWT는 Authorization 헤더의 Bearer 토큰 형태로 요청에 포함된다.
> 서버는 이 토큰의 서명을 검증해서 사용자를 인증한다."

- 인증과 세션 관리를 위해 사용되는 개념
- 토큰 기반의 인증 방식으로, 사용자의 중요 개인정보 등이 노출되지 않고 인증을 수행할 수 있다.
- 서버는 사용자가 로그인에 성공하면, 사용자 정보를 담은 JWT 토큰을 생성하여 클라이언트에 전달한다. 
  -> token안에 정보를 포함시키는 것이 가능하다.
  -> 유효 시간이 필수적으로 존재한다.
- 웹 표준(RFC-7519d)으로서 두 개체(클라이언트-서버)간 JSON 객체를 사용하여 정보를 안전하게 전달하기 위한 방법

## JWT 토큰의 구조
- Header : 어떤 알고리즘으로 서명했는지
- Payload : 사용자 정보
- Signature : 위 내용이 위조되지 않았음을 증명하는 전자 서명

```text
// JWT(JSON Web Token)의 구조 - "xxxxx.yyyyy.zzzzz"
// 실제로는 더 길다.
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MzQ3NjgwMDB9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```
- JWT는 Header, Payload, Signature의 세 부분으로 구성된다.
- Header는 알고리즘 정보,
- Payload는 사용자 정보,
- Signature는 변조 방지를 위한 서명이다
- 각 부분을 Base64Url로 인코딩해, 마침표(.)로 연결한 것이 JWT이다.

| 구분            | 설명                       | 예시 (단축형)                                                                       |
| ------------- | ------------------------ | ------------------------------------------------------------------------------ |
| **Header**    | 어떤 알고리즘으로 서명했는지          | `{"alg": "HS256", "typ": "JWT"}`                                               |
| **Payload**   | 사용자 정보, 권한, 만료시간 등       | `{"sub": "user123", "role": "USER", "exp": 1734768000}`                        |
| **Signature** | 위 두 부분이 변조되지 않았는지 확인용 서명 | `HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)` |
** Signature
- Header와 Payload를 인코딩한 후, 서버의 비밀 키(secret)로 해시(HMAC)해서 생성
- 이 서명을 통해 JWT가 위변조되지 않았음을 검증
- 서버는 비밀 키를 안전하게 관리해야 하며, 노출 시 보안 위험이 발생

## JWT 토큰의 동작 방식
1. 사용자가 로그인하면, 서버는 사용자 정보를 담은 JWT 토큰 1개를 생성
2. 토큰 안에는 사용자 ID, 권한, 만료 시간 등의 정보가 포함
3. 클라이언트(브라우저나 앱)는 이 토큰을 로컬스토리지나 쿠키에 저장
4. 이후 요청 시마다, 클라이언트는 JWT 토큰을 HTTP 헤더(Authorization: Bearer <JWT_TOKEN>)에 포함시켜 서버로 전송
5. 서버는 토큰을 검증하여, 유효한 토큰인지 확인 후 요청을 처리


## 장점
1. 서버가 별도로 "세선 저장소"를 안써도 됨 -> Stateless
2. 확장성이 좋음(토큰만 같으면 서버가 달라도 인증 가능)

## 단점
1. JWT 안에는 유효기간이 있음
2. 한번 발급된 JWT 토큰은 서버에서 강제 만료시키기 어려움
3. 유효기간을 짧게 하면 자주 로그인해야 하고, 길게하면 해킹 시 피해가 커짐

---
### 문제1. 발급 된 JWT 토큰이 만료되면?
    -> 시간을 늘리면 되지 않을까?
    -> 토큰의 유효기간을 늘리는 것은 보안 위험을 증가시킨
### 문제2. JWT 토큰이 탈취 당하면?
    -> 클라이언트에 저장되는 정보는 언제든 탈취 가능성을 생각해야 한다.
### 문제3. 보안을 챙기면서, 고객의 불편함(잦은 JWT Token 재발급 액션-로그인)을 줄일 수는 없나?
    -> Refresh Token 개념 도입
    -> 짧은 유효 기간의 Access Token(JWT Token)과 긴 유효 기간의 Refresh Token을 2개를 함께 사용하여 Lifecycle을 관리

    ①. 요청 시에는 항상 Access Token(JWT Token)을 사용
    ②. Access Token이 만료되면, Refresh Token을 사용하여 새로운 Access Token을 재발급
    ③. Refresh Token 탈취 시, 서버에서 비활성화
    ④. JWT Token 탈취 리스크 적음
---
# Access Token + Refresh Token 함께 사용하는 경우
> JWT의 단점을 보완하기 위해 두 개의 토큰을 함께 사용하는 방식
> Access Token은 짧은 유효기간, Refresh Token은 긴 유효기간을 가짐
 
## Access Token + Refresh Token 동작 방식
1. 로그인 시 서버가, Access Token(JWT)과 Refresh Token 2개를 발급
2. 클라이언트는 Access Token으로 API를 호출
3. Access Token이 만료되면, 클라이언트는 Refresh Token을 사용하여 새로운 Access Token을 요청
4. Refresh Token도 만료되거나 위조가 의심되면, 다시 로그인해야 한다.

## 장점
1. 보안 향상: Access Token이 유출되어도 짧은 시간만 유효
2. 편의성: 사용자는 매번 로그인하지 않아도 됨(Refresh Token으로 재발급 가능)
3. 통제 가능: 서버에서 Refresh Token을 DB에 저장해 "로그아웃 처리"도 가능

## 단점
1. 토큰 2개 관리해야 해서 구조가 복잡해짐
2. Refresh Token은 DB나 Redis에 저장하고 관리해야 함(Stateless하지 않음)

## 예시 코드
 ```http response
  // http response
  
  HTTP/1.1 200 OK
  Content-Type: application/json
  
  {
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
```

```http request
// http request

GET /api/v1/user/profile HTTP/1.1
Host: example.com
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

// 1. Authorization 헤더를 읽고 → Bearer 다음의 문자열(JWT 토큰)을 추출
// 2. JWT의 서명을 검증 -> 위변조 여부 확인, 유효기간 확인
// 3. 유효한 토큰이면, Payload에서 사용자 정보 추출 -> 인증
// 4. 유효하지 않은 토큰이면, 401 Unauthorized 응답 반환
```

# 세션(Session) vs JWT 토큰 비교
| 항목        | 세션(Session)          | JWT                        |
| --------- | -------------------- | -------------------------- |
| 쿠키에 담기는 것 | 세션 ID (서버에서 관리되는 키)  | JWT 자체 (정보와 서명이 들어있음)      |
| 서버 역할     | 세션 저장소에서 로그인 정보 조회   | 토큰의 서명 검증만 수행              |
| 상태 유지 방식  | 서버가 상태를 저장(Stateful) | 서버가 상태를 저장하지 않음(Stateless) |
| 확장성       | 서버 여러 대면 세션 공유 필요    | 서버 여러 대여도 토큰만 있으면 인증 가능    |
** 서버가 상태를 저장한다(Stateful)
- **서버**가 사용자의 로그인 정보나 현재 상태를 직접 기억하고 관리한다는 뜻.

# JWT 로그아웃(또는 토큰 무효화) 방법
- JWT는 기본적으로 Stateless하기 때문에, 서버에서 토큰을 강제로 무효화하기 어렵다.
- 하지만 다음과 같은 방법으로 로그아웃 기능을 구현할 수 있다.
## 1. 블랙리스트(Blacklist) 방식
> 이미 발급된 Access Token이 남은 유효기간이 있어도, 더 이상 신뢰할 수 없는 토큰으로 등록해 두는 방식
> 즉, 만료되기 전이라도 무효화

### 동작 방식
1. 사용자가 로그아웃 요청을 보냄
2. 서버는 JWT 토큰을 Blacklist 저장소(보통 Redis)에 등록
```
key: blacklisted_token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
value: expiresAt=2025-10-21T18:00
```
3. 이후 요청 시, 서버는 토큰이 Blacklist에 있는지 확인.
4. 있으면 401 Unauthorized 응답 반환

## 2. 토큰 만료 정책
> Access Token의 유효기간을 짧게 설정하여, 탈취된 토큰의 사용 가능 시간을 최소화하는 방법

### 동작 방식
1. Access Token이 짧게 끝나기 때문에, 해커가 탈취하더라도 짧은 시간내에 자동 만료돼서 피해 최소화
2. 사용자는 Refresh Token을 통해 새로운 Access Token을 발급받아 계속 이용 가능

## 3. Refresh Token 저장소(DB 또는 Redis)
> Refresh Token은 서버가 "누구에게 어떤 토큰을 줬는지" 추적하기 위해 DB나 Redis에 저장

### 동작 방식
1. 로그인 시 서버가 Refresh Token을 생성하고, DB나 Redis에 저장
```diff
user_id | refresh_token | expires_at
-------------------------------------
1       | eyJhbGciOiJI... | 2025-11-01
```
2. 클라이언트가 Access Token 갱신 요청 시, 서버는 DB의 Refresh Token과 비교 -> 일치하면 새 Access Token 발급
3. 로그아웃 시 Refresh Token을 DB에서 삭제하여 더 이상 갱신 불가