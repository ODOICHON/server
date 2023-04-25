## 주말의집 - 듀얼 라이프 커뮤니티 플랫폼 🏠

<div style="text-align : center;">
  <img alt="image" src="https://user-images.githubusercontent.com/61505572/220284128-c1ebd399-0928-4c9c-9d4a-f9ef2b5da3f0.png">
</div>

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=coverage)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=bugs)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![CD](https://github.com/ODOICHON/server/actions/workflows/CD.yml/badge.svg)](https://github.com/ODOICHON/server/actions/workflows/CD.yml)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FODOICHON%2Fserver&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

### 프로젝트 설명

"주말의집"은 듀얼 라이프를 꿈꾸고 실천하는 사용자들 간의 커뮤니티 형성을 도와주는 플랫폼입니다.

### 기술 스택
1. 어플리케이션 ( Language & Framework )
- Kotlin
- Spring Framework ( Spring Boot, Spring MVC )
- MySQL ( JPA ), Redis
- Junit5, AssertJ, Mockito
- Gradle( Kotlin )

2. 인프라 아키텍처

![주말의집 아키텍처](https://user-images.githubusercontent.com/61505572/220286737-8b62ca94-a38e-4b68-b0a3-54d85a4b622c.png)

- Route 53
- EC2
- ALB, Nginx, S3, CodeDeploy
- MySQL( RDS )
- Git, Github Actions CI/CD
- CloudWatch, Slack
- Jacoco, SonarlCloud

### API 명세서

- [사용자 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/user.html)
- [게시글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/board.html)
- [댓글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/comment.html)
- [JWT 예외처리](https://odoichon.github.io/server/src/main/resources/static/docs/jwt.html)
- [좋아요 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/love.html)
- [ErrorCode 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/error.html)
<br/>

### 회고 및 문서화
- [은비 - 인프라 아키텍처 설계 및 구성 과정](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9D%B8%ED%94%84%EB%9D%BC-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EA%B5%AC%EC%84%B1)
- [태민 - 인프라 구축 및 무중단 배포 프로세스](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9D%B8%ED%94%84%EB%9D%BC-%EA%B5%AC%EC%B6%95-%EB%B0%8F-%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4)
- [은비 - ERD 설계](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-ERD-%EC%84%A4%EA%B3%84)
- [민혁 - Spring Security 없이 인증인가 구현하기](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-Spring-Security-%EC%97%86%EC%9D%B4-%EC%9D%B8%EC%A6%9D%EC%9D%B8%EA%B0%80-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
- [은비 - Jasypt 라이브러리를 이용한 YML 파일 암호화하기](https://github.com/ODOICHON/server/wiki/%5Bproject%5D-Jasypt-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-YML-%ED%8C%8C%EC%9D%BC-%EC%95%94%ED%98%B8%ED%99%94%ED%95%98%EA%B8%B0)
- [은비 - 테스트 코드를 작성하는 이유](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C%5D-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C%EB%A5%BC-%EC%9E%91%EC%84%B1%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0)
- [은비 - RestDocs 적용 과정](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C%5D-RestDocs-%EC%82%AC%EC%9A%A9%EC%97%90-%EB%94%B0%EB%A5%B8-Controller-%EB%8B%A8%EC%9C%84-%ED%85%8C%EC%8A%A4%ED%8A%B8-(-e2e-Test-))
- [은비 - Soft Delete 방식 적용](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-Soft-Delete-%EB%B0%A9%EC%8B%9D-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)
- [은비 - findByIdOrThrow 함수 커스터마이징하기](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D--Select-%EC%BF%BC%EB%A6%AC-%EB%B0%9C%EC%83%9D-%EC%8B%9C,-Exception-%EC%B2%98%EB%A6%AC-%ED%95%9C-%EB%B2%88%EC%97%90-%ED%95%98%EA%B8%B0)
- [은비 - Jacoco + SonarlCloud 적용과정](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%B8%ED%8C%85%5D-Jacoco-&-SonarCloud-%EC%A0%81%EC%9A%A9)
- [은비 - SonarCloud 세팅 중 마주한 이슈 정리](https://github.com/ODOICHON/server/wiki/%5B%EC%9D%B4%EC%8A%88%5D-SonarCloud-%EC%84%A4%EC%A0%95-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)
- [은비 - Github Actions와 SonarCloud 사용 중 트러블 슈팅 정리](https://github.com/ODOICHON/server/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85%5D-Github-Actions---SonarCloud)
- [민혁 - 유저 탈퇴 처리](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-%EC%9C%A0%EC%A0%80-%ED%83%88%ED%87%B4-%EC%B2%98%EB%A6%AC)
- [민혁 - 리프레시 토큰 저장방식 변경](https://github.com/ODOICHON/server/wiki/%5B%EC%9D%B4%EC%8A%88%5D-%EB%A6%AC%ED%94%84%EB%A0%88%EC%8B%9C-%ED%86%A0%ED%81%B0-%EC%A0%80%EC%9E%A5%EB%B0%A9%EC%8B%9D-%EB%B3%80%EA%B2%BD)
- [민혁 - 유저 테이블 암호화](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9C%A0%EC%A0%80-%ED%85%8C%EC%9D%B4%EB%B8%94-%EC%95%94%ED%98%B8%ED%99%94)
- [민혁 - DDOS 대응 방안](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-DDOS-%EB%8C%80%EC%9D%91-%EB%B0%A9%EC%95%88)

### 버전 관리
🏷 VERSION 1 - 로그인/회원가입 API 연동 및 메인 페이지 뷰 작업

### 팀 구성
| Name    | <center>이은비</center>|<center>윤태민</center> |<center>문민혁</center> | 
| ------- | --------------------------------------------- | ------------------------------------ | --------------------------------------------- | 
| Profile | <center> <img width="110px" height="110px" src="https://avatars.githubusercontent.com/u/61505572?v=4" /> </center>|<center><img width="110px" height="110px" src="https://avatars.githubusercontent.com/u/80155336?v=4" /></center>|<center><img width="110px" height="110px" src="https://avatars.githubusercontent.com/u/102985637?v=4" /></center>|
| Role    | <center>Team Leader<br> Back-end, DevOps</center>   | <center>Back-end, <br> DevOps</center>    | <center>Back-end ,<br> DevOps</center>  | 
GitHub | <center>[@dldmsql](https://github.com/dldmsql)</center> | <center>[@YoonTaeMinnnn](https://github.com/YoonTaeMinnnn) </center>| <center>[@MoonMinHyuk1](https://github.com/MoonMinHyuk1) </center>|
