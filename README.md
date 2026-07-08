# 오늘도 한걸음

Android 기반 만보기 앱입니다. 걸음 수를 측정하고 목표 달성 여부, 걷기 시간, 거리, 칼로리, 날씨 정보를 함께 보여주는 것을 목표로 제작했습니다.

## 프로젝트 목적

2021년 졸업 과제로 제작한 만보기 앱을 현재 Android 개발 환경에서 다시 빌드할 수 있도록 정리한 프로젝트입니다.

현재 코드는 기존 기능을 유지하면서 API 28 실행 기준, 최신 Gradle 환경, GitHub 공개 기준에 맞게 정리한 상태입니다. 실제 앱 기능은 기본 만보기 흐름을 우선 복구한 상태이며, 날짜별 기록 저장과 위치 기반 경로 기록은 아직 추가 구현이 필요합니다.

## 전체 시스템 구조

```text
02_최종 App/
├─ README.md
├─ build.gradle
├─ settings.gradle
├─ gradle.properties
├─ gradlew
├─ gradlew.bat
├─ gradle/
│  └─ wrapper/
└─ app/
   ├─ README.md
   ├─ build.gradle
   ├─ proguard-rules.pro
   └─ src/
      ├─ main/
      ├─ test/
      └─ androidTest/
```

| 경로 | 설명 |
| --- | --- |
| `README.md` | 프로젝트 전체 구조, 실행 방법, 검증 기준을 정리한 문서 |
| `build.gradle` | 프로젝트 단위 Gradle 설정 |
| `settings.gradle` | Gradle 모듈 포함 설정 |
| `gradle.properties` | Gradle과 Android 빌드 옵션 |
| `gradle/` | Gradle Wrapper 실행 파일 |
| `app/` | Android 앱 모듈 코드와 리소스 |

## 실행 또는 빌드 방법

명령어는 프로젝트 최상단인 `02_최종 App/`에서 실행합니다.

```bash
./gradlew.bat assembleDebug
```

빌드가 성공하면 debug APK가 아래 경로에 생성됩니다.

```text
app/build/outputs/apk/debug/app-debug.apk
```

Windows PowerShell에서 JDK 경로를 명시해 실행해야 하는 경우에는 다음 방식으로 실행합니다.

```powershell
$env:JAVA_HOME='JDK_설치_경로'
.\gradlew.bat assembleDebug --warning-mode all
```

## 핵심 기능

현재 코드 기준으로 연결되어 있는 주요 기능입니다.

- 걸음 수 측정
- 목표 걸음 수 설정
- 걷기 시간 측정
- 이동 거리 및 소모 칼로리 계산
- 목표 달성 여부 표시
- 기상청 단기예보 기반 날씨 표시
- 현재 연/월 기준 동적 캘린더 표시
- Google Maps 화면 표시

## 사용 기술

| 항목 | 현재 코드 기준 |
| --- | --- |
| 앱 플랫폼 | Android |
| 주요 언어 | Java |
| 빌드 도구 | Gradle Wrapper `9.4.1` |
| Android Gradle Plugin | `9.2.0` |
| JDK | Eclipse Temurin JDK 21 |
| UI | Android XML Layout, AndroidX AppCompat |
| 센서 | `Sensor.TYPE_STEP_COUNTER` |
| 지도 | Google Play Services Maps |
| 데이터 저장 | `SharedPreferences` |
| 외부 API | 기상청 단기예보 XML API |

## 현재 코드 기준 주요 규격

| 항목 | 값 |
| --- | --- |
| `compileSdk` | 36 |
| `minSdk` | 24 |
| `targetSdk` | 28 |
| Java 컴파일 옵션 | Java 17 |
| 앱 패키지 | `com.cookandroid.a2b_03_onestep` |
| 앱 이름 | `오늘도 한걸음` |

## 테스트 또는 검증 방법

빌드 검증은 프로젝트 최상단에서 실행합니다.

```bash
./gradlew.bat assembleDebug --warning-mode all
```

테스트 소스셋 컴파일 흐름은 아래 명령으로 확인합니다.

```bash
./gradlew.bat testDebugUnitTest --warning-mode all
```

현재 테스트 파일은 실제 테스트 코드를 작성하지 않은 상태라 `NO-SOURCE`로 정상 종료됩니다.

2026-07-08 기준 확인한 내용입니다.

- `assembleDebug --warning-mode all` 빌드 성공
- `testDebugUnitTest --warning-mode all` 정상 종료
- 생성 APK 기준 `compileSdkVersion=36`, `minSdkVersion=24`, `targetSdkVersion=28` 확인

## 주의사항

- `local.properties`는 개인 로컬 환경 파일이므로 GitHub에 올리지 않습니다.
- Google Maps 화면을 실제로 표시하려면 `local.properties`에 `MAPS_API_KEY`를 추가해야 합니다.
- 현재 날씨 API service key는 코드에 직접 들어 있어 공개 저장소 기준으로 분리 작업이 필요합니다.
- Windows 한글 경로에서 빌드하기 위해 `android.overridePathCheck=true`를 사용하고 있습니다.
- 빌드 성공은 실제 기기 동작을 보장하지 않습니다. 발표나 시연 전에는 걸음 수 센서, 날씨 API 응답, 지도 표시, 설정 저장을 실제 기기에서 확인해야 합니다.
- 위치 기반 이동 경로 기록, 캘린더 날짜별 기록 저장, 백그라운드 만보기 기능은 아직 구현되지 않았습니다.

## 하위 문서

| 문서 | 설명 |
| --- | --- |
| [app/README.md](app/README.md) | Android 앱 모듈의 코드 구조, 화면 흐름, 설정값, 세부 주의사항 |
