# 매장 예약 서비스
#### Store Reservation Service

## Skills
- Java11, SpringBoot 2.5.6, gradle 8.2.1
- Spring data JPA, Spring security, JWT
- MariaDB
- JUnit5
- IntelliJ Idea



---
## 요구사항
### 인증 
회원가입, 로그인은 `global/auth`패키지에서 관리된다.
- User와 Partner는 모두 `AuthService`를 통해 회원가입, 로그인 할 수 있다.
- User는 `MemberType=ROLE_USER`을 가진다. 
- Partner는 `MemberType=ROLE_PARTNER`을 가진다. 
- Partner는 User의 서비스까지 모두 접근 가능하다.
- User는 `/partner`로 시작되는 URL에 접근할 수 없다.

### 이용자 (User)
#### 매장 검색
- 이용자는 매장을 검색하고, 상세 정보를 확인할 수 있다.
- 매장 검색시에는 정렬 방법을 설정할 수 있다.(전체, 가나다순, 평점순, 리뷰순, 거리순)
- `쿼리 파라미터 p`에 따라 페이징 처리가 된다.(1페이지부터 시작)
- 매장 상세 정보를 확인 할 수 있다. (매장 명, 매장 주소, 매장 설명, 별점, 리뷰수)

### 점장 (Partner)
#### 매장 등록
- 매장 등록, 수정 (Partner 계정 하나 당 매장은 하나만 등록 가능하다.)
- 파트너 회원가입이 된 후 매장을 등록할 수 있다.
- 매장 등록 시 파트너 정보에 매장ID, 매장 정보에 파트너ID가 저장된다.

### 예약 (Reservation)
#### 예약신청
- 이용자는 매장 상세정보를 보고 예약을 신청할 수 있다. (회원가입 필수)
- 예약은 `예약요청(REQUESTING)`, `거절(REFUSED)`, `승인(CONFIRM)`, `이용완료(USE_COMPLETE)`, `노쇼(NO_SHOW)`로 분류된다.
- 




