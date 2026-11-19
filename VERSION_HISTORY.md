# Version History

## v1.1 — 2026-11-19
- BWC-CHG-014: Reworked the native widget to match the approved screenshot: left clock/date, centre current weather, right next-day forecast; icons left of details; live Now temperature; separate Day/Night rows; wind speed and direction; date at the bottom of each weather column.
- Removed the v1.0-only top weather dates, gust rows and blue-purple gradient edge, which were not present in the approved screenshot.
- Tapping anywhere on the widget now opens that widget's settings.
- Added a visible Remove widget help action in settings. Actual deletion is delegated to the Android launcher using its standard long-press > Remove action because the launcher owns the widget ID. The provider cleans per-widget appearance preferences in onDeleted and cancels refresh work when the final widget is deleted.
- Matched widget minimum dimensions to the reference panel's approximate 2.69:1 aspect ratio (360 × 134dp; resize minimum 320 × 119dp).
- Preserved live Open-Meteo, manual/device location configuration, appearance preferences and 30-minute refresh.
- Incremented versionName to 1.1 and versionCode to 7.
- Source committed to feature/android-starter. CI compilation, APK inspection and physical-device screenshot comparison pending.


## BWC-CHG-015 — correction following user report (2026-11-19)
- Reverted the unapproved widget descriptor sizing to the exact v1.0 values: minWidth 360dp, minHeight 100dp, minResizeWidth 320dp, minResizeHeight 88dp. No intentional 5×2 size change is retained.
- Rebased the provider on the v1.0 provider implementation and retained only the required layout-ID mapping, tap-to-settings PendingIntent and launcher deletion cleanup. Existing weather refresh/scheduling logic is preserved.
- Replaced generic View separator elements with TextView elements, which are supported by Android RemoteViews, to address a likely cause of the launcher “Can't load widget” inflation failure.
- Source-level ID check passed. New CI build and device test pending; no device-load success claimed.
