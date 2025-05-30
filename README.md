﻿# RecipeMate-SERVER

## **Contributor**  

| 최민영 |
|:----------|
| <img src="https://github.com/2024-MobileProgramming/MP-Server/assets/97686638/f09cd579-9677-45c8-90f0-5a1b8c5dd8c3" width="300" /> |
|[@codingmy](https://github.com/codingmy, "codingmy")|


## **ER Diagram**
<img src="https://github.com/user-attachments/assets/f893ec13-5e87-4982-a2bf-5baac100619f"/>

## **API Docs**
- 회원가입, 로그인, 유저 정보 조회
- 구매한 식재료 추가, 삭제, 조회
- 특정 식재료와 식습관 목표에 적합한 레시피 검색


## **Directory**

<pre>
```
├─🗂️ main
│  ├─🗂️ java
│  │  └─🗂️ org
│  │          └─🗂️ RecipeMate
│  │              ├─🗂️ recipe
│  │              ├─🗂️ food 
│  │              ├─🗂️ common
│  │              │  └─🗂️ exception
│  │              │  └─🗂️ response
|  |              ├─🗂️ user 
│  └─🗂️ resources
       ├─static
       └─templates

```
</pre>





## **Branch Convention**
- main
- develop
- issue 생성 후, git flow 전략으로 feat브랜치 생성
- feat브랜치 -> develop브랜치로 merge
- develop -> main 브랜치로 merge

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
