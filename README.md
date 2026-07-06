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
│  │  ├─ AppPreferences.java
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

- Android Gradle Plugin: `9.2.0`
- Gradle Wrapper: `9.4.1`
- `compileSdk`: 36
- `minSdk`: 24
- `targetSdk`: 28
- Java 컴파일 옵션: Java 17
- ViewBinding: 사용

현재 설정 기준으로는 API 28 기기 실행 조건을 이미 만족합니다.  
Android Gradle Plugin과 Gradle Wrapper는 현재 공식 문서 기준 최신 안정 버전에 맞춰 정리했습니다.

## 주요 의존성

- AndroidX AppCompat
- AndroidX ConstraintLayout
- Google Material Components
- JUnit
- AndroidX Test
- Espresso

## 권한

Manifest에 선언된 주요 권한은 다음과 같습니다.

- `ACTIVITY_RECOGNITION`
- `INTERNET`
- `ACCESS_NETWORK_STATE`
- `VIBRATE`

## 데이터 저장 방식

앱 데이터는 `SharedPreferences`를 사용해 저장합니다.
저장소 이름은 `AppPreferences`에서 `OneStep`으로 통일해 관리합니다.

- 목표 걸음 수: `goal`
- 날씨별 목표: `Roal`, `Soal`, `Woal`
- 알림 스위치: `GSW`, `MSW`
- 누적 기록: `STEP`, `HH`, `MM`, `SS`, `KK`, `CC`

## 남은 확인 사항

빌드는 통과했지만 실제 기기에서 한 번 더 확인하면 좋은 부분입니다.

- 실제 기기에서 날씨 API 응답 확인
- 실제 기기에서 걸음 수 센서 지원 여부 확인
- 설정값 저장 후 앱 재실행 시 기록 유지 확인
- 백그라운드 만보기 기능이 필요할 경우 알림 채널과 포그라운드 서비스 추가

## 변경 기록

### 2026-07-06

- Android Gradle Plugin을 `7.0.3`에서 `9.2.0`으로 올렸습니다.
- Gradle Wrapper를 `7.0.2`에서 `9.4.1`로 올렸습니다.
- `targetSdk`를 API 28 기준으로 변경했습니다.
- `compileSdk`를 36으로 변경했습니다.
- Build Tools 버전을 `36.0.0`으로 지정했습니다.
- Android Gradle Plugin 9.x 요구사항에 맞춰 `namespace`를 추가했습니다.
- Android Gradle Plugin 9.x에서 무시되는 Manifest `package` 속성을 제거했습니다.
- Java 컴파일 옵션을 Java 17 기준으로 변경했습니다.
- 버전이 없는 중복 `material` 의존성을 제거했습니다.
- 테스트 의존성 `junit:junit:4.+`를 `4.13.2`로 고정했습니다.
- 루트 저장소 설정에서 더 이상 사용하지 않는 `jcenter()`를 제거했습니다.
- 더 이상 필요하지 않은 `android.enableJetifier=true` 설정을 제거했습니다.
- Windows 한글 경로에서 빌드 검증이 가능하도록 `android.overridePathCheck=true`를 추가했습니다.
- 실제 코드에서 사용하지 않는 Google Places, Google Maps, Google Location 의존성을 제거했습니다.
- 날씨 API 호출 주소를 `http`에서 `https`로 변경했습니다.
- 문자열 비교에 사용하던 `==`를 `.equals()` 기준으로 정리했습니다.
- 목표 달성 알림의 대입 조건 오류를 수정했습니다.
- 걸음 수 센서 리스너를 화면 일시정지 시 해제하도록 정리했습니다.
- 기록 저장소를 기본 `SharedPreferences` 기준으로 통일했습니다.
- `AppPreferences`를 추가해 저장소 이름을 한 곳에서 관리하도록 정리했습니다.
- Manifest에서 사용하지 않는 저장소/위치 권한과 Google Maps API Key를 제거했습니다.
- 내부 화면과 서비스의 `exported` 값을 `false`로 변경했습니다.
- 만보기 센서 feature를 `android.hardware.sensor.stepcounter`로 정리했습니다.
- Gradle 10 호환성을 위해 `app/build.gradle`의 속성 대입 문법을 최신 방식으로 정리했습니다.

### 2026-07-07

- 기존에 2021년 10월, 11월, 12월로 고정되어 있던 통계 화면 달력을 동적 캘린더로 변경했습니다.
- 현재 연도와 월을 기준으로 달력을 표시하도록 정리했습니다.
- `지난 달`, `이후 달` 버튼으로 매년 모든 월을 이동할 수 있도록 구현했습니다.
- 매월 1일의 요일과 마지막 일을 계산해 실제 달력 칸에 맞게 일을 표시하도록 구현했습니다.
- 오늘 날짜는 달력에서 더 진한 배경으로 표시하도록 정리했습니다.
- DB 없이 화면에서 연/월/일만 계산해 보여주는 구조로 구현했습니다.
- 통계 화면에서 숫자 값이 비어 있거나 잘못 전달되어도 기본값을 사용하도록 정리했습니다.

## 검증 메모

- debug APK는 `app/build/outputs/apk/debug/app-debug.apk`에 생성됩니다.
- `./gradlew.bat assembleDebug` 빌드가 성공했습니다.
- `./gradlew.bat assembleDebug --warning-mode all` 빌드가 성공했습니다.
- 생성된 APK 기준 `compileSdkVersion=36`, `minSdkVersion=24`, `targetSdkVersion=28`로 확인했습니다.
- 한글 파일과 XML 리소스는 UTF-8 기준으로 확인했습니다.
- Windows 한글 경로에서 빌드하기 위해 `android.overridePathCheck=true`를 사용하고 있습니다.
- 현재 남은 Gradle 출력은 한글 경로 우회 옵션이 실험적이라는 안내입니다.
