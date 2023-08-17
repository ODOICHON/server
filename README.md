## 주말의집 - 듀얼 라이프 커뮤니티 플랫폼 🏠

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=coverage)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ODOICHON_server&metric=bugs)](https://sonarcloud.io/summary/new_code?id=ODOICHON_server)
[![CD](https://github.com/ODOICHON/server/actions/workflows/CD.yml/badge.svg)](https://github.com/ODOICHON/server/actions/workflows/CD.yml)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FODOICHON%2Fserver&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

### 프로젝트 설명

"주말의집"은 듀얼 라이프를 꿈꾸고 실현하는 사람들 간의 소통의 공간 부족과 지역 사회의 빈집 문제 해결을 위한 플랫폼입니다.

본 프로젝트를 통해 아래와 같은 경험과 지식을 쌓고 있습니다.

- SpringBoot 와 Kotlin 기반의 웹 애플리케이션 생애 주기 기획부터 배포 및 운영까지 경험
- AWS 클라우드 환경 기반의 CICD 무중단 배포 인프라 구축 경험
- JPA, Hibernate를 사용한 도메인 설계 경험
- MVC 기반 애플리케이션 서버 구축
- SSR 방식의 백오피스 개발 경험
- 인가 프로세스가 적용된 비즈니스 로직 구현 경험
- 성능 향상을 위한 고민과 도전 경험

### 기술 스택
**어플리케이션 ( Language & Framework & Library )**
- Kotlin
- Spring Framework ( Spring Boot 2.7.8, Spring MVC )
- Spring Data JPA, Redis, QueryDsl
- Junit5, AssertJ, Mockito
- Rest Docs

**Build Tool**
- Gradle( Kotlin )

**Database**
- MySQL

**인프라( with CICD )**

![밋업데이-아키텍처-cicd-flow drawio](https://github.com/ODOICHON/server/assets/61505572/d19f80f5-143d-4486-bfcc-4ffeea26670c)


- AWS Route 53
- AWS EC2
- Nginx, AWS S3, AWS CodeDeploy(Agent)
- AWS RDS
- Git, Github Actions
- CloudWatch, Slack
- Jacoco, SonarCloud
- Docker
- Lambda

**WEB for back-office**
- Javascript
- HTML/CSS
- Thymeleaf
- Bootstrap 5

**WEB for tech-blog**
- Typescript
- HTML/CSS
- Vue3
- Vuex
- Vue-Router
- Bootstrap 5
- Axios
- npm
- Toast-ui for vue

**그외 주요 라이브러리**
- JWT
- Bucket4j
- Cache
- Coolsms
- Jasypt

### E-R 다이어그램
![주말내집 ERD-6](https://github.com/ODOICHON/server/assets/61505572/24511404-8fd0-49fe-9104-6245d7b24fec)


### REST DOCS 기반 API 명세서
**주말의집 서비스**
- [사용자 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/user.html)
- [게시글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/board.html)
- [댓글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/comment.html)
- [JWT 예외처리](https://odoichon.github.io/server/src/main/resources/static/docs/jwt.html)
- [커뮤니티 게시글 좋아요 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/love.html)
- [빈집 게시글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/house.html)
- [빈집 게시글 스크랩 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/scrap.html)
- [알림 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/notification.html)

**테크블로그 서비스**
- [레코드 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/record.html)
- [레코드 카테고리 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/record_category.html)
- [레코드 댓글 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/record_comment.html)
- [레코드 리뷰 API 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/record_review.html)

**공통 문서**
- [ErrorCode 명세서](https://odoichon.github.io/server/src/main/resources/static/docs/error.html)
<br/>

### 핵심 기능 ( 배포 이후 )
**메모리 부족으로 인한 swap으로 가상 메모리 설정**

프리티어 환경에서 EC2 서버를 운영하면서 CD를 통해 배포하면 서버가 죽는 이슈가 발생했습니다. swap을 통해 가상 메모리 설정을 통해 해당 이슈를 해결하였습니다. 서버를 증설하는 것 대신 가용 메모리를 늘린 이유는 비용 발생 구간을 최소화하기 위함입니다.

**사용자 개인 정보 보호를 위한 암호화 처리**

회원가입과 로그인/로그아웃 관련 기능 구현 이후, 배포된 사이트를 통해 QA를 하면서 개인 정보 보호에 대한 걱정이 들었습니다.

실제 사용자가 접속해서 회원가입 과정을 거쳐 사이트에 제공하는 정보에 대해 제3자로부터 보호하는 것은 당연하지만, 이를 운영하는 관리자들로부터도 보호되어야 한다고 생각했습니다.

따라서 사용자 정보를 생성/수정/조회 시, 암호화와 복호화 처리를 거치도록 리펙토링하였습니다. DB에 콘솔로 접근하여 select 쿼리를 통해 조회하더라도 모든 데이터는 암호화 처리된 상태로 조회됩니다. 암복호화는 Converter와 AES 알고리즘을 적용하여 구현하였습니다.

리펙토링 과정은 [유저 테이블 암호화](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9C%A0%EC%A0%80-%ED%85%8C%EC%9D%B4%EB%B8%94-%EC%95%94%ED%98%B8%ED%99%94)에서 자세히 확인할 수 있습니다.

**사용자 탈퇴 이후의 처리**

사용자로부터 얻는 데이터로 운영되기 때문에 사용자가 탈퇴를 하더라도 개인정보를 제외한 데이터는 보유하고 있기로 하였습니다. 이를 위해 사용자 데이터를 delete 하는 것이 아닌 "" 공백 문자를 넣어 사용자 테이블과 Cascade가 걸린 다른 테이블의 데이터를 보존할 수 있도록 리펙토링하였습니다.

리펙토링 과정은 [유저 탈퇴 처리](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-%EC%9C%A0%EC%A0%80-%ED%83%88%ED%87%B4-%EC%B2%98%EB%A6%AC)에서 자세히 확인할 수 있습니다.

**리프레시 토큰 저장방식의 변경**

기능 개발 중에 JWT 토큰 형태의 Access-token과 Refresh-token을 Frontend에서 클라이언트의 localstorage에 관리했습니다. Server에서는 Redis 서버에 Refresh-token을 저장하여 관리했습니다.

유효기간이 긴 Refresh-token이 localstorage에 저장되면 XSS 공격 등으로 탈취 당할 위험이 있다고 판단하여 Cookie에 담아 저장하는 방식으로 변경하였습니다.

리펙토링 과정은 [리프레시 토큰 저장방식 변경](https://github.com/ODOICHON/server/wiki/%5B%EC%9D%B4%EC%8A%88%5D-%EB%A6%AC%ED%94%84%EB%A0%88%EC%8B%9C-%ED%86%A0%ED%81%B0-%EC%A0%80%EC%9E%A5%EB%B0%A9%EC%8B%9D-%EB%B3%80%EA%B2%BD)에서 자세히 확인할 수 있습니다.

**테크 블로그 서비스 기획/개발**

팀 오도리에서 주말의집 서비스 개발을 위해 거쳐온 지난 여정과 마주했던 이슈 및 회고에 대해 사용자들과 공유하고자 off the record로 시작하게 된 서비스입니다. 해당 서비스는 서버 파트 내부에서 자체적으로 기획/디자인/개발 하는 것이며, 본 서비스와 공용으로 사용하는 API가 있기에 해당 레포지토리에서 추가 확장으로 개발을 진행하였습니다. 프론트엔드 작업 내용은 [back-office](https://github.com/ODOICHON/back-office) 에서 확인할 수 있습니다.

[테크 블로그 바로가기](https://duaily.net/tech) 에서 결과를 확인할 수 있습니다.

**테크 블로그 프로세스 정의**

<details>
<summary>로그인</summary>
<div markdown="1">
  
![image](https://github.com/ODOICHON/back-office/assets/61505572/d78202f1-8f6a-424d-b2a0-b420a234e2a6)
  
</div>
</details>

<details>
<summary>게시글 작성</summary>
<div markdown="1">
  
  ![image](https://github.com/ODOICHON/back-office/assets/61505572/ff0d33be-2a57-4eca-ae8f-1743fc5bc40e)
  
</div>
</details>

<details>
<summary>게시글 수정</summary>
<div markdown="1">
  
  ![image](https://github.com/ODOICHON/back-office/assets/61505572/a6766194-4ca2-47df-b752-a7d7ceb9cbd2)
  
</div>
</details>

<details>
<summary>게시글 삭제</summary>
<div markdown="1">
  
  ![image](https://github.com/ODOICHON/back-office/assets/61505572/9eba77c1-4341-46e6-9694-082991d17ade)
  
</div>
</details>

<details>
<summary>게시글 댓글 작성</summary>
<div markdown="1">
  
  ![image](https://github.com/ODOICHON/back-office/assets/61505572/7afc1b82-bff8-4443-821c-287fe29c4c82)
  
</div>
</details>

<details>
<summary>마이페이지 리뷰 조회</summary>
<div markdown="1">
  
  ![image](https://github.com/ODOICHON/back-office/assets/61505572/5296a1c1-37ca-4aa1-b8d3-94488056a653)
  
</div>
</details>

<details>
<summary>마이페이지 리뷰 등록</summary>
<div markdown="1">
  
 ![image](https://github.com/ODOICHON/back-office/assets/61505572/285334fd-480a-4e69-a840-4485bf9bec01)
  
</div>
</details>

**커뮤니티 게시판에서 페이징 데이터 캐싱으로 조회 성능 향상**

레디스를 이용해 커뮤니티 게시판에서 페이징 데이터를 캐싱하여 조회 성능을 향상 시켰습니다. 데이터에 수정 혹은 삭제 발생이 캐싱 내용도 함께 변경되도록 하였으며, 3시간의 유효시간을 걸어두었습니다.

**빈집 거래 서비스 론칭을 위한 사용자 타입 추가, 공인중개사**

빈집 거래 서비스는 지역 사회의 빈집 문제 해결을 위한 것입니다. 빈집 매물에 대한 신뢰성을 높이고자 공인중개사 사용자를 추가하여 빈집 매물을 본 서비스를 통해 홍보하고 거래로 이어지도록 합니다.
일반 사용자 역시 빈집 매물을 등록할 수 있지만, 관리자에게 우선적으로 승인이 되어야 합니다. 

**빈집 게시글 글자수 제한, 유효성검사를 위한 Validator 커스터마이징**

커뮤니티 게시글과 달리 빈집 게시글은 10,000자의 글자 수 제한이 있습니다. 게시글 내용에는 순수한 글자만이 있는 것이 아닌 이미지 주소, HTML 태그 등 부가적 요소들이 포함됩니다. 이러한 요소들을 파싱하면서 제외시키고 순수한 내용에 대한 글자 길이를 확인하기 위해 CodeValid 어노테이션을 만들고 별도의 validator를 구현하였습니다.

### 핵심 기능 ( 기능 개발 중 )
**반응형 웹 화면**  

<details>
  <summary>백오피스 관리자 페이지 화면</summary>
  <div markdown="1">
    <img width="1470" alt="스크린샷 2023-04-25 오후 5 03 44" src="https://user-images.githubusercontent.com/80155336/235109603-fbd9d512-f47d-4edb-bf78-e99603ed426d.png">  
<img width="578" alt="스크린샷 2023-04-28 오후 6 25 04" src="https://user-images.githubusercontent.com/80155336/235110024-628d77cf-12ec-4919-a309-9848885698d1.png">  <br/>
[사진 - 백오피스 중 관리자 페이지]  
  </div>
</details>

Bootstrap을 이용하여 모바일 기기로 접속은 물론 모니터 화면에서도 문제없이 동작하는 반응형 웹으로 구현하였습니다.  

**백오피스**

주말의집을 운영하는 관리자만이 접속할 수 있는 백오피스를 SSR 방식으로 구현하였습니다.

SSR 방식을 선택한 이유는 제한된 접근자와 적은 수의 트래픽이 발생하기 때문에 개발 리소스를 최소화하기 위함이었습니다.

백오피스에서의 핵심 기능은 다음과 같습니다.

- 사용자 연령대별 비율, 사용자 가입경로 시각화
- 게시글 상단 고정 및 고정 해제
- 게시글 영구삭제
- 공인중개사 회원 가입 승인/반려
- 일반 사용자의 빈집 매물 등록 게시글 승인/반려
- 신고 처리된 게시글 관리

관리자 계정으로의 로그인은 Session 방식을 적용하였으며, interceptor를 통해 로그인이 필요한 페이지에 로그인 없이 접근하는 것을 불가능하게 하였습니다.

회원가입 시, 사용자로부터 얻은 데이터에 대한 통계적 자료를 확인하기 위해 연령대와 가입경로 데이터를 시각화하여 조회할 수 있도록 하였습니다.

홍보 게시판은 일반 사용자의 요청으로 관리자가 게시글을 상단에 고정할 수 있습니다. 한 번에 최대 10개의 게시글을 고정/고정해제 할 수 있으며 이는 위에 관리자 페이지 화면에 해당합니다.

일반 사용자가 접근할 수 있는 페이지에서의 게시글 삭제는 Soft delete 방식으로 DB에서 영구적으로 삭제되지 않습니다. DB에서 영구 삭제는 관리자만이 할 수 있으며 이를 위해 관리자 페이지에서 게시글 영구 삭제 기능을 구현하였습니다. 

주말의집 서비스에는 관리자, 일반 사용자, 공인중개사 유저가 존재하며, 관리자는 공인중개사의 회원가입 승인/반려 처리를 통해 관리합니다. 

공인중개사와 달리 일반 사용자는 빈집 매물을 등록할 시, 관리자의 승인이 필요합니다. 이는 사이트에서 운영하는 빈집 거래 중개 서비스의 신뢰성을 보장하기 위한 주말의집 만의 프로세스입니다.

공인중개사 혹은 일반 사용자가 업로드한 빈집 게시글이 허위 게시글이거나 상업적 목적으로 잘못된 정보를 기재한 경우 유저들로부터 신고를 받으며 이러한 게시글을 삭제 처리를 합니다.

> `뒤에서 묵묵히 역할하는 관리자 페이지`는 지속적으로 업데이트 중입니다.

**로컬 로그인**

로컬 로그인 구현을 위해 Spring Security가 아닌 ArgumentResolver와 AOP를 커스터마이징하여 구현하였습니다.

@Auth 어노테이션으로 컨트롤러 메소드가 실행되기 전에 HTTP Request Authorization Header에 담긴 JWT 토큰에 대해 검증합니다.

@AuthUser 어노테이션으로 @Auth 어노테이션으로 검증된 토큰 값에 담긴 사용자의 아이디로 User를 찾은 후, 반환된 값을 컨트롤러 메소드의 Argument로 사용할 수 있습니다.

로컬 로그인 구현 과정은 [Spring Security 없이 인증인가 구현하기](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-Spring-Security-%EC%97%86%EC%9D%B4-%EC%9D%B8%EC%A6%9D%EC%9D%B8%EA%B0%80-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)에서 확인할 수 있습니다.

**사용자 권한**

로컬 로그인을 통해 회원가입을 하는 사용자는 일반 사용자의 권한을 가지며, 관리자 권한을 갖는 사용자는 내부적으로 Authority를 ADMIN으로 부여하고 있습니다. 

일반 사용자는 게시글 조회/수정/삭제/생성 및 댓글 작성/수정/삭제, 좋아요 등의 기능을 수행할 수 있습니다.

관리자는 소개 페이지 내 후기/트렌드 게시글 작성/수정/삭제 권한을 가지며, 이는 일반 사용자에게 노출되지 않는 기능입니다.

관리자는 일반 사용자의 악의성 게시글 및 댓글에 대해 영구 삭제 권한을 갖고 있습니다.

**React-Quill 기반 게시글 작성**

글 작성 시, 아래 사진과 같이 사용자가 자유롭게 꾸밀 수 있는 에디터 형식을 지원합니다.

![img_2.png](img/img_2.png)

대표 썸네일 이미지와 게시글 내용에 포함되는 이미지에 대해서는 Frontend 측에서 AWS S3로 직접 업로드 처리합니다. 

이미지 업로드 후 url 주소에 대해서 Server로 게시글 내용과 함께 전달하여 DB에서 이를 관리합니다. 

**트러블 슈팅** (2023.04.17) <br/>
게시글 테이블 데이터 최대 크기 초과 이슈가 발생했습니다. 

Board(Id, CreatedAt, UpdatedAt, Category, Code, Content, Fixed, FixedAt, ImageUrls, PrefixCategory, Title, UseYn, UserId )

게시글 테이블은 위와 같은 구조로 설계 되어 있었으며, Code 컬럼은 Frontend 측에서 넘어오는 HTML 태그가 포함된 데이터입니다. 

게시글 검색 시, 게시글의 제목과 내용에 대해 검색이 가능해야 했기에 게시글 데이터가 insert 되기 이전에 Code 데이터로부터 HTML 태그를 파싱해서 순수한 값만 갖는 Content 데이터를 별도의 컬럼으로 저장하여 관리합니다.

게시글 테이블 내에 이미 많은 컬럼이 존재하고, 사용자가 작성하는 게시글의 글자수 제한이 없기 때문에 Code와 Content 데이터의 크기가 하나의 Row가 가질 수 있는 범위를 넘어서면서 insert 오류가 발생했습니다.

이를 해결하기 위해 테이블을 분리하여 board와 board_code를 One-To-One으로 관리합니다.

Code 컬럼은 TEXT 타입으로 데이터를 저장 및 관리합니다.

해당 이슈의 발생 과정 및 해결 과정은 [게시글 테이블 데이터 크기 초과로 인한 테이블 분리 작업](https://github.com/ODOICHON/server/wiki/%5B%EC%9D%B4%EC%8A%88%5D-%EA%B2%8C%EC%8B%9C%EA%B8%80-%ED%85%8C%EC%9D%B4%EB%B8%94-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EA%B8%B8%EC%9D%B4-%EC%B4%88%EA%B3%BC%EB%A1%9C-%EC%9D%B8%ED%95%9C-%ED%85%8C%EC%9D%B4%EB%B8%94-%EB%B6%84%EB%A6%AC-%EC%9E%91%EC%97%85)에서 자세히 확인할 수 있습니다.

**게시글 CRUD**

게시글에 대한 기본적인 CRUD를 모두 구현하였습니다. 게시글 작성은 위의 내용처럼 에디터 형식으로 게시글이 작성되어 등록되며, 조회 시에도 HTML로 파싱 후 렌더링 되어 보여집니다.

수정과 삭제의 경우, 관리자 유저와 게시글 작성자 본인에 대해서만 인가하여 권한을 제한하였습니다.

**Soft Delete 방식의 게시글 삭제**

사용자로부터 얻는 데이터를 중심으로 운영되는 서비스의 경우, 데이터가 곧 자산입니다. 데이터가 누적되어 그 안에서 유의미한 인사이트를 얻을 수 있고, 이것이 신규 기능이자 서비스로 발전될 수 있습니다.

기획의 요구사항 변경으로 삭제된 데이터를 다시 노출해야 하는 예기치 못한 상황에 대응하기 위해 Soft Delete 방식을 적용하기로 하였습니다.

이를 위해 게시글 테이블 내에 UseYn 컬럼을 두어 일반 사용자가 게시글을 삭제한 경우에는 조회되지 않고, 관리자 페이지에서 관리자가 삭제하는 경우에는 영구 삭제합니다.

soft delete 적용 과정은 [Soft Delete 방식 적용](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-Soft-Delete-%EB%B0%A9%EC%8B%9D-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)에서 자세히 확인할 수 있습니다.

**댓글과 좋아요**

게시글 내에 댓글 작성 기능을 제공하고 있으나, 대댓글 작성 기능은 제공하고 있지 않습니다.

게시글에 대해 좋아요 표시를 할 수 있으며 이 데이터를 통해 게시글 리스트 조회 시 필터링으로 사용됩니다. 

**말머리 데이터 관리**

일종의 카테고리라고 볼 수 있는 말머리 데이터에 대해서는 각 게시판 마다 고유의 말머리가 존재하며, 해당 데이터의 추가 및 삭제는 관리자만이 할 수 있습니다. 

### 그외 회고 및 문서화
- [은비 - 인프라 아키텍처 설계 및 구성 과정](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9D%B8%ED%94%84%EB%9D%BC-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EA%B5%AC%EC%84%B1)
- [태민 - 인프라 구축 및 무중단 배포 프로세스](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9D%B8%ED%94%84%EB%9D%BC-%EA%B5%AC%EC%B6%95-%EB%B0%8F-%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4)
- [태민 - 관리자 페이지(client with Thymeleaf)](https://github.com/ODOICHON/server/wiki/%5B%EA%B4%80%EB%A6%AC%EC%9E%90-%ED%8E%98%EC%9D%B4%EC%A7%80%5D-%08client-with-Thymeleaf)  
- [태민 - server health-check](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-server-health-check) 
- [태민 - CI/CD workflow](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-CI-CD-workflow)  
- [태민 - 배포 도중 503에러](https://github.com/ODOICHON/server/wiki/%5B%EC%9D%B4%EC%8A%88%5D-%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC-%EB%8F%84%EC%A4%91-503-%EC%97%90%EB%9F%AC)  
- [태민 - 로깅 & 로그관리](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-Service-layer-logging)
- [태민 - 캐시 적용](https://github.com/ODOICHON/server/wiki/%5B%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%5D-%EA%B2%8C%EC%8B%9C%EB%AC%BC-%EC%A1%B0%ED%9A%8C-%EC%BA%90%EC%8B%9C-%EC%A0%81%EC%9A%A9)
- [태민 - 쿼리 통일화](https://github.com/ODOICHON/server/wiki/%5B%EC%84%B1%EB%8A%A5%5D-Querydsl%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%9C-%EC%BF%BC%EB%A6%AC-%ED%86%B5%EC%9D%BC%ED%99%94)
- [태민 - 이미지 캐싱 & 리사이징](https://github.com/ODOICHON/server/wiki/%5B%EC%84%B1%EB%8A%A5%5D-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%BA%90%EC%8B%B1-&-%EB%A6%AC%EC%82%AC%EC%9D%B4%EC%A7%95-with-CloudFront)
- [은비 - ERD 설계](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-ERD-%EC%84%A4%EA%B3%84)
- [은비 - Jasypt 라이브러리를 이용한 YML 파일 암호화하기](https://github.com/ODOICHON/server/wiki/%5Bproject%5D-Jasypt-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-YML-%ED%8C%8C%EC%9D%BC-%EC%95%94%ED%98%B8%ED%99%94%ED%95%98%EA%B8%B0)
- [은비 - 테스트 코드를 작성하는 이유](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C%5D-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C%EB%A5%BC-%EC%9E%91%EC%84%B1%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0)
- [은비 - RestDocs 적용 과정](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C%5D-RestDocs-%EC%82%AC%EC%9A%A9%EC%97%90-%EB%94%B0%EB%A5%B8-Controller-%EB%8B%A8%EC%9C%84-%ED%85%8C%EC%8A%A4%ED%8A%B8-(-e2e-Test-))

- [은비 - findByIdOrThrow 함수 커스터마이징하기](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D--Select-%EC%BF%BC%EB%A6%AC-%EB%B0%9C%EC%83%9D-%EC%8B%9C,-Exception-%EC%B2%98%EB%A6%AC-%ED%95%9C-%EB%B2%88%EC%97%90-%ED%95%98%EA%B8%B0)
- [은비 - Jacoco + SonarlCloud 적용과정](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%B8%ED%8C%85%5D-Jacoco-&-SonarCloud-%EC%A0%81%EC%9A%A9)
- [은비 - SonarCloud 세팅 중 마주한 이슈 정리](https://github.com/ODOICHON/server/wiki/%5B%EC%9D%B4%EC%8A%88%5D-SonarCloud-%EC%84%A4%EC%A0%95-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)
- [은비 - Github Actions와 SonarCloud 사용 중 트러블 슈팅 정리](https://github.com/ODOICHON/server/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85%5D-Github-Actions---SonarCloud)
- [은비 - 주말의집 서버팀은 이렇게 일해요!](https://github.com/ODOICHON/server/wiki/%5B%ED%8C%80%EB%AC%B8%ED%99%94%5D-%EC%A3%BC%EB%A7%90%EC%9D%98%EC%A7%91-%EC%84%9C%EB%B2%84%ED%8C%80%EC%9D%80-%EC%9D%B4%EB%A0%87%EA%B2%8C-%EC%9D%BC%ED%95%B4%EC%9A%94!)
- [은비 - Validator 커스터마이징하여 글자수 유효성 검사하기](https://github.com/ODOICHON/server/wiki/%5B%EA%B8%B0%EB%8A%A5%EA%B5%AC%ED%98%84%5D-Validator-%EC%B2%98%EB%A6%AC%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%BB%A4%EC%8A%A4%ED%84%B0%EB%A7%88%EC%9D%B4%EC%A7%95)
- [은비 - 객체지향적 코드 작성을 위한 임베디드 방식 적용하기](https://github.com/ODOICHON/server/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81%EC%9A%A9%5D-%EA%B0%9D%EC%B2%B4%EC%A7%80%ED%96%A5%EC%A0%81-%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1%EC%9D%84-%EC%9C%84%ED%95%9C-%EB%85%B8%EB%A0%A5,-@Embedded-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0)
- [은비 - 게시글 내용의 데이터 타입 변경 이유](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%EC%9D%B4%EB%B8%94-%EC%84%A4%EA%B3%84%5D-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EB%82%B4%EC%9A%A9%EC%9D%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%ED%83%80%EC%9E%85-%EC%A7%80%EC%A0%95,-varchar-vs-text)
- [은비 - 프로젝트 설계(추가된 내용 반영)](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9D%B8%ED%94%84%EB%9D%BC-%EB%B0%8F-%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EC%84%A4%EA%B3%84)
- [민혁 - Spring Security 없이 인증인가 구현하기](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-Spring-Security-%EC%97%86%EC%9D%B4-%EC%9D%B8%EC%A6%9D%EC%9D%B8%EA%B0%80-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
- [민혁 - DDOS 대응 방안](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-DDOS-%EB%8C%80%EC%9D%91-%EB%B0%A9%EC%95%88)
- [민혁 - 유저 탈퇴 처리](https://github.com/ODOICHON/server/wiki/%5BAPI-%EA%B5%AC%ED%98%84%5D-%EC%9C%A0%EC%A0%80-%ED%83%88%ED%87%B4-%EC%B2%98%EB%A6%AC)
- [민혁 - 리프레시 토큰 저장방식 변경](https://github.com/ODOICHON/server/wiki/%5B%EC%9D%B4%EC%8A%88%5D-%EB%A6%AC%ED%94%84%EB%A0%88%EC%8B%9C-%ED%86%A0%ED%81%B0-%EC%A0%80%EC%9E%A5%EB%B0%A9%EC%8B%9D-%EB%B3%80%EA%B2%BD)
- [민혁 - 유저 테이블 암호화](https://github.com/ODOICHON/server/wiki/%5B%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84%5D-%EC%9C%A0%EC%A0%80-%ED%85%8C%EC%9D%B4%EB%B8%94-%EC%95%94%ED%98%B8%ED%99%94)
- [민혁 - 테크블로그(ERD 설계)](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%ED%81%AC%EB%B8%94%EB%A1%9C%EA%B7%B8%5D-ERD-%EC%84%A4%EA%B3%84)
- [민혁 - 테크블로그(하위 경로 이슈)](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%ED%81%AC%EB%B8%94%EB%A1%9C%EA%B7%B8%5D-%ED%95%98%EC%9C%84-%EA%B2%BD%EB%A1%9C-%EC%9D%B4%EC%8A%88)
- [민혁 - 테크블로그(무한 대댓글)](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%ED%81%AC%EB%B8%94%EB%A1%9C%EA%B7%B8%5D-%EB%AC%B4%ED%95%9C-%EB%8C%80%EB%8C%93%EA%B8%80)
- [민혁 - 테크블로그(리뷰 프로세스 시나리오)](https://github.com/ODOICHON/server/wiki/%5B%ED%85%8C%ED%81%AC%EB%B8%94%EB%A1%9C%EA%B7%B8%5D-%EB%A6%AC%EB%B7%B0-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4-%EC%8B%9C%EB%82%98%EB%A6%AC%EC%98%A4)

### 버전 관리
🏷 VERSION 0.0.1 - 로그인/회원가입 API 연동 (2023.03.39)

🏷 VERSION 0.0.2 - 메인 페이지 화면 작업 및 소개 게시판 API 연동 (2023.04.25)

🏷 VERSION 0.0.3 - 관리자 페이지 개발 (2023.05.10)

🏷 VERSION 0.0.4 - 커뮤니티 게시판 API 연동 (2023.06.01)

🏷 VERSION 0.0.5 - 백오피스 메인 기능 개발 (2023.06.15)

🏷 VERSION 0.0.6 - 빈집 게시판 API 연동 (2023.06.xx)

### 팀 구성
| Name    | <center>이은비</center>|<center>윤태민</center> |<center>문민혁</center> | 
| ------- | --------------------------------------------- | ------------------------------------ | --------------------------------------------- | 
| Profile | <center> <img width="110px" height="110px" src="https://avatars.githubusercontent.com/u/61505572?v=4" /> </center>|<center><img width="110px" height="110px" src="https://avatars.githubusercontent.com/u/80155336?v=4" /></center>|<center><img width="110px" height="110px" src="https://avatars.githubusercontent.com/u/102985637?v=4" /></center>|
| Role    | <center>Team Leader<br> Back-end, DevOps</center>   | <center>Back-end, <br> DevOps</center>    | <center>Back-end ,<br> DevOps</center>  | 
| Part    | <center> 프로젝트 세팅 <br> 커뮤니티 API <br> 빈집 거래 API <br> 테크 블로그 프론트엔드 100% 개발 </center>   | <center> 인프라 세팅 <br> 관리자페이지(SSR) 100% 개발 <br> 성능 개선(캐싱, 동적쿼리 리펙토링) </center>    | <center> 사용자 API <br> DDoS 방지 처리 <br> 테크 블로그 API 100% 개발 </center>  | 
GitHub | <center>[@dldmsql](https://github.com/dldmsql)</center> | <center>[@YoonTaeMinnnn](https://github.com/YoonTaeMinnnn) </center>| <center>[@MoonMinHyuk1](https://github.com/MoonMinHyuk1) </center>|
