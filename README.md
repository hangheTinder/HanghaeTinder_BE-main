# HanghaeTinder_BE-main

### 항해99 13조 클론 코딩: tinder 
### BackEnd: 조우철,김근보,김지승,김현준
### FrontEnd: 조형민,신희제 https://github.com/hangheTinder/HanghaeTinder_FE

## 1.아키텍쳐, CI/CD 파이프라인
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/5f7608aa-7ce4-4bb7-bcc7-4f61fccfa64d)
- 배포 프로세스 상태 확인하는 법
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/2ccb2d03-cdb9-46d2-b6ca-1807fb3101b1)

✅ Green: 성공 / 🟡 Yello: 진행중 / ❌ Red: 실패 혹은 취소

## 2.Swagger
### 링크 : http://15.164.159.168:8080/swagger-ui/index.html#/
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/1ccb9713-f890-4c3c-b8e7-7afdfa18f08f)

## 3.와이어 프레임(pingma로 작업)
링크: https://www.figma.com/file/oYhmdlSiaHAOabE1izourg/%ED%81%B4%EB%A1%A0%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?type=design&node-id=0-1&t=UO6bjn6d9UTlw7O7-0

### 메인 페이지

- Log in 버튼 : 로그인 페이지로 이동
- Matching Start 버튼 : (로그인 상태 아닐 시)로그인페이지 이동 , (로그인 상태 시)매칭 페이지 이동

![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/3d5a3b01-4d2b-46e7-b436-2c10b05de49a)



### 회원가입 페이지

- 이름(nickname) : 유저 닉네임 입력

- 생일(date) : 유저 출생 연도, 월 ,일 입력

- 성별(sex) : 유저 성별 입력

- ID: (이메일형식)의 유저 ID 입력

- PW:유저 PW 입력 / PW check : 앞의 입력한 유저 PW와 일치한지 체크

- 관심사(favorite): 유저 관심사 선택란 (ex: 책보기, 독서하기, 농구하기, LOL 등등..) 리스트 갖고 오기

- 프로필 사진(image): 유저 프로필 사진 등록 (사진 등록 안 할시 Next 버튼으로 다음페이지 이동 불가)

- Next 버튼 : 클릭 시 매칭 페이지로 이동
 
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/5ad4ef84-ce62-4f74-bdd2-ee235f9fb587)



### 로그인 페이지

- Email:  유저 회원가입 시 입력한 email형식의 ID 입력

- Password :  유저 회원가입 시 입력한 password 입력

- Login : Id,pw를 모두 올바르게 입력했는지 검증 후 매칭페이지 이동

- sign up : 회원가입 페이지로 이동
 
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/cef4d919-238f-4273-acf6-b52a4f333533)



### 매칭 페이지

- tinder 배너 : 클릭 시 메인페이지로 이동

- 필터버튼: (default) 랜덤한 전체유저 / 필터버튼 클릭 시: 나를 좋아요한 유저만 볼 수 있도록 선택

- 닉네임 란 : 유저 가입 시 입력한 nickname 노출

- 성별: 유저 가입 시 선택한 성별 노출( 남성 or 여성)

- 나이: 유저 가입 시 입력한 DATE를 계산하여 int형 숫자로 노출

- 채팅모양버튼 : 클릭 시 채팅 목록 페이지로 이동

- 대표이미지 : 랜덤하게 노출된 상대 유저 프로필 이미지 

- 관심사 : 랜덤하게 노출된 상대 유저가 회원가입 시 선택한 관심사(favorite) 노출
 
- 넘기기 버튼(👋) : 상대가 마음에 들지 않을 시 현재 유저 정보를 넘기고 다른 유저의 정보를 불러옴

- 좋아요 버튼(♥): 랜덤하게 노출된 상대가 마음에 들 시 클릭하여 좋아요(♥)한 유저로 저장
 
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/5c3290fe-5956-42f0-b3b3-4bcda039068a)



### 채팅 페이지

- 상대 프로필 : 상대 프로필 이미지 노출 + 상대 닉네임 노출

- 채팅란 : 상대방의 대화 내용노출 (왼쪽) , 내가 입력한 대화 노출(오른쪽) 내림차순으로 

- 채팅입력란: 상대방에게 보낼 채팅(Text)을 입력하는 란

- 보내기 버튼: 클릭 시 입력한 채팅(Text)을 채팅란으로 보냄

- 뒤로가기 버튼(🔙): 클릭 시 채팅목록 페이지로 이동
 
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/e856904b-9d61-425e-90c2-b3c11790800e)

### 채팅 목록페이지

- tinder 배너 : 클릭 시 메인페이지로 이동

- chats : 나와 채팅(대화)한 유저들의 대화 리스트 노출(상대와 내가 서로 좋아요 누를 시에만 채팅 가능)

- 채팅(대화) 리스트: 클릭 시 해당 유저와의 채팅 페이지로 이동 
 
![image](https://github.com/hangheTinder/HanghaeTinder_BE-main/assets/125139072/156382ec-5a49-4db4-b222-45c91cf396ab)

