## Description
특정 지역 부동산 매물들의 현재 시세와 각 매물의 주변 편의 시설 정보들을 함께 제공해주는 웹 서비스 개발.

## Function
1. 부동산 매물 검색 (GET)
2. 검색한 부동산 매물에 대해 MySQL DB에 영구 저장 (POST)
3. MySQL DB에 저장된 부동산 매물 MyList 불러오기 (GET)
4. MySQL DB에 저장된 부동산 매물에 대해 정보 갱신 (PUT)
5. MySQL DB에 저장된 부동산 매물 삭제 (DELETE)

## Development Scope
웹 서버 백엔드

## Tech Stack
Java / Spring Boot / Spring MVC / Spring Data JPA / MySQL

## Development Environment
Linux / IntelliJ

## Usage
1. 소스 코드 빌드
2. DataSources and Drivers 설정 (MySQL DB 설정)
3. RealEstate/src/main/resources/application.yml 수정
  -  Date -> Year, Month (검색하고자 하는 매물 거래일자 설정)
  - 국토교통부(Molit) open api 신청(아파트매매 실거래자료, 연립다세대 매매 실거래자료) 후 Molit -> ServiceKey 작성
  - KakaoMap open api 신청 후 KakaoMap -> Authorization 작성
  - spring -> datasource -> password 설정
4. Talend API 다운로드
5. 스프링 서버 부팅
6. 튜토리얼 동영상(RealEstateWebServer_Tutorial.mp4) 참고하여 서버 제공 기능 사용
