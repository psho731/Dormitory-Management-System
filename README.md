# Dormitory Management System
생활관(기숙사) 관리 시스템 (CLI 환경)

사용자가 생활관 신청 및 관리하는 프로그램 (금오공과대학교의 생활관을 기반으로 구현)

Packet과 Message 구현하여 사용

3 Tier Architecture


## 프로젝트 소개
생활관을 관리하는 프로그램입니다.

학생은 입사를 신청하고 퇴사하는 등 다양한 기능을 활용할 수 있습니다.

관리자는 더욱 편리하게 생활관과 학생을 관리할 수 있습니다.


## 개발 기간 및 인원
인원: 4명

개발 기간: 8주


## 사용 기술
- language: Java 17
- Gradle: 8.5
- DB: MySQL
- IDE: IntelliJ
- JDBC와 Socket 프로그래밍 활용 (패킷 직렬화 방식)
- 멀티 쓰레드 프로그래밍


## 담당한 부분
- DAO, DTO, Service, View 총괄
- Packet, Message 구현
- DB와 프로토콜 포맷 설계 일부
- 효율, 유지보수 향상
- 최종 첨삭


## 기능 소개

### 학생
- 로그인
- 선발 일정 및 비용 확인
- 입사 신청
- 합격 여부 및 호실 확인
- 생활관 비용 확인 및 납부
- 결핵진단서 제출
- 퇴사 신청
- 환불 확인
- 상벌점 확인
- 로그아웃
- 프로그램 종료

### 관리자
- 로그인
- 선발 일정
- 기숙사비 및 급식비 등록
- 입사 신청자 조회
- 입사자 선발 및 호실 배정
- 생활관 비용 납부자 조회
- 생활관 비용 미납부자 조회
- 결핵진단서
- 퇴사 신청자 조회 및 환불
- 상벌점
- 로그아웃
- 프로그램 종료


## 프로토콜 포맷
자세한 설명은 Packet 클래스 참조

![type](https://github.com/user-attachments/assets/f56f5fe8-f115-4171-ac38-f4c109e65766)


## ERD
![png](https://github.com/user-attachments/assets/4bc43ee5-959b-4459-aaa2-b8f4fbde062f)
