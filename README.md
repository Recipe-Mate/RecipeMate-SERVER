# RecipeMate-SERVER

## 📝 Introduce

RecipeMate 백엔드는 프론트엔드와 OCR, AI 모델의 후처리 과정을 통해 얻은 식재료 데이터를 관리하고 외부 리소스를 통해 레시피 관련 기능을 제공, 대체 가능한 추천 식재료 정보를 제공합니다.

## ⚙️ Develop Environment

<div align="center">
	<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/></a>
	<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/></a>
	<img src="https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white"/></a>
	<img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white"/></a>
</div>
<br>
<div align="center">
	<img src="https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white"/></a>
	<img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"/></a>
	
</div>


## **Contributor**  

| 최민영 |
|:----------|
| <img src="https://github.com/2024-MobileProgramming/MP-Server/assets/97686638/f09cd579-9677-45c8-90f0-5a1b8c5dd8c3" width="300" /> |
|[@codingmy](https://github.com/codingmy, "codingmy")|



## 설치 및 실행 방법
### 요구사항
- Java 17 이상
- Gradle 7.5.1 이상
- Spring Boot 3.3.2
- MySQL 8.0.32 이상

### 설치 및 실행
```
./gradlew build
./gradlew bootRun
```

## **System architecture**
![Image](https://github.com/user-attachments/assets/1ff22a17-30b3-4a52-84c9-73a57f92c498)

## **ER Diagram**
![Image](https://github.com/user-attachments/assets/dfeee1a8-82ca-4441-a626-f5f0dc7bc3dc)

## **API Docs**
[**🔗RecipeMate-BE 바로가기**](https://broad-furniture-095.notion.site/api-215bf26cec1b80d5a988cb0a028c2ad8?pvs=73)
- 회원가입, 로그인, 유저 정보 조회
- 구매한 식재료 추가, 삭제, 조회
- 특정 식재료와 식습관 목표에 적합한 레시피 검색
- 조리한 레시피 내역 관리 

## **개발 사항**
- 카카오 소셜 로그인 구현, 기존의 자체 회원 시스템을 마이그레이션
- JWT 토큰을 통한 인증/인가 구현
- 식품의약품안전처의 OPEN API 연동으로 레시피 DB 및 관련 기능구현
- GEMINI AI API 연동을 통한 데이터 고도화
   - 효율적인 리소스 사용을 목적으로 비동기적 로직의 구현 
- Nginx, Aws 서비스를 통한 배포 진행
- AWS S3 이미지 서버 연동

## **Directory**

<pre>
```
├─🗂️ main
│  ├─🗂️ java
│  │  └─🗂️ org
│  │          └─🗂️ RecipeMate
│  │              ├─🗂️ auth
│  │              ├─🗂️ common
│  │              │  └─🗂️ constant       //상수화 진행
│  │              │  └─🗂️ enums
│  │              │  └─🗂️ exception
│  │              │  └─🗂️ response
│  │              │  └─🗂️ validation
│  │              ├─🗂️ config
│  │              ├─🗂️ external
│  │              ├─🗂️ food
│  │              ├─🗂️ jwt
│  │              ├─🗂️ recipe
│  │              ├─🗂️ user
│  └─🗂️ resources
       ├─static
       └─templates

```
</pre>





## **Branch Convention**
- Git-Flow 방식으로 진행
- main 브랜치 :  배포 브랜치

**브랜치명 예시** : feat/#9-api-setting


## **Git 커밋 컨벤션**

*# feat : 기능 추가*

*# fix : 버그 수정*

*# docs : 문서 수정*

*# add : 파일, 의존성 추가*

*# refactor : 코드 리팩토링*

*# style : 코드 의미에 영향을 주지 않는 변경사항*

*# chore : 빌드 부분 혹은 패키지 매니저 수정사항*

**커밋 예시** : [feat/#38] 유저 스케줄링 수정


## **Convention**
[캠퍼스 핵데이 Java 코딩 컨벤션](https://naver.github.io/hackday-conventions-java/, "캠퍼스 핵데이 Java 코딩 컨벤션")
