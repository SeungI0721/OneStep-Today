# app

Android 앱 모듈입니다. 실제 만보기 화면, 설정 화면, 통계 화면, 지도 화면, 리소스 파일을 포함합니다.

## 이 폴더의 역할

`app/` 폴더는 `오늘도 한걸음` 앱의 실행 코드와 Android 리소스를 담당합니다.

현재 구현은 걸음 수 측정, 목표 설정, 기록 요약 표시, 날씨 표시, 동적 캘린더 표시, 기본 지도 화면 표시까지 연결되어 있습니다. 날짜별 기록 저장, 위치 기반 경로 기록, 백그라운드 측정은 아직 구현되지 않았습니다.

## 주요 파일 설명

| 경로 | 설명 |
| --- | --- |
| `build.gradle` | 앱 모듈 빌드 설정, 의존성, SDK 규격, Manifest placeholder 설정 |
| `src/main/AndroidManifest.xml` | 앱 권한, Activity, Service, Google Maps API Key 메타데이터 설정 |
| `src/main/java/com/cookandroid/a2b_03_onestep/MainActivity.java` | 메인 화면, 누적 기록 표시, 날씨 API 호출, 화면 이동 처리 |
| `src/main/java/com/cookandroid/a2b_03_onestep/Start.java` | 걸음 수 센서 측정, 세션 시간/거리/칼로리 계산 |
| `src/main/java/com/cookandroid/a2b_03_onestep/Setting.java` | 목표 걸음 수와 알림 설정 저장 |
| `src/main/java/com/cookandroid/a2b_03_onestep/Statistics.java` | 현재 기록 요약과 동적 캘린더 표시 |
| `src/main/java/com/cookandroid/a2b_03_onestep/MapActivity.java` | Google Maps 화면 표시 |
| `src/main/java/com/cookandroid/a2b_03_onestep/AppPreferences.java` | 앱 공통 `SharedPreferences` 접근 관리 |
| `src/main/java/com/cookandroid/a2b_03_onestep/MyService.java` | Manifest에 등록된 기본 Service |
| `src/main/res/layout/` | 화면별 XML 레이아웃 |
| `src/main/res/drawable/` | 날씨, 상태 표시, 아이콘 이미지 리소스 |
| `src/test/`, `src/androidTest/` | 테스트 소스셋. 현재 실제 테스트 코드는 비워둔 상태 |

## 실행 방법

앱 모듈만 직접 실행하지 않고, 프로젝트 최상단에서 Gradle Wrapper를 사용합니다.

```bash
cd "OneStep-Today"
./gradlew.bat assembleDebug
```

생성된 APK는 다음 경로에서 확인합니다.

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 코드 흐름

```text
MainActivity
├─ Start
│  └─ 걸음 수 센서 측정 후 MainActivity로 세션 기록 전달
├─ Setting
│  └─ 목표 걸음 수와 알림 설정을 SharedPreferences에 저장
├─ Statistics
│  └─ MainActivity에서 전달받은 기록 요약과 현재 연/월 달력 표시
└─ MapActivity
   └─ Google Maps 화면 표시
```

기록 저장은 DB가 아니라 `SharedPreferences`를 사용합니다. 현재 구조에서는 화면에 표시 중인 누적 기록을 저장하고 다시 불러오는 방식입니다.

## 외부 의존성

| 의존성 | 사용 목적 |
| --- | --- |
| AndroidX AppCompat | 기본 Activity 호환성 |
| AndroidX ConstraintLayout | 일부 레이아웃 구성 |
| Google Material Components | Android UI 구성 요소 |
| Google Play Services Maps | 지도 화면 표시 |
| JUnit, AndroidX Test, Espresso | 테스트 의존성. 현재 실제 테스트 코드는 작성되지 않음 |

## 주요 설정값

| 항목 | 값 또는 설명 |
| --- | --- |
| `namespace` | `com.cookandroid.a2b_03_onestep` |
| `applicationId` | `com.cookandroid.a2b_03_onestep` |
| `compileSdk` | 36 |
| `minSdk` | 24 |
| `targetSdk` | 28 |
| Java 컴파일 옵션 | Java 17 |
| 저장소 이름 | `OneStep-Today` |
| 지도 API Key | `local.properties`의 `MAPS_API_KEY` |
| 날씨 API Key | `local.properties`의 `WEATHER_API_KEY` |

`local.properties` 예시는 다음과 같습니다.

```properties
MAPS_API_KEY=발급받은_구글_맵_API_KEY
WEATHER_API_KEY=발급받은_기상청_API_SERVICE_KEY
```

`local.properties`는 개인 로컬 설정 파일이므로 GitHub에 올리지 않습니다.

## 저장 데이터

현재 `SharedPreferences`에 저장하는 주요 키입니다.

| 키 | 설명 |
| --- | --- |
| `goal` | 기본 목표 걸음 수 |
| `Roal` | 비 오는 날 목표 걸음 수 |
| `Soal` | 눈 오는 날 목표 걸음 수 |
| `Woal` | 기상 악화 목표 걸음 수 |
| `GSW` | 목표 달성 알림 설정 |
| `MSW` | 날씨 알림 설정 |
| `STEP` | 누적 걸음 수 |
| `HH`, `MM`, `SS` | 누적 시간 |
| `KK` | 누적 이동 거리 |
| `CC` | 누적 칼로리 |

## 테스트 방법

프로젝트 최상단에서 실행합니다.

```bash
./gradlew.bat assembleDebug --warning-mode all
```

```bash
./gradlew.bat testDebugUnitTest --warning-mode all
```

현재 테스트 파일은 설명 주석만 남겨둔 상태라 `testDebugUnitTest`는 `NO-SOURCE`로 정상 종료됩니다.

## 주의사항

- Google Maps API Key와 기상청 API service key는 `local.properties`로 분리되어 있으며, 공개 저장소에 올리지 않습니다.
- `MainActivity.java`의 날씨 좌표는 고정값입니다. 사용자 위치 기반 날씨는 아직 구현되지 않았습니다.
- `MapActivity.java`는 기본 지도 화면만 표시합니다. 사용자 현재 위치 표시와 이동 경로 선 그리기는 아직 구현되지 않았습니다.
- `Statistics.java`의 캘린더는 날짜를 계산해 보여주지만, 날짜별 기록 저장이나 조회 기능은 아직 없습니다.
- `MyService.java`는 현재 서비스 생명주기 로그 확인 정도의 기본 구현입니다. 백그라운드 만보기 측정과 직접 연결되어 있지 않습니다.
- `Temporary.java`, `ExampleUnitTest.java`, `ExampleInstrumentedTest.java`는 현재 사용하는 코드가 없어 설명 주석만 남긴 상태입니다.

## 관련 문서

| 문서 | 설명 |
| --- | --- |
| [../README.md](../README.md) | 프로젝트 전체 구조와 빌드 기준 |
