# 🌸 DateNow : LLM 기반 데이트 코스 추천 앱

<p align="center">
  
</p>

원하는 분위기를 고르면 **AI가 데이트 코스**를 추천해줘요!  
추천받은 코스는 **직접 편집**하고 **사진과 함께 저장**할 수 있어요.  
다른 사람들의 **추천 코스**도 확인해보세요!

## 🛠 기술 스택

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/MongoDB-Atlas-47A248?style=for-the-badge&logo=mongodb&logoColor=white"/>
  <img src="https://img.shields.io/badge/Redis-Cloud-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
  <img src="https://img.shields.io/badge/WebSocket-RandomChat-85EA2D?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/LangChain4j-Java-FF6F00?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Gemini_API-Google_AI-4285F4?style=for-the-badge&logo=google&logoColor=white"/>
  <img src="https://img.shields.io/badge/Kakao_Map-API-FFCD00?style=for-the-badge"/>
</p>


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

- **장소 다양성 부족** → 키워드/해시태그 기반 탐색 + 에디터 픽  
- **AI 추천 불투명** → Gemini + LangChain4j + RAG로 **근거 기반 응답**  
- **사용자 성향 반영 부족** → 분위기 선택 + **지도로 장소 추가/편집**  
- **사용성 향상** → 비속어 필터링, 신고/블락, 인증/비밀번호 재설정


4️⃣ DateNow의 차별화 포인트
<p align="center"><img width="1296" height="706" alt="image" src="https://github.com/user-attachments/assets/17b3ae89-e48e-442d-8b13-f0543c8d086f" />
</p>

1. **AI 추천 + 지도 API + 사용자 정의 코스 제작**  
2. 코스 **공개/비공개** 선택  
3. **유저 추천/에디터 픽** 큐레이션  
4. 편집·공유에 최적화된 **코스 앨범 UX**

5️⃣ 주요 기능
### 🔐 로그인 & 회원가입
- GitHub **소셜 로그인** 및 일반 로그인  
- **이메일 인증/비밀번호 재설정(SMTP)**  
- 아이디/닉네임/이메일 **중복 검증**
  
### 회원가입
아이디, 닉네임, 이메일 등 **중복 입력에 대한 예외처리**를 했어요.<br>
소셜 로그인을 하는 경우 필요한 정보만 입력하시면 돼요.
이메일 인증 메일을 통해 회원가입이 완료되니, 꼭 본인의 이메일을 사용해주세요!

<table width="100%" align="center">
  <tr>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/0ad373e9-395e-401b-910c-8db8014e1115" alt="로그인 화면" style="width:100%; height:auto; display:block;" />
    </td>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/b00666ab-eb3a-4f45-a043-30d12c6f1daf" alt="회원가입 화면" style="width:100%; height:auto; display:block;" />
    </td>
  </tr>
</table>

### 🏠 메인 & 추천 코스
- **에디터 픽**, **유저 추천 코스** 섹션  
- **좋아요 상위** 게시물 배너 노출  
- **해시태그/키워드** 필터링

<table width="100%" align="center">
  <tr>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/23c21ee6-9751-4a9f-bc35-0a25afa2de73" alt="메인 페이지 화면" style="width:100%; height:auto; display:block;" />
    </td>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/1e3909b7-57f0-47d6-8d72-d93bf7b712b0" alt="추천 데이트 코스 상세 화면" style="width:100%; height:auto; display:block;" />
    </td>
  </tr>
</table>

### 🧠 분위기 기반 AI 추천
- 분위기를 선택하면 **Gemini + LangChain4j(RAG)** 기반으로 코스를 제안  
- 제안 목록을 바탕으로 **나만의 코스 편집/저장**

<table width="100%" align="center">
  <tr>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/988f8a2a-9304-4d0b-baf9-0d6238d5751a" alt="AI 추천 코스 화면" style="width:100%; height:auto; display:block;" />
    </td>
    <td width="50%" align="center" style="padding: 0 5px;">
    <img src="https://github.com/user-attachments/assets/2a9809c8-b72a-4644-a9fd-43030e38eb4c" alt="AI 추천 코스 화면" style="width:100%; height:auto; display:block;" />  
    </td>
  </tr>
  <tr>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/acc37b2f-f338-4349-a81c-a103900b4d62" alt="AI 추천 코스 화면" style="width:100%; height:auto; display:block;" />
    </td>
    <td width="50%" align="center" style="padding: 0 5px;">
    <img src="https://github.com/user-attachments/assets/137419c2-96a8-4f37-a7cf-a4e0f6473d38" alt="AI 추천 코스 화면" style="width:100%; height:auto; display:block;" />  
    </td>
  </tr>
</table>

## 🖼️ 데이트 코스 리뷰 & 랜덤 채팅

### 데이트 코스 리뷰
유저들이 추천한 코스에는 **댓글로 리뷰를 달 수 있어요**.

### 랜덤 채팅
혼자서 사용하기 **심심하고**, 데이트 하고 싶은 **상대방을 찾고 싶을 때!**<br>
랜덤채팅을 **정보 교류**나 **새로운 인연**을 시작해봐요!

<table width="100%" align="center">
  <tr>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/e9476869-8d98-451a-8bea-127a79ddd53b" alt="데이트 코스 리뷰" style="width:100%; height:auto; display:block;" />
    </td>
    <td width="50%" align="center" style="padding: 0 5px;">
      <img src="https://github.com/user-attachments/assets/4d3622b3-9850-4b79-b2ea-3746018959e7" alt="랜덤 채팅 메시지" style="width:100%; height:auto; display:block;" />
    </td>
  </tr>
</table>

---
6️⃣ 🚀 주요 기능 <br>
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
8️⃣  ⚙️ 기술 스택

| 분류 | 기술 |
|---|---|
| 💻 Language | Java 21, (선택) Kotlin 1.9 |
| 📦 Build | Maven 3.9 (또는 Gradle 8.x) |
| 🔧 Backend | Spring Boot 3.x, Spring Security 6.x, JPA(Hibernate), JWT |
| 🎨 Frontend | Thymeleaf, HTML, CSS, JavaScript |
| 📡 Real-time | WebSocket, Redis Cloud |
| 🧠 LLM & AI | Gemini API, LangChain4j (RAG) |
| 🗺 지도 | Kakao Map API |
| 🗃 DB | MySQL 8.0, MongoDB Atlas (VectorDB) |
| 🧰 협업 | Notion, Zoom, Discord, GitHub |

🚀 Core Troubleshooting & Performance Optimization
기존의 기능 중심 개발에서 벗어나, 실제 운영 환경에서의 병목 현상을 진단하고 **응답 속도를 약 96% 개선(15.6s → 0.6s)**한 과정을 기록했습니다.

1. N+1 문제 해결 및 DB I/O 최적화
   Issue: 코스 조회 시 루프 내에서 이미지, 좋아요, 리뷰를 개별 조회하는 N+1 문제 발생. (데이터 1,000건 기준 3,001번의 쿼리 실행)

Solution:

Fetch Join을 활용해 연관된 엔티티를 한 번에 조회하도록 쿼리 최적화.

default_batch_fetch_size 설정을 통해 컬렉션 조회 시 IN 절을 사용하여 쿼리 횟수 최소화.

Result: k6 부하 테스트 결과, 평균 응답 시간 15.6s에서 0.6s대로 단축.

2. 대량 데이터 처리를 위한 Pagination 도입
   Issue: 페이징 처리 없는 findAll() 수행으로 데이터 증가 시 힙 메모리 포화 및 응답 지연 발생.

Solution: Spring Data JPA의 Pageable을 적용하여 DB 단계에서 필요한 데이터만큼만 끊어서 조회하도록 변경.

Result: 서버 자원(CPU, RAM) 점유율 안정화 및 데이터 확장성 확보.

3. 멀티스레드 기반 동시성 제어 (Concurrency)
   Issue: 좋아요 및 게시글 수정 시 다수 사용자의 동시 접근으로 인한 데이터 정합성 이슈 우려.

Solution: 비관적 락(Pessimistic Lock) 및 트랜잭션 격리 수준 조정을 통해 데이터 무결성 보장.

9️⃣ 빠른 시작
사전 준비
- JDK **21**
- Maven **3.9+**
- MySQL **8.0+** (스키마 예: `datenow`)
- (선택) Redis, MongoDB


🔟 폴더 구조

```
datenow
 ├─ src/main/java/com/grepp/datenow
 │   ├─ app (controller, service, dto 등)
 │   ├─ model
 │   └─ infra
 ├─ src/main/resources
 │   ├─ static (css, js, images)
 │   └─ templates
 └─ test
```


## 🏃 팀원 소개


| <img src="https://img.shields.io/badge/Leader-FF5733" /> | <img src="https://img.shields.io/badge/Member-%2300264B" /> | <img src="https://img.shields.io/badge/Member-%2310069F%20" /> | <img src="https://img.shields.io/badge/Member-blue" /> |
| :--------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------------------: | :-----------------------------------------------------------: |
| <a href="https://github.com/qkqehenr7" target="_blank"><img src="https://avatars.githubusercontent.com/qkqehenr7?s=100" width="120px;" alt="최동준"/></a> | <a href="https://github.com/LightandSaltt" target="_blank"><img src="https://avatars.githubusercontent.com/LightandSaltt?s=100" width="120px;" alt="박승민"/></a> | <a href="https://github.com/aronmin" target="_blank"><img src="https://avatars.githubusercontent.com/aronmin?s=100" width="120px;" alt="손창민"/></a> | <a href="https://github.com/syongsyong6035" target="_blank"><img src="https://avatars.githubusercontent.com/syongsyong6035?s=100" width="120px;" alt="김가희"/></a> |
| **최동준** | **박승민** | **손창민** | **김가희** |
