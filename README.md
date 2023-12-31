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
이 프로그램의 기능을 이용하기 위해서는 회원가입과 로그인을 우선 해야한다.
- 유저 회원가입 : `/user/register`
- 유저 로그인 : `/user/login`
- 파트너 회원가입 : `/partner/register`
- 파트너 로그인 : `/partner/login`

#### 회원가입
- User와 Partner은 각각  UserService, PartnerService를 통해 회원가입 할 수 있다.
#### 인증 관련은 `global/auth` 패키지에서 관리된다.
- User와 Partner는 모두 `AuthService`을 통해 로그인 할 수 있다.
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
#### 리뷰 관련
- 이용자는 매장 이용 후 `USE_COMPLETE` 상태의 예약 건에 한하여 리뷰를 쓸 수 있다.
- 리뷰는 별점(0~5), 텍스트(200자 이내)로 구성된다.
- 이용자는 본인이 쓴 리뷰 목록을 조회할 수 있다.
- 이용자는 리뷰를 수정할 수 있다.

## 점장 (Partner)
#### 매장 등록
- 매장 등록, 수정 (Partner 계정 하나 당 매장은 하나만 등록 가능하다.)
- 파트너 회원가입이 된 후 매장을 등록할 수 있다.
- 매장 등록 시 파트너 정보에 매장ID, 매장 정보에 파트너ID가 저장된다.

#### 예약 관련
- 파트너는 예약 리스트(상태 별 검색 가능)를 보고 예약 상태를 변경할 수 있다. (예약 요청 승인 or 거절)
- 파트너는 상점 내 키오스크에서 예약 완료 상태(`ARRIVED`)의 예약을 확인할 수 있다. 

## 예약 (Reservation)
#### 예약 구성
- 예약은 `예약요청(REQUESTING)`, `거절(REFUSED)`, `승인(CONFIRM)`, `이용완료(USE_COMPLETE)`, `노쇼(NO_SHOW)`로 분류된다.
- 예약은 `이용자 정보`, `매장 정보(파트너 정보)`, `예약 정보(인원수, 기간, 상태)` 정보를 가진다. 

### 예외(Exception)
- 예외 발생 시에 `ErrorResponse` 클래스로 에러 응답이 반환된다.
- 에러 응답은 `상태 코드(Status Code)`, `에러 코드(ErrorCode)`, `에러 메시지(한국말)`로 구성되어있다.
- `MyException`을 통해 커스텀 예외를 발생시키며, 
- `MyExceptionHandler`을 통해 예외 발생 시 예외 응답을 반환한다.

## URI 설계

#### 유저 인증
- `POST` `/user/register` : 유저 회원가입
- `POST` `/user/login` : 유저 로그인
#### 매장 - 유저
- `GET` `/store/list` : 매장 리스트 조회
- `GET` `/store/detail` : 매장 상세 조회
- `GET` `/store/review` : 매장 리뷰 조회
#### 예약 - 유저
- `POST` `/reservation/request` : 예약 요청
- `GET` `/reservation/list` : 예약 내역 모두 보기
- `GET` `/reservation/list/{status}` : 예약 내역 모두 보기(예약 상태 별)
- `GET` `/reservation/detail/{reservationId}` : 예약 상세 정보 보기 (파트너 로그인 시, 유저 로그인 시에 따라 다르게 동작)
- `POST` `/reservation/arrived` : 매장 도착 확인
#### 리뷰 - 유저
- `POST` `/review/add/{reservationId}` : 리뷰 쓰기
- `GET` `/review/list/{userId}` : 내가 쓴 리뷰 리스트 확인
- `PUT` `/review/edit/{reviewId}` : 리뷰 수정

---

#### 파트너 인증
- `POST` `/partner/register` : 파트너 회원가입
- `POST` `/partner/login` : 파트너 로그인
#### 매장 - 파트너
- `POST` `/partner/register-store/{partnerId}` : 매장 등록
- `PUT` `/partner/edit-store/{partnerId}` : 매장 수정
#### 예약 - 파트너
- `GET` `/reservation/detail/{reservationId}` : 예약 상세 정보 보기 (파트너 로그인 시, 유저 로그인 시에 따라 다르게 동작)
- `GET` `/partner/reservation/list` : 파트너 - 자신의 상점 예약 내역 모두 보기
- `PUT` `/partner/reservation/{reservationId}` : 예약 상태 변경(승인, 거절, 이용완료 등)

## 테스트(TEST)
- service 로직 테스트 코드 작성
- JUnit5

## Swagger
- `/swagger-ui.html`에서 Swagger API 명세를 확인하실 수 있습니다.

---

## Comments
#### DTO
이번 프로젝트에서는 금융권 관련 도메인을 다루는 강의에서 배운 것 처럼 조금 엄격하게 DTO를 구분지어 사용해봤습니다.
Request 객체 -> DTO -> Entity -> DB 접근 -> Entity -> DTO -> Response 객체 까지 클라이언트에게 꼭 필요한 정보만을 입력받고, 꼭 필요한 정보만을 응답하는 구조로 만들어 봤습니다.
하다보니 과도하게 복잡해 지는 느낌이 있었고, 위와 같은 방식은 꼭 필요한 경우에만 사용하는게 좋겠다는 생각이 들었습니다.
상황에 따른 DTO 사용에 대해 조금 더 고민해 봐야겠다는 생각이 들었습니다.

#### Security
JWT 토큰을 이용한 Security 설정을 하면서 더 자세히 알게된 것들이 많았습니다.
그 중, JWT 토큰은 따로 권한해제를 할 수 없기 때문에, 따로 권한을 빼앗은 목록 같은것을 생성하여 관리해야한다고 들었습니다.
현업에서는 어떤 방식을 많이 사용하는지, 상황에 따른 Security 방식에 대해 알고싶다는 생각이 들었습니다.

#### JPA 
아직 JPA를 깊게 공부하지 못해서 FK 매핑을 하지 못했습니다.
프로젝트 진행 도중 JPA에 대한 공부를 따로 하며 알게된 것들이 많은데,
예를 들면 Store엔티티의 필드에 PartnerEntity, UserEntity 등을 직접 사용하면 되지만, partnerId, userId, userPhoneNumber과 같은 필드를 사용하고, 수정, 추가시마다 일일히 변경해줘야하는 수고로움이 있었습니다.
이 프로젝트에서 엔티티 매핑 관련 리팩터링을 해보거나, 이후에는 JPA의 특성과 이점을 살려서 Entity매핑과 설계를 더 효율적으로 해볼 수 있을 것 같습니다.
- 리팩터링을 시도해보았으나 엔티티 설계 자체를 변경하는게 너무 어려워서 포기했고, 다음에 만들 프로젝트부터 엔티티 설계에 신경을 써 볼 예정이다.







