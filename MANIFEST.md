# Project Manifest

**Project:** Barn Weather Clock
**Project ID:** BWC-001
**Repository:** https://github.com/PhilColbert/PhilColbert-Barn-Weather-Clock
**Working branch:** feature/android-starter
**Default branch:** main
**Version:** v0.7
**Status:** v0.7 smaller clock committed; user build/device verification pending

## Tracked files

| Path | Version introduced | Status |
|---|---|---|
| SPECIFICATION.md | v0.1/v0.2 | Requirements and implementation decisions |
| CHANGE_REGISTER.md | v0.1/v0.2 | Change history |
| MANIFEST.md | v0.1/v0.2 | This file |
| VERSION_HISTORY.md | v0.1/v0.2 | Version record |
| `settings.gradle.kts` | v0.2 | New Android project file |
| `build.gradle.kts` | v0.2 | New Android project file |
| `gradle.properties` | v0.2 | New Android project file |
| `app/build.gradle.kts` | v0.2/v0.3/v0.4 | JVM targets 17; fused location dependency and app versionName 0.7 and versionCode 3 in BWC-CHG-006 |
| `app/src/main/AndroidManifest.xml` | v0.2/v0.4 | Added foreground coarse/fine location permission declarations in BWC-CHG-004; unchanged in v0.5 |
| `app/src/main/res/xml/barn_weather_widget_info.xml` | v0.2 | New Android project file |
| `app/src/main/res/values/strings.xml` | v0.2 | New Android project file |
| `app/src/main/res/layout/widget_barn_weather.xml` | v0.2/v0.7 | Clock reduced from 48sp to 32sp in BWC-CHG-007 |
| `app/src/main/java/uk/co/barnweatherclock/WeatherData.kt` | v0.2 | New Android project file |
| `app/src/main/java/uk/co/barnweatherclock/BarnWeatherWidgetProvider.kt` | v0.2 | New Android project file |
| `app/src/main/java/uk/co/barnweatherclock/WeatherRefresh.kt` | v0.2 | New Android project file |
| `app/src/main/java/uk/co/barnweatherclock/WidgetConfigActivity.kt` | v0.2/v0.4 | Added current-location button and permission flow in BWC-CHG-004; provider selection, cached-fix use and single-update listener in BWC-CHG-005; fused provider and version label in BWC-CHG-006 |
| `README.md` | v0.2 | New Android project file |

## Integrity and test status\n- v0.7 reduces the clock display to 32sp; versionName 0.7 and versionCode 3.\n- Files not changed by BWC-CHG-007 are carried forward byte-for-byte from v0.6; they were not rebuilt or device-tested.
- Documentation files originate from v0.1 and were updated for v0.2.
- Android project files were newly added in BWC-CHG-002.
- All 17 tracked project and record files were fetched from the feature branch successfully after commit.
- No files are claimed as rebuilt or device-tested.
- JVM target correction v0.3 was reported working by the user.
- v0.6 fused location and version label are committed; compilation and physical-device permission/location behaviour are awaiting user verification.
- No APK has been produced by this workflow.
- Visual baseline screenshots and matching after-change screenshots are not yet available; no emulator/device has been supplied or tested.
- Build, runtime and launcher compatibility remain unverified.
