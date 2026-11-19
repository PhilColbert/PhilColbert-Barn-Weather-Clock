# Barn Weather Clock — Project Specification

**Project ID:** BWC-001  
**Baseline:** v1.1  
**Status:** BWC-CHG-014 source implementation committed; build and device verification pending  
**Date:** 2026-11-19

## Purpose
A native Android home-screen widget showing current time/date, current outdoor weather, and the following day's forecast.

## Fixed requirements
- Native Kotlin Android AppWidget.
- Three-column horizontal layout matching the approved reference image: clock/date on the left; current conditions in the middle; next-day forecast on the right.
- Rounded dark slate panel with a restrained grey outline and subtle vertical separators. No blue-purple gradient border. Minimum widget size 360 × 134dp; resize minimum 320 × 119dp to retain the approved reference proportions.
- Clock shown at approximately 28sp with locale-aware date directly below.
- Current conditions: weather icon on the left; small bold “Now” label beside the live current temperature; “Day” and “Night” temperatures on separate lines; wind speed followed by compass direction; date at the bottom of the column.
- Forecast: weather icon on the left; “Day” and “Night” temperatures on separate lines; wind speed followed by compass direction; next-day date at the bottom of the column.
- No gust rows, no weather dates at the top of columns, and no settings cog/icon on the widget.
- Tapping anywhere on the widget opens that widget's configuration/settings screen.
- Settings retain manual latitude/longitude, optional one-shot fused device location, background colour and transparency controls, Open-Meteo attribution and installed version.
- A visible “Remove widget” help action explains the Android launcher removal process. The actual removal is performed by the launcher: press and hold the widget and choose Remove (or drag it to Remove). Android AppWidget hosts own widget IDs; the app cannot safely remove a host-owned widget itself.
- Weather is live Open-Meteo data. Celsius and mph.
- Preserve 30-minute WorkManager refresh, location permissions and manual-entry fallback.

## Approved visual reference
barn_weather_clock_widget_preview.png (approved 2026-11-19). The preview's outer wallpaper, status bar, navigation bar and launcher icons are not part of the widget. The widget itself is the rounded three-column panel.

## Technical approach
- Android AppWidget framework and Kotlin.
- Open-Meteo current conditions and two-day daily forecast.
- WorkManager refresh, respecting Android background limits.
- Preferences stored in SharedPreferences.
- Dedicated WidgetConfigActivity registered as the AppWidget configure Activity and also opened by a tap PendingIntent.

## Unresolved items
- **FIXED:** Manual latitude/longitude entry.
- **FIXED:** Optional one-shot foreground location retrieval; no background tracking.
- **FIXED:** Settings screen displays installed app version.
- **UNRESOLVED:** Reverse geocoding into a place name.
- **UNRESOLVED:** Physical Android device/API level for final visual comparison.
- **UNRESOLVED:** Exact launcher behaviour varies by Android launcher; standard long-press removal must be checked on the user's device.

## Acceptance criteria
1. Widget can be added from the Android widget picker.
2. Rendered layout matches the approved panel: three columns, relative sizing, colours, typography, icons and row positions.
3. Tapping the widget opens its settings.
4. Settings can save location and appearance preferences.
5. Launcher long-press removal remains available and the settings screen explains it.
6. Weather and dates update from live data; failure states do not crash.
7. APK builds successfully; build, emulator and physical-device tests are reported separately.
