# pharmacy-explorer

> 특정 위치에서 가까운 약국 탐색기 서비스

# 요구사항

약국 찾기 서비스 요구사항

- 위도, 경도가 포함된 약국 현황 데이터를 기반으로 주소 정보를 입력 시 해당 데이터 기반 가까운 3곳의 정보를 반환한다.
    - 입력 데이터인 주소는 도로명주소 혹은 지번주소로 한다.
        - 데이터 입력 시 [카카오 우편번호 서비스](https://postcode.map.daum.net/guide)를 이용할 예정이다.
        - 동, 호수는 불필요하니 지번, 혹은 도로명까지만 입력한다.
    - 가까운 3곳에 대한 정보 추출 시 아래의 방법으로 구분한다.
        - 구체의 두 점 사이의 거리 관련 알고리즘 ([Haversine Formula](https://en.wikipedia.org/wiki/Haversine_formula))
        - 반경 10km 내에 없을 경우 아무것도 반환하지 않는다.
        - 추출한 약국 정보에 대한 길안내 URL을 반환한다
            - URL 가독성을 위해 shortcut url 활용 예정

# 수행 흐름

```mermaid
sequenceDiagram
    actor User
    participant Service
    participant Cache
    participant RDB
    participant KakaoAPI
    User ->> Service: 1. 주소 입력 (도로명 or 지번)
    Service ->> KakaoAPI: 2. 입력 데이터 기반 위치 정보 조회 요청
    KakaoAPI ->> Service: 3. 입력 데이터 기반 위치 정보 조회 반환 (위도, 경도 포함)
    Service ->> Cache: 4-a-1. 현재 위치 기반 약국 정보 조회 요청
    Cache ->> Service: 4-a-2. 캐싱된 데이터가 존재할 시 데이터 반환
    Service ->> RDB: 4-b-1. 캐싱된 데이터가 없을 시 저장된 약국 현황 DB에 조회 요청
    RDB ->> Service: 4-b-2. 현재 위치 기반 약국 정보 데이터 반환
    Service ->> Service: 5. 알고리즘을 통해 근방 3곳의 약국 정보 추출
    Service ->> RDB: 6. 추출된 약국들에 대한 길찾기 URL 및 shortcut URL 매핑 저장
    Service ->> User: 7. 조회된 약국들에 대해 구성된 shortcut URL 출력

```

# 프로젝트 구성 항목

- Java 11
- Spring boot 2.7.16
- Spring boot JPA
- Spring boot Web
- Spring boot Devtools
- Spring boot Configuration Processor
- Lombok
- h2
- mariaDB

# ERD

```mermaid
erDiagram
    PHARMACY {
        bigint id PK
        varchar[255] address
        varchar[255] name
        double longtitude
        double latitude
        timestamp created_date
        timestamp modified_date
    }
```