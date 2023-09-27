# 매장 예약 서비스
#### Store Reservation Service

## Skills
- Java11, SpringBoot 2.5.6, gradle 8.2.1
- Spring data JPA, Spring security(JWT)
- MariaDB
- JUnit5
- IntelliJ Idea

# 요구사항

## 인증 (Auth)
#### 회원가입, 로그인은 `global/auth`패키지에서 관리된다.
- User와 Partner는 모두 `AuthService`를 통해 회원가입, 로그인 할 수 있다.
- User는 `MemberType=ROLE_USER`을 가진다. 
- Partner는 `MemberType=ROLE_PARTNER`을 가진다. 
- Partner는 User의 서비스까지 모두 접근 가능하다.
- User는 `/partner`로 시작되는 URL에 접근할 수 없다.
- Partner, User 모두 개인정보(주문내역, 매장정보, 주문정보 등)를 열람하려면 해당 유저(Partner or User) 로그인이 되어있어야 한다.

#### SpringSecurity
- 유저, 파트너 로그인 시 로그인 사용자 ID와 함께 JWT Token을 담아 응답한다.
- 이 Token을 Request Header에 포함시켜 로그인 유저or파트너를 인증한다.

## 이용자 (User)
#### 매장 검색
- 이용자는 매장을 검색하고, 상세 정보를 확인할 수 있다.
- 매장 검색시에는 정렬 방법을 설정할 수 있다.(전체, 가나다순, 평점순, 리뷰순, 거리순)
- `쿼리 파라미터 p`에 따라 페이징 처리가 된다.(1페이지부터 시작)
- 매장 상세 정보를 확인 할 수 있다. (매장 명, 매장 주소, 매장 설명, 별점, 리뷰수)
#### 예약 관련
- 이용자는 매장 상세정보를 보고 예약을 신청할 수 있다. (회원가입 필수)
- 예약 시간 10분 전까지 이용자는 매장에 도착해서 도착 확인 정보를 전달할 수 있다.
  - STATUS : `CONFIRM` => `ARRIVED`

## 점장 (Partner)
#### 매장 등록
- 매장 등록, 수정 (Partner 계정 하나 당 매장은 하나만 등록 가능하다.)
- 파트너 회원가입이 된 후 매장을 등록할 수 있다.
- 매장 등록 시 파트너 정보에 매장ID, 매장 정보에 파트너ID가 저장된다.

#### 예약 관련
- 파트너는 예약 리스트(상태 별 검색 가능)를 보고 예약 상태를 변경할 수 있다. (예약 요청 승인 or 거절)

## 예약 (Reservation)
#### 예약 구성
- 예약은 `예약요청(REQUESTING)`, `거절(REFUSED)`, `승인(CONFIRM)`, `이용완료(USE_COMPLETE)`, `노쇼(NO_SHOW)`로 분류된다.
- 예약은 `이용자 정보`, `매장 정보(파트너 정보)`, `예약 정보(인원수, 기간, 상태)` 정보를 가진다. 

### 예외(Exception)
- 예외 발생 시에 `ErrorResponse` 클래스로 에러 응답이 반환된다.
- 에러 응답은 `상태 코드(Status Code)`, `에러 코드(ErrorCode)`, `에러 메시지(한국말)`로 구성되어있다.
- `MyException`을 통해 커스텀 예외를 발생시키며, 
- `MyExceptionHandler`을 통해 예외 발생 시 예외 응답을 반환한다.

## 테스트(TEST)
- 모든 service 클래스 메서드 테스트
- JUnit5



