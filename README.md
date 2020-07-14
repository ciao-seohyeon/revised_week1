# revised_week1


```
1.팀원 이름
2.프로젝트 이름
3.Git 저장소 URL (public으로 바꿔주세요)
4.설명
5.apk 파일
```



# 1. 팀원 이름

- 정서현

- 김상윤

  

# 2. 프로젝트 이름

**"My Memory"**

![thumbnail](C:\Users\tryot\Desktop\thumbnail.PNG)



# 3. Git 저장소 URL

- https://github.com/ciao-seohyeon/revised_week1.git

  

  

# 4. 설명

**1) 썸네일, 폰트, 어플 아이콘**

- SplashActivity : intent, thread 사용

**2) 전체 탭 구조**

- 스와이프 기능 구현 : ViewPager, Fragment 이용해서 탭 구성

**3) 탭 구성**

- tab1 : 전화번호부
  - 전체 구성 방식 : 리사이클러 뷰

  - 기능

    - JSON파일 읽어오기

        - JSONArray, JSONObject 이용

    - 휴대폰 전화번호부 불러오기 

        - ContactContract 이용

    - 전화번호부 삭제 기능

    - 문자메세지 보내기 기능

        - SmsManager 사용

        

- tab2 : 갤러리 

  - 전체 구성방식 : 리사이클러 뷰

  - 기능

      - 휴대폰 외부 디렉토리 사진 가져오기

          - getExternalFileDir 사용 : 외부 디렉토리 주소 얻기

      - 사진 찍고 최신화 하기

          - 버튼 클릭 시 Implicit Intent 방식으로 카메라 호출

            

- tab3 : 메모장
  - 전체 구성 방식 : 리사이클러 뷰
  - 기능
      - 로그인/메모장 추가 기능
          - SQLiteDatabase, SQLiteOpenHelper 를 활용해서 휴대폰 DB 접근
          - AlertDialog 이용해서 텍스트 입력



# 5. apk 파일

 [mymemory.apk](..\Downloads\mymemory.apk) 
