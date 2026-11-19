# Project Manifest

**Project:** Barn Weather Clock  
**Project ID:** BWC-001  
**Repository:** https://github.com/PhilColbert/PhilColbert-Barn-Weather-Clock  
**Working branch:** feature/android-starter  
**Default branch:** main  
**Version:** v1.1  
**Status:** BWC-CHG-014 committed on feature branch; CI build pending; physical-device visual test pending

## Tracked files

| Path | Status |
|---|---|
| SPECIFICATION.md | Updated to v1.1 approved screenshot requirements |
| CHANGE_REGISTER.md | BWC-CHG-014 logged before implementation |
| MANIFEST.md | v1.1 file and verification register |
| VERSION_HISTORY.md | v1.1 change record |
| README.md | v1.1 behaviour and usage |
| app/src/main/res/layout/widget_barn_weather.xml | Reworked three-column widget to match approved screenshot
| app/src/main/res/xml/barn_weather_widget_info.xml | Minimum size 360 × 134dp; resize minimum 320 × 119dp to match reference proportions |
| app/src/main/res/drawable/widget_frame.xml | Solid slate panel with restrained outline and rounded corners |
| app/src/main/java/uk/co/barnweatherclock/BarnWeatherWidgetProvider.kt | Widget-wide tap opens its own settings; current/forecast rows match screenshot; onDeleted clears per-widget appearance preferences and onDisabled cancels refresh work |
| app/src/main/java/uk/co/barnweatherclock/WidgetConfigActivity.kt | Adds visible removal guidance; retains location and appearance settings |
| app/build.gradle.kts | versionName 1.1, versionCode 7 |
| .github/workflows/android-build.yml | CI debug APK workflow; artifact label barn-weather-clock-v1.1-debug |

## Integrity and test status
- Previous v0.8 CI build run 44540883299 succeeded; this does not verify v1.1.
- v1.1 source changes are committed on feature/android-starter only. main has not been intentionally modified.
- Source-level review and GitHub fetch verification are pending final pass.
- Initial CI run 44547837308 failed at Android resource linking because the first layout draft used invalid percentage heights for separators. The offending values were replaced with valid match_parent heights and dp margins in commit 090652523826bea441154bfa4d642ea4f2353be7. A fresh CI run 44548191284 is queued; successful compilation is not yet established.
- CI compilation and APK artifact inspection are pending.
- No v1.1 APK is yet claimed.
- No emulator or physical-device installation/rendering test is claimed.
- Exact visual comparison against a real device screenshot remains pending.
- Unchanged project files are carried forward from v1.0 and are not claimed rebuilt or tested.
