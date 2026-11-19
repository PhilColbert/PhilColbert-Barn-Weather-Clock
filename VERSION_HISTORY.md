# Version History

## v1.1 — 2026-11-19
- BWC-CHG-014: Reworked the native widget to match the approved screenshot: left clock/date, centre current weather, right next-day forecast; icons left of details; live Now temperature; separate Day/Night rows; wind speed and direction; date at the bottom of each weather column.
- Removed the v1.0-only top weather dates, gust rows and blue-purple gradient edge, which were not present in the approved screenshot.
- Tapping anywhere on the widget now opens that widget's settings.
- Added a visible Remove widget help action in settings. Actual deletion is delegated to the Android launcher using its standard long-press > Remove action because the launcher owns the widget ID.
- Preserved live Open-Meteo, manual/device location configuration, appearance preferences and 30-minute refresh.
- Incremented versionName to 1.1 and versionCode to 7.
- Source committed to feature/android-starter. CI compilation, APK inspection and physical-device screenshot comparison pending.

