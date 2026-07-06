# 오늘도 한걸음

2021년 졸업 과제로 제작한 Android 만보기 앱입니다.  
걸음 수를 측정하고, 목표 걸음 수 달성 여부와 걷기 시간, 거리, 칼로리, 날씨 정보를 함께 보여주는 것을 목표로 만들었습니다.

현재 이 저장소는 기존 프로젝트를 최신 개발 환경에 맞게 정리하고 업그레이드하기 전의 기준 상태를 기록하고 있습니다.

## 주요 기능

- 걸음 수 측정
- 목표 걸음 수 설정
- 걷기 시간 측정
- 이동 거리 및 소모 칼로리 계산
- 목표 달성 여부 표시
- 날씨 정보 표시
- 월별 통계 화면 제공

## 프로젝트 구성

```text
02_최종 App/
├─ app/
│  ├─ src/main/java/com/cookandroid/a2b_03_onestep/
│  │  ├─ MainActivity.java
│  │  ├─ Start.java
│  │  ├─ Setting.java
│  │  ├─ Statistics.java
│  │  ├─ MyService.java
│  │  └─ Temporary.java
│  ├─ src/main/res/
│  └─ build.gradle
├─ build.gradle
├─ settings.gradle
└─ gradle/
```

## 화면별 역할

### MainActivity

앱의 메인 화면입니다. 저장된 걸음 수, 목표 걸음 수, 누적 시간, 거리, 칼로리를 보여줍니다.  
걷기 측정 화면에서 돌아오면 전달받은 세션 기록을 기존 누적값에 더해 화면에 반영합니다.

또한 기상청 단기예보 XML API를 호출해 기온, 하늘 상태, 강수 형태를 표시하고 날씨에 맞는 아이콘을 보여줍니다.

### Start

걷기 측정 화면입니다.  
`Sensor.TYPE_STEP_COUNTER` 센서를 사용해 현재 세션의 걸음 수를 계산하고, 걸음 수를 기준으로 거리와 칼로리를 산출합니다.

측정이 끝나면 걸음 수, 시간, 거리, 칼로리 값을 `MainActivity`로 전달합니다.

### Setting

목표 걸음 수와 알림 설정을 관리하는 화면입니다.  
기본 목표, 비 오는 날 목표, 눈 오는 날 목표, 기상 악화 목표를 입력받고 `SharedPreferences`에 저장합니다.

### Statistics

통계 화면입니다.  
현재 걸음 수, 거리, 칼로리를 표시하고, 2021년 10월, 11월, 12월 통계 레이아웃을 버튼으로 전환합니다.

현재 통계 화면은 데이터베이스 기반의 동적 통계가 아니라 레이아웃에 구성된 정적 화면에 가깝습니다.

### MyService

Manifest에 등록되어 있는 기본 서비스입니다.  
현재는 시작과 종료 로그를 확인하는 정도의 구현이며, 만보기 핵심 기능과 직접 연결되어 있지는 않습니다.

## 현재 빌드 환경

- Android Gradle Plugin: `7.0.3`
- Gradle Wrapper: `7.0.2`
- `compileSdk`: 31
- `minSdk`: 24
- `targetSdk`: 30
- Java 컴파일 옵션: Java 8
- ViewBinding: 사용

현재 설정 기준으로는 API 28 기기 실행 조건을 이미 만족합니다.  
이후 작업에서는 API 28 대응 여부를 다시 확인하면서 Gradle, Android Gradle Plugin, JDK 버전을 함께 정리할 예정입니다.

## 주요 의존성

- AndroidX AppCompat
- AndroidX ConstraintLayout
- Google Material Components
- Google Play Services Maps
- Google Play Services Location
- Google Places
- JUnit
- AndroidX Test
- Espresso

## 권한

Manifest에 선언된 주요 권한은 다음과 같습니다.

- `ACTIVITY_RECOGNITION`
- `INTERNET`
- `WRITE_EXTERNAL_STORAGE`
- `ACCESS_NETWORK_STATE`
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `VIBRATE`

## 데이터 저장 방식

앱 데이터는 `SharedPreferences`를 사용해 저장합니다.

- 목표 걸음 수: `goal`
- 날씨별 목표: `Roal`, `Soal`, `Woal`
- 알림 스위치: `GSW`, `MSW`
- 누적 기록: `STEP`, `HH`, `MM`, `SS`, `KK`, `CC`

## 정리 및 개선 예정

업그레이드 전후로 확인이 필요한 부분입니다.

- Gradle, Android Gradle Plugin, JDK 버전 정리
- API 28 기준 동작 확인
- 최신 Android 환경에서 권한 처리 방식 점검
- 백그라운드 만보기 기능이 필요할 경우 포그라운드 서비스 구조 검토
- 날씨 API의 HTTP 통신 방식 점검
- Manifest에 포함된 API Key 관리 방식 개선
- 중복 의존성 정리
- 문자열 비교 로직 정리
- `SharedPreferences` 저장소 사용 방식 통일
- 센서 리스너 등록/해제 흐름 점검

## 검증 메모

- 기존 debug APK는 `app/build/outputs/apk/debug/app-debug.apk`에 있습니다.
- 기존 APK 기준 `compileSdkVersion=31`, `minSdkVersion=24`, `targetSdkVersion=30`으로 확인했습니다.
- 한글 파일과 XML 리소스는 UTF-8 기준으로 확인했습니다.
