<p align="center">
  <img src="https://github.com/user-attachments/assets/d204118f-7d99-4224-88e2-ed05c5bf46c9" alt="tablebuddy" width="460" height="300">
</p>


# 테이블버디 서비스

**식당 줄서기 서비스**

* 식당 이용객은 순서를 기다리기 위해 현장에서 대기하지 않아도 된다.
* 식당측은 간편하게 대기열을 관리할 수 있다.

## 기술 스택

* Backend: `Java 17`, `Spring Boot 3.3.4`, `Spring Data JPA`
* DevOps: `MariaDB 10.11`

### 유스케이스

- 회원가입
- 로그인
- 로그아웃
- 비밀번호 찾기(재설정 이메일)
- 회원 탈퇴

**고객**
- 가게를 조회할 수 있다.
- 줄서기를 신청할 수 있다.
    - 인원 수 입력
- 줄서기 취소할 수 있다.
- 줄서기 대기자 수 조회 가능
- 줄서기 알림을 받을 수 있다.

**사장**
- 가게를 등록할 수 있다
    - 식당의 이름을 등록할 수 있다.
    - 운영 시간을 등록할 수 있다.
    - 최대 수용 공간을 등록할 수 있다.
- 가게를 수정할 수 있다
- 가게를 삭제할 수 있다
- 줄서기 현황을 업데이트할 수 있다
  - 고객에게 현장 대기 알람을 보낼 수 있다.
  - 고객의 입장 요청을 수락할 수 있다.
    


## 프로젝트 컨벤션

### TODO 컨벤션
```text
// TODO: 내용 작성
```

### 커밋 메시지 규칙

```text
# type 작성 후, :(콜론) 뒤 공백 하나를 두고 title을 작성한다.
# 마지막 소괄호 사이 이슈 번호를 기입한다.

<type>: <title>(<#issueNumber>)

[optional body]

[optional footer(s)]
```

* type
  - feat : 기능 추가
  - refactor : 리팩토링
  - test : 테스트 작업
  - docs : 문서 작업
  - fix : 버그 수정
  - chore : 프로젝트 설정 작업

* 참고
  - https://github.com/excalidraw/excalidraw (커밋메시지 참고)
  - https://www.conventionalcommits.org/en/v1.0.0/

