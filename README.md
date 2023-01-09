# dhhan0430.RealEstateWebServer

# Description
특정 지역 부동산 매물들의 현재 시세와 각 매물의 주변 편의 시설 정보들을 함께 제공해주는 웹 서비스 개발.

# Development Scope
웹 서버 백엔드

# Tech Stack
Java / Spring Boot / Spring MVC / Spring Data JPA / MySQL

# Development Environment
Linux / IntelliJ

# Usage
1. 소스 코드 빌드
2. RealEstate/src/main/resources/application.yml 수정
  2-1. Date -> Year, Month (검색하고자 하는 매물 거래일자 설정)
  2-2. 국토교통부(Molit) open api 신청(아파트매매 실거래자료, 연립다세대 매매 실거래자료) 후 Molit -> ServiceKey 작성
  2-3. KakaoMap open api 신청 후 KakaoMap -> Authorization 작성
  2-4. spring -> datasource -> password 설정
