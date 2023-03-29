## 주말의집 - 듀얼 라이프 커뮤니티 플랫폼 🏠

<div style="text-align : center;">
  <img alt="image" src="https://user-images.githubusercontent.com/61505572/220284128-c1ebd399-0928-4c9c-9d4a-f9ef2b5da3f0.png">
</div>

### 프로젝트 설명

"주말의집"은 듀얼 라이프를 꿈꾸고 실천하는 사용자들 간의 커뮤니티 형성을 도와주는 플랫폼입니다.

### 기술 스택
1. 어플리케이션 ( Language & Framework )
- Kotlin
- Spring Framework ( Spring Boot, Spring MVC )
- MySQL ( JPA ), Redis
- Junit5, AssertJ, Mockito
- Gradle

2. 인프라 아키텍처

![주말의집 아키텍처](https://user-images.githubusercontent.com/61505572/220286737-8b62ca94-a38e-4b68-b0a3-54d85a4b622c.png)

- Route 53
- ALB, Nginx, S3, CodeDeploy
- MySQL(RDB)
- Git, Github Actions CI/CD
- CloudWatch, Slack

### API 명세서

- [사용자 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/user.html)
- [게시글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/board.html)
- [댓글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/comment.html)
- [JWT 예외처리](https://odoichon.github.io/server/src/main/resources/static/docs/jwt.html)
- [좋아요 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/love.html)
<br/>

### 회고 및 문서화
- [은비 & 태민 - 인프라 아키텍처 설계 및 구성 과정](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9D%B8%ED%94%84%EB%9D%BC-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EA%B5%AC%EC%84%B1)
- [은비 - ERD 설계](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-ERD-%EC%84%A4%EA%B3%84)
- [민혁 - Spring Security 없이 인증인가 구현하기](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-Spring-Security-%EC%97%86%EC%9D%B4-%EC%9D%B8%EC%A6%9D%EC%9D%B8%EA%B0%80-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
- [은비 - asypt 라이브러리를 이용한 YML 파일 암호화하기](https://github.com/ODOICHON/server/wiki/%5Bproject%5D-Jasypt-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-YML-%ED%8C%8C%EC%9D%BC-%EC%95%94%ED%98%B8%ED%99%94%ED%95%98%EA%B8%B0)

### 버전 관리
🏷 VERSION 1 - 로그인/회원가입 API 연동 및 메인 페이지 뷰 작업