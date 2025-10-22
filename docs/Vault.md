# Vault란?
- HashiCorp에서 개발한 Vault는 암호화 키(Key) 관리 전용 서버.
- 중요한 정보를 중앙 집중식으로 안전하게 관리하고, 이곳에 접근하기 위한 인증도 제공
- 일반적으로는 상용 클라우드, K8S와 연동되어 많이 사용됨.
- 모두 조금씩은 다른 Flow 사용되지만 기본 원리는 동일하다.

1. Application 기동 시, Vault 사용을 위한 token 정보 Get
2. 얻어 온 token 기반, Vault에 원하는 시크릿 Get 요청
3. 사용

## DB 암호화를 위한 Vault 활용 Spec 정의
1. Vault에 DB 암호화를 위한 대칭키(Key)를 정의
2. 정의된 Key에 접근할 수 있는 정책(Policy) 정의
3. Vault에 접근해서 대칭키를 얻어오기 위해, Service 기동시 token 발급
4. 발급된 token을 사용하여, Vault에서 대칭키를 얻어와
5. 얻어온 대칭키를 사용하여, DB 암호화/복호화 수행
6. 암호화 된 DB 데이터 확인

## Vault 플로우차트
```mermaid
flowchart TD
    A[사용자 (Browser / Client)] -->|HTTPS 요청| B[Spring Server]
    subgraph Spring Server
        B1[Spring Security<br/>(인증/인가)]
        B2[Service 계층<br/>암호화 로직 수행]
        B3[Vault Client<br/>(Key 요청/갱신)]
        B4[Repository]
    end

    B --> B1 --> B2
    B2 -->|Key 요청| B3
    B3 -->|Key 발급 응답| B2
    B2 -->|데이터 AES 암호화| B4
    B4 --> C[(Database)]
    C -->|암호문 저장| C

    subgraph Vault Server
        V1[Key Storage (Master Key)]
        V2[Policy & Access Control]
    end
    B3 -->|API 요청| Vault Server
    Vault Server -->|응답 (암호화 키 반환)| B3

    %% 설명 노트
    note1["Vault: Key 관리 전용 서버 (마스터 키·정책·버전 관리)"]
    note2["Spring: Vault Client로부터 키를 받아 AES로 암호화/복호화 수행"]
    note3["DB: 암호문만 저장 (평문 없음)"]
    note1 -.-> Vault Server
    note2 -.-> B2
    note3 -.-> C
```
### 단계별 흐름
| 단계 | 구간                          | 설명                          |
| -- | --------------------------- | --------------------------- |
| ①  | 사용자 → Spring 서버             | HTTPS 통신으로 요청 (보안 전송)       |
| ②  | Spring Security             | 인증/인가 수행 (JWT 또는 세션 기반)     |
| ③  | Service 계층                  | DB 저장 전 민감정보 암호화 수행         |
| ④  | Vault Client → Vault Server | AES Key 요청 및 수신             |
| ⑤  | Service 계층                  | Vault로부터 받은 Key로 암호화/복호화 수행 |
| ⑥  | Repository → DB             | 암호문만 저장 (평문 저장 금지)          |

### 구성 요소 역할
| 구성 요소                   | 역할                  | 주요 기술                            |
| ----------------------- | ------------------- | -------------------------------- |
| **Vault Server**        | 키 저장 및 정책 관리, 접근 제어 | HashiCorp Vault, AWS KMS         |
| **Spring Vault Client** | Vault와 통신, 키 요청/갱신  | Spring Cloud Vault, RestTemplate |
| **Service Layer**       | 암복호화 수행, 키 캐싱 관리    | AES/GCM, Jasypt, Vault SDK       |
| **Database**            | 암호문 저장              | MySQL / PostgreSQL               |
| **Spring Security**     | 인증·인가 관리            | JWT, OAuth2, Session             |

# 요약
> Vault는 Spring 서버가 DB 데이터를 AES 등으로 암호화할 때, 암호화 키를 안전하게 발급·관리하는 전용 키 관리 서버이다.