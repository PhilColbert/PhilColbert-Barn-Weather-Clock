# Barn Weather Clock — Project Specification

**Project ID:** BWC-001  
**Baseline:** v0.1  
**Status:** Initial specification  
**Date:** 2026-11-19

## Purpose
A native Android home-screen widget showing current time/date, current outdoor weather, and the following day's forecast.

## Fixed requirements
- Native Android AppWidget, not a shortcut or web page.
- Kotlin implementation using Android AppWidget APIs.
- Current time is prominent; current design target is **48sp**.
- Current date appears beneath the time.
- Current conditions: weather icon, temperature, wind speed in mph, compass direction, and date.
- Next-day forecast uses the same icon-left / details-right arrangement as current weather.
- Forecast column shows its actual calendar date. Do not show the words “Tomorrow” or an extra +1 day label.
- No settings cog or settings icon inside the widget.
- Appearance configuration is reached through the launcher's widget configuration flow.
- Appearance settings include background colour selection and adjustable transparency.
- Appearance preferences are persisted and applied to the widget.
- Compact horizontal widget: time/date on the left, current and next-day weather columns to the right.
- Weather values displayed must come from a live provider; mock-ups must be labelled illustrative.
- Weather location must be configurable; no private precise address is hard-coded.
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
- Left: large 48sp current time, current date below.
- Middle: current weather icon on the left; temperature, wind speed/direction and current date on the right.
- Right: next-day forecast icon on the left; temperature, wind speed/direction and forecast date on the right.
- No “Tomorrow” label, no +1 day label, no settings icon.

## Unresolved items
- **UNRESOLVED:** Default weather location mode (device location or manually entered saved location).
- **UNRESOLVED:** Primary Android device model and Android API level for device testing.
- **UNRESOLVED:** Default background colour and transparency.
- **UNRESOLVED:** Minimum widget dimensions and launcher resize behaviour.
- **UNRESOLVED:** Preferred date formatting.

## Acceptance criteria
1. Widget can be added from the Android widget picker.
2. Time updates and date remains locale-aware.
3. Current weather and the next calendar day's forecast display temperature, condition icon, wind speed and direction, with dates.
4. Weather location can be configured.
5. Background colour and transparency can be changed through widget configuration and persist after reboot.
6. Widget remains legible at supported sizes and transparency levels.
7. Network/location failures show a clear stale/unavailable state without crashing.
8. No unrelated launcher behaviour is changed.
9. APK builds successfully and installation/device tests are reported separately.
