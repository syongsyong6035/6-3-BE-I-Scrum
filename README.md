🌸 DateNow : LLM 기반 데이트 코스 추천 앱
<p align="center"> <img src="./docs/logo.png" width="260" alt="DateNow logo" /> </p>

원하는 분위기를 고르면 AI가 코스를 추천해줘요!
추천받은 코스는 직접 편집하고, 사진과 함께 저장할 수 있어요.
다른 사람들의 추천 코스도 확인해보세요!

<p align="center"> <img src="https://img.shields.io/badge/Java-21-007396?logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white"/> <img src="https://img.shields.io/badge/MongoDB-Atlas-47A248?logo=mongodb&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-Cloud-DC382D?logo=redis&logoColor=white"/> <img src="https://img.shields.io/badge/WebSocket-RandomChat-85EA2D"/> <img src="https://img.shields.io/badge/Gemini%20API-Google%20AI-4285F4?logo=google&logoColor=white"/> <img src="https://img.shields.io/badge/LangChain4j-Java-FF6F00"/> <img src="https://img.shields.io/badge/Kakao%20Map-API-FFCD00"/> </p>
목차

1. 기획 배경

2. 페르소나

3. Pain Point & 해결 방향

4. DateNow의 차별화 포인트

5. 주요 기능

6. 화면 구성

7. 시스템 아키텍처 & ERD

8. 기술 스택

9. 빠른 시작

10. API 문서

11. 폴더 구조

12. 팀원

1️⃣ 기획 배경
<p align="center"><img width="1357" height="737" alt="image" src="https://github.com/user-attachments/assets/88bda8bb-d88c-4ac6-9bec-df2ba4083cf5" />
</p>
2️⃣ 페르소나
<p align="center"><img width="1351" height="760" alt="image" src="https://github.com/user-attachments/assets/77b64fc0-74d6-4423-8efd-8ce78344b21d" />
</p>
3️⃣ Pain Point & 해결 방향
<p align="center"><img width="1316" height="687" alt="image" src="https://github.com/user-attachments/assets/becc99eb-42a2-4218-93ae-394cf32dfadc" />
</p>

장소 다양성 부족 → 키워드/해시태그 기반 탐색 + 에디터 픽

AI 추천 불투명 → Gemini + LangChain4j + RAG로 근거 기반 응답

사용자 성향 반영 부족 → 분위기 선택 + 코스 직접 편집(지도로 장소 추가)

사용성 ⇧ → 비속어 필터링, 신고/블락, 인증/비밀번호 재설정 지원

4️⃣ DateNow의 차별화 포인트
<p align="center"><img width="1296" height="706" alt="image" src="https://github.com/user-attachments/assets/17b3ae89-e48e-442d-8b13-f0543c8d086f" />
</p>

AI 추천 + 지도 API + 사용자 정의 코스 제작

코스 공개/비공개 선택

유저 추천/에디터 픽 큐레이션

편집/공유에 최적화된 코스 앨범 UX

5️⃣ 주요 기능
로그인 & 회원가입

GitHub 소셜 로그인 및 일반 로그인 지원

이메일 인증/비밀번호 재설정(SMTP)

중복 체크 및 예외 처리

<table width="100%"> <tr> <td width="50%" align="center"><img width="1010" height="899" alt="image" src="https://github.com/user-attachments/assets/8b6fea09-7977-46f3-8643-2e66c275720d" />
</td> <td width="50%" align="center"><img width="1266" height="1226" alt="image" src="https://github.com/user-attachments/assets/c74d872e-eb5b-4d58-8bcd-0734ad93ce28" />
<img width="1251" height="1264" alt="image" src="https://github.com/user-attachments/assets/5bed9f75-5487-4ac9-bee3-8353280da197" />

</td> </tr> </table>
메인 페이지 & 추천 코스

에디터 픽, 유저 추천 코스 섹션

좋아요 상위 게시물 배너 노출

해시태그/키워드 필터링

<table width="100%"> <tr> <td width="50%" align="center"<img width="1264" height="1259" alt="image" src="https://github.com/user-attachments/assets/14351b93-7f32-4514-8342-157d3677b295" />
></td> <td width="50%" align="center"><img width="1265" height="1252" alt="image" src="https://github.com/user-attachments/assets/8cd1d486-afe1-476c-aa8a-7ad158b26d9d" />
<img width="1261" height="1253" alt="image" src="https://github.com/user-attachments/assets/8367ab6e-d675-44cf-8eb5-08de38ce3afe" />

</td> </tr> </table>
🧠 분위기 기반 AI 추천 코스

분위기 선택 → Gemini + LangChain4j (RAG) 기반 추천

추천 리스트를 바탕으로 나만의 코스 편집/저장

<table width="100%"> <tr> <td align="center"><img src="./docs/screens/ai-1.png" /></td> <td align="center"><img src="./docs/screens/ai-2.png" /></td> </tr> <tr> <td align="center"><img src="./docs/screens/ai-3.png" /></td> <td align="center"><img src="./docs/screens/ai-4.png" /></td> </tr> </table>
리뷰 & 랜덤 채팅

코스별 댓글/후기

WebSocket + Redis 기반 랜덤 채팅

<table width="100%"> <tr> <td width="50%" align="center"><img width="1255" height="1260" alt="image" src="https://github.com/user-attachments/assets/0ca362d1-d36d-47dc-bc89-559b4b0a362e" />
</td> <td width="50%" align="center"><img width="893" height="881" alt="image" src="https://github.com/user-attachments/assets/dc05e68b-46b7-45bb-a464-2db8b9aa97e3" />
</td> </tr> </table>
6️⃣ 🚀 주요 기능
DateNow는 특별한 데이트 경험을 선사하기 위해 다양한 핵심 기능을 제공합니다.

### AI 데이트 코스 추천<br>
  원하는 분위기를 선택하면 Gemini와 LangChain4j를 활용한 AI가 맞춤형 데이트 코스를 제안합니다.
### 랜덤 채팅
  WebSockets와 Redis를 통해 새로운 사람들과 실시간으로 소통하고 데이트 아이디어를 자유롭게 공유해 보세요.
### 데이트 코스 편집 및 저장
  Kakao Map API를 기반으로 장소를 검색하여 추천받은 코스를 직접 편집할 수 있어요. 나만의 특별한 코스를 사진과 함께 저장하고 등록해 보세요.
### 추천 코스 탐색 및 검색
  다른 유저들이 공유한 추천 코스(유저, 에디터 픽)를 확인하고, 해시태그와 키워드를 이용하여 편리하게 원하는 코스를 찾아볼 수 있습니다.
### 안전하고 편리한 서비스
  비속어 필터링으로 건전한 소통 환경을 조성합니다.<br>
  이메일 인증 및 비밀번호 재설정(SMTP) 기능을 통해 사용자 계정을 안전하게 관리할 수 있습니다.

<br>화면 구성


7️⃣ 시스템 아키텍처 & ERD
<p align="center"><img width="2557" height="816" alt="image" src="https://github.com/user-attachments/assets/e14a3235-f1bc-483a-ae07-e8fa3900b32f" />
</p> <p align="center"><img width="1887" height="1050" alt="image" src="https://github.com/user-attachments/assets/420c5284-e351-402f-9a68-3de4c622f44f" />
</p>
8️⃣ 기술 스택
분류	기술
💻 Language	Java 21, (선택) Kotlin 1.9
📦 Build	Maven 3.9 (또는 Gradle 8.x)
🔧 Backend	Spring Boot 3.x, Spring Security 6.x, JPA(Hibernate), JWT
🎨 Frontend	Thymeleaf, HTML, CSS, JavaScript
📡 Real-time	WebSocket, Redis Cloud
🧠 LLM & AI	Gemini API, LangChain4j, (RAG)
🗺 지도	Kakao Map API
🗃 DB	MySQL 8.0, MongoDB Atlas (VectorDB)
🧰 협업	Notion, Zoom, Discord, GitHub
9️⃣ 빠른 시작
사전 준비

JDK 21

Maven 3.9+

MySQL 8.0+ (스키마 예: datenow)

Redis, MongoDB 


🔟 API 문서

👉 /backend/APIs.md (레포 내 문서로 연결)

1️⃣1️⃣ 폴더 구조 (예시)
├─.idea
├─datenow
│  ├─.mvn
│  │  └─wrapper
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─grepp
│      │  │          └─datenow
│      │  │              ├─app
│      │  │              │  ├─controller
│      │  │              │  │  ├─api
│      │  │              │  │  │  ├─auth
│      │  │              │  │  │  │  └─payload
│      │  │              │  │  │  ├─chat
│      │  │              │  │  │  ├─course
│      │  │              │  │  │  ├─member
│      │  │              │  │  │  ├─place
│      │  │              │  │  │  └─recommend
│      │  │              │  │  └─web
│      │  │              │  │      ├─admin
│      │  │              │  │      ├─chat
│      │  │              │  │      ├─course
│      │  │              │  │      ├─member
│      │  │              │  │      │  └─payload
│      │  │              │  │      └─recommend
│      │  │              │  └─model
│      │  │              │      ├─auth
│      │  │              │      │  ├─code
│      │  │              │      │  ├─domain
│      │  │              │      │  ├─service
│      │  │              │      │  └─token
│      │  │              │      │      ├─dto
│      │  │              │      │      └─entity
│      │  │              │      ├─chat
│      │  │              │      │  ├─code
│      │  │              │      │  ├─dto
│      │  │              │      │  ├─entity
│      │  │              │      │  ├─repository
│      │  │              │      │  └─sevice
│      │  │              │      ├─course
│      │  │              │      │  ├─dto
│      │  │              │      │  ├─entity
│      │  │              │      │  ├─repository
│      │  │              │      │  └─service
│      │  │              │      ├─image
│      │  │              │      │  ├─entity
│      │  │              │      │  ├─repository
│      │  │              │      │  └─service
│      │  │              │      ├─like
│      │  │              │      │  ├─dto
│      │  │              │      │  ├─entity
│      │  │              │      │  ├─repository
│      │  │              │      │  └─service
│      │  │              │      ├─member
│      │  │              │      │  ├─dto
│      │  │              │      │  ├─entity
│      │  │              │      │  ├─repository
│      │  │              │      │  └─service
│      │  │              │      ├─place
│      │  │              │      │  ├─document
│      │  │              │      │  ├─dto
│      │  │              │      │  │  └─mainpage
│      │  │              │      │  ├─entity
│      │  │              │      │  ├─repository
│      │  │              │      │  └─service
│      │  │              │      ├─recommend
│      │  │              │      │  ├─code
│      │  │              │      │  ├─dto
│      │  │              │      │  └─service
│      │  │              │      └─review
│      │  │              │          ├─dto
│      │  │              │          ├─entity
│      │  │              │          ├─repository
│      │  │              │          └─service
│      │  │              └─infra
│      │  │                  ├─auth
│      │  │                  │  ├─oauth2
│      │  │                  │  │  └─user
│      │  │                  │  └─token
│      │  │                  │      ├─code
│      │  │                  │      └─filter
│      │  │                  ├─chat
│      │  │                  │  └─config
│      │  │                  ├─config
│      │  │                  ├─entity
│      │  │                  ├─error
│      │  │                  │  └─exception
│      │  │                  │      ├─course
│      │  │                  │      ├─image
│      │  │                  │      └─member
│      │  │                  ├─event
│      │  │                  ├─init
│      │  │                  ├─llm
│      │  │                  │  └─config
│      │  │                  ├─mail
│      │  │                  ├─response
│      │  │                  └─util
│      │  │                      └─file
│      │  └─resources
│      │      ├─static
│      │      │  ├─css
│      │      │  ├─images
│      │      │  └─js
│      │      └─templates
│      │          └─fragments
│      └─test
│          └─java
│              └─com
│                  └─grepp
│                      └─datenow
│                          └─app
│                              ├─controller
│                              │  └─api
│                              │      └─place
│                              └─model
│                                  └─chat
│                                      └─sevice
└─mail
    ├─gradle
    │  └─wrapper
    └─src
        ├─main
        │  ├─kotlin
        │  │  └─com
        │  │      └─grepp
        │  │          └─spring
        │  │              ├─app
        │  │              │  ├─controller
        │  │              │  │  └─payload
        │  │              │  └─model
        │  │              │      └─code
        │  │              └─infra
        │  │                  ├─annotation
        │  │                  ├─config
        │  │                  ├─error
        │  │                  │  └─exceptions
        │  │                  ├─event
        │  │                  ├─mail
        │  │                  ├─response
        │  │                  └─security
        │  └─resources
        │      ├─static
        │      │  ├─css
        │      │  │  ├─error
        │      │  │  └─mail
        │      │  ├─img
        │      │  │  └─book
        │      │  └─js
        │      │      └─module
        │      └─templates
        │          └─mail
        └─test
            └─kotlin
                └─com
                    └─grepp
                        └─spring
                            └─infra
                                └─mail


1️⃣2️⃣ 팀원
역할	프로필
<img src="https://avatars.githubusercontent.com/qkqehenr7?s=100" width="90"/> 최동준	Leader
<img src="https://avatars.githubusercontent.com/LightandSaltt?s=100" width="90"/> 박승민	Member
<img src="https://avatars.githubusercontent.com/aronmin?s=100" width="90"/> 손창민	Member
<img src="https://avatars.githubusercontent.com/syongsyong6035?s=100" width="90"/> 김가희	Member
