# ddjtest

## 사용 목적
백엔드 API 서버입니다.
회원 프로필 관리, 포인트 충전 시스템 등의 기능을 제공합니다.

## 실행 방법

### Docker를 이용한 실행
Docker만 설치되어 있으면 Java, MySQL 설치 없이 바로 실행 가능합니다.

```bash
# 1. 프로젝트 클론
git clone https://github.com/ddj82/djtest.git
cd djtest

# 2. Docker 이미지 빌드 및 실행
docker build -t storelabs-app .
docker run -p 5064:5064 -p 3306:3306 --name storelabs-container storelabs-app
```

**실행 시 자동으로 더미 데이터(사용자 10명)가 생성되어 바로 API 테스트가 가능합니다.**

## 사용 기술

### Backend
- **Java 17**
- **Spring Boot 3.3.13**
- **Spring Data JPA**
- **QueryDSL 5.0.0**
- **Spring Validation**

### Database
- **MySQL**

### Build Tool
- **Gradle**

### 라이브러리
- **Lombok**
- **토스페이먼츠** (연동 코드 구현 완료, 과제 특성상 Mock 처리)

## API 사용 방법

**기본 URL:** http://localhost:5064

### 주요 API 엔드포인트

### 1. 회원 프로필 목록 조회
**GET** `/api/users`

**Response 예시:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "a",
      "point": 0,
      "profileViewCount": 1,
      "createdAt": "2025-06-27T00:32:52"
    },
    {
      "id": 2,
      "name": "b", 
      "point": 0,
      "profileViewCount": 2,
      "createdAt": "2025-06-28T00:32:52"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 10,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

**정렬 옵션:**
- `/api/users?sort=name` - 이름 내림차순
- `/api/users?sort=view` - 조회수 내림차순  
- `/api/users?sort=date` - 등록 최신순

**페이징 옵션:**
- `/api/users?page=0&size=5` - 페이지 번호, 페이지 크기

**조합 사용:**
- `/api/users?sort=view&page=0&size=5` - 조회수순 정렬 + 페이징

### 2. 회원 프로필 상세 조회 (조회수 증가)
**GET** `/api/users/{userId}`
**예시:** `/api/users/1`

**Response:**
```json
{
  "id": 1,
  "name": "a",
  "point": 20000,
  "profileViewCount": 5,
  "createdAt": "2025-06-27T00:32:52"
}
```
*호출할 때마다 해당 사용자의 profileViewCount가 1씩 증가합니다.*

### 3. 포인트 충전

**Step 1: 결제 요청**
**POST** `/api/points/charge/request`

**Request:**
```json
{
  "userId": 1,
  "amount": 5000
}
```

**Response:**
```json
{
  "orderId": "ORDER_1751903253594_a7936763"
}
```

**Step 2: 결제 승인**
**POST** `/api/points/charge/confirm`

**Request:**
```json
{
  "paymentKey": "test_payment_key_12345",
  "orderId": "ORDER_1751903253594_a7936763", 
  "amount": 5000
}
```

**Response:**
```
결제가 완료되고 포인트가 충전되었습니다.
```

### 4. 포인트 잔액 조회
**GET** `/api/points/balance/{userId}`

**예시:** `/api/points/balance/1`

**Response:**
```
20000
```

## 컨테이너 관리

### 컨테이너 정지
```bash
docker stop storelabs-container
```

### 컨테이너 재시작
```bash
docker start storelabs-container
```

### 컨테이너 제거
```bash
docker rm storelabs-container
docker rmi storelabs-app
```
