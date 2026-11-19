# Barn Weather Clock — Project Specification

**Project ID:** BWC-001  
**Baseline:** v0.9  
**Status:** v0.9 implementation baseline  
**Date:** 2026-11-19

## Purpose
A native Android home-screen widget showing current time/date, current outdoor weather, and the following day's forecast.

## Fixed requirements
- Native Android AppWidget, not a shortcut or web page.
- Kotlin implementation using Android AppWidget APIs.
- Current time is prominent; current design target is **32sp**.
- Current date appears beneath the time.
- Current conditions: weather icon, live current temperature, today's maximum and minimum temperatures, displayed as two unlabeled values, wind speed in mph, compass direction, and date shown as day/month only (no weekday).
- Next-day forecast uses the same icon-left / details-right arrangement as current weather and shows the next day's maximum and minimum temperatures, displayed as two unlabeled values.
- Forecast column shows its actual calendar date. Do not show the words “Tomorrow” or an extra +1 day label.
- No settings cog or settings icon inside the widget.
- Appearance configuration is reached through the launcher's widget configuration flow.
- Appearance settings include background colour selection and adjustable transparency.
- Appearance preferences are persisted and applied to the widget.
- Compact horizontal widget: time/date on the left, current and next-day weather columns to the right.
- Weather values displayed must come from a live provider; mock-ups must be labelled illustrative.
- Weather location must be configurable; no private precise address is hard-coded.
- Configuration offers an optional current-device-location action using Google's fused location provider to populate latitude/longitude; manual coordinate entry remains available. The configuration screen displays the installed app version.
- Temperature display unit: Celsius.
- Wind speed display unit: mph.

## Proposed technical approach
- Android Studio project using Kotlin.
- Android AppWidget framework.
- Open-Meteo as the initial weather provider, subject to API verification and attribution requirements.
- WorkManager or another Android-appropriate refresh mechanism, respecting Android background limits.
- Android DataStore for persisted appearance and location preferences.
- Dedicated configuration Activity registered as the widget's configure screen.

## Approved layout baseline
- Single-line horizontal widget.
- Left: prominent 32sp current time, current date below.
- Middle: current weather icon on the left; a small “NOW” label immediately beside the larger live temperature on one line, followed by two unlabeled temperature values, wind speed/direction and current date (d MMM, no weekday) on the right.
- Right: next-day forecast icon on the left; two unlabeled temperature values, wind speed/direction and forecast date on the right.
- No “Tomorrow” label, no +1 day label, no settings icon.

## Unresolved items
- **FIXED for v0.2:** Manual latitude/longitude entry remains available.
- **FIXED for v0.4:** Optional one-shot current-device location retrieval in the configuration screen. The app requests foreground coarse/fine location permission only when the user taps the location button; it does not continuously track location. GPS provider is eligible with either permission level; Android may reduce coordinate precision when only approximate permission is granted. The fused provider uses a recent cached fix where available, otherwise requests a current fix and falls back to a bounded single update. The screen stops location requests after success or failure. The retrieved coordinates populate the fields and are applied only after Save settings.
- **FIXED for v0.6:** The settings screen displays `Barn Weather Clock v{versionName}` from the app build metadata.
- **UNRESOLVED:** Reverse geocoding of coordinates into a human-readable place name is not implemented.
- **UNRESOLVED:** Primary Android device model and Android API level for device testing.
- **PROPOSED:** Default slate background with 20% transparency.
- **UNRESOLVED:** Minimum widget dimensions and launcher resize behaviour.
- **FIXED for v0.2:** Locale-aware clock date display uses `EEE d MMM`. **FIXED for v0.8:** Current-weather date uses `d MMM` with no weekday.

## Acceptance criteria
1. Widget can be added from the Android widget picker.
2. Time updates and date remains locale-aware.
3. Current weather and the next calendar day's forecast display temperature, condition icon, wind speed and direction, with dates.
4. Weather location can be configured manually or populated from a one-shot device location request after user permission.
5. Background colour and transparency can be changed through widget configuration and persist after reboot.
6. Widget remains legible at supported sizes and transparency levels.
7. Network/location failures show a clear stale/unavailable state without crashing.
8. Denied location permission leaves manual coordinate entry usable.
9. No unrelated launcher behaviour is changed.
10. The widget configuration screen displays the installed app version.
11. APK builds successfully and installation/device tests are reported separately.
