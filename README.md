# 프로젝트 개요
![프로젝트 개요](https://github.com/user-attachments/assets/b14a88b2-1672-4556-8611-c4b5482a3c4d)
교육 서비스 사업과 관련된 다양한 데이터를 효과적으로 관리하고 분석할 수 있는 도구나 기능
1. 교육 과정, 수강생 관리 등 교육 서비스업 직원을 대상으로 한 교육 시스템
2. 수강생에게 수강 정보를 제공하기 위한 모바일 웹 어플리케이션

##  개발일정
![프로젝트 개요](https://github.com/user-attachments/assets/410022b4-da62-478a-b9de-b6d6af799b78)
- 총 개발 기간: 24.06.19 ~ 24.07.23
  - 기획: 06.19 ~ 06.21
  - 1차 프로젝트 구현: 06.24 ~ 07.06
  - 2차 프로젝트 구현: 07.07 ~ 07.23

##  팀원 및 역할
![프로젝트 개요](https://github.com/user-attachments/assets/ff689e59-1fe7-4c11-87b3-dcabb83af434)
- 양유진
  - 수강생 관리, 출결 관리 FE, BE, TEST 구현
  - AWS S3를 이용한 파일 업로드 기능 구현
  - 로고 제작
- 손유철
  - 과정 관리, 관리자 관리, 포인트 관리, 수강생 전용 모바일 웹 서비스 FE, BE, TEST 구현
  - 취업관리: BE 구현
  - Spring Boot Security 세팅 및 적용
- 김진희
  - 공지사항, 취업관리 FE, BE, TEST 구현
  - ChartJS, 엑셀 추출기능 구현
- 김선희
  - 정산관리, 포인트관리, 메인화면 FE, BE, TEST 구현
  - Spring Security JWT 인증방식 세팅 및 적용
  - AWS EC2 서버 관리 및 배포
  - 수강생 웹 서비스 디자인 및 세팅
# 소프트웨어 아키텍처
Spring Boot를 배우기 이전 JAVA 8 코어 API만을 사용해서 만든 기존 프로젝트를 바탕으로 Spring Boot를 적용하여 신규 프로젝트를 진행하였습니다.
## 기존 프로젝트
![프로젝트 개요](https://github.com/user-attachments/assets/14cee992-e256-49f3-81f6-fcf552510ef0)
## 신규 프로젝트
![프로젝트 개요](https://github.com/user-attachments/assets/e651f464-154a-4753-8f35-1d3436c83edc)

# 사용 기술스택
![프로젝트 개요](https://github.com/user-attachments/assets/16941f3a-4796-46cf-83f1-ae9c5def5411)
## 기획 및 협업
- Notion
- 구글 스프레드시트
- Figma
- ERD Cloud
- **Git**
## 테스트
- Junit5 (+ Assertj 3.25)
- Postman
## 구현
### 클라이언트
- ThymeLeaf
- Node.js - React - Axios
### 서버
- Maven
- Spring Boot 3.3.0
- Spring Boot Security 6.3.0 (+JWT)
- MyBatis
- JPA
### DB
- MariaDB
### 배포
- Amazon EC2
- Amazon S3
### 설치 시 주의사항
- 보안상의 문제로 application.properties는 업로드하지 않았습니다. 다음 코드를 상황에 맞게 작성해주시기 바랍니다.
```
spring.application.name=ems
spring.datasource.driverClassName=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://(DB서버 IP)
spring.datasource.username=(사용자명)
spring.datasource.password=(비밀번호)
#
mybatis.type-aliases-package=com.kosta.ems.mapper
mybatis.mapper-locations=/mapper/*.xml


#custom value
##sercurity.level OFF
##sercurity.level ON
security.level =ON

#jpa
spring.jpa.hibernate.ddl-auto= validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

#jwt
jwt.secret=(jwt secret 키값)
jwt.access.expire=30
jwt.refresh.expire=14

#s3
cloud.aws.region.static=ap-northeast-2
cloud.aws.s3.bucket=attendance-document-bucket
cloud.aws.credentials.access-key=(S3서버 액세스 키)
cloud.aws.credentials.secret-key=(S3서버 시크릿 키)
cloud.aws.s3.bucket.url=(S3 서버 IP)
cloud.aws.s3.upload-temp=upload/
cloud.aws.stack.auto=false```
</svg> 