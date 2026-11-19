# Version History

## v0.6 — 2026-11-19
- BWC-CHG-006: Replaced LocationManager-only polling with Google Play Services FusedLocationProviderClient.
- Requests a high-accuracy current fix, accepting a recent cached fix up to 120 seconds old; if unavailable, falls back to a single fused location update for up to 30 seconds.
- Added in-screen location progress and failure messages.
- Added a settings footer showing the installed version dynamically from BuildConfig.VERSION_NAME.
- Added play-services-location 21.3.0 and explicitly enabled BuildConfig generation.
- Incremented app versionName to 0.6; versionCode remains 1 because no release has been issued.
- GitHub source and record fetch verification only; Android compilation and physical-device test remain pending.

## v0.5 — 2026-11-19
- BWC-CHG-005: Corrected provider selection so GPS is used when enabled even when Android grants approximate/coarse location permission. Android controls the precision returned.
- Replaced the API 30+ one-shot callback path with a single location-update listener, matching the legacy path and avoiding reliance on a null-prone one-shot result.
- Uses a recent cached fix (up to two minutes old) immediately when available; otherwise listens for a fix, then unregisters. A 30-second timeout reports failure.
- The listener is removed on success, timeout, provider disable or Activity destruction; no ongoing/background tracking.
- Toast now reports provider, coordinate accuracy and actionable errors.
- Incremented app versionName to 0.5; versionCode remains 1 because no release has been issued.
- GitHub source and record fetch verification only. Android compilation and physical-device GPS test remain pending.

## v0.4 — 2026-11-19
- BWC-CHG-004: Added a "Use current device location" button to widget configuration.
- Added foreground coarse/fine location permission declarations and runtime permission handling; permission is requested only after the user taps the button.
- On supported Android versions, retrieves a one-shot location fix using the enabled GPS or network provider and fills the latitude/longitude fields.
- Coordinates are not saved/applied until the user presses Save settings. Manual coordinate entry remains available if permission is denied or location is unavailable.
- Added a timeout for the legacy single-update path on Android 8–10.
- Incremented app versionName to 0.4; versionCode remains 1 because no release has been issued.
- GitHub source commit/fetch verification only; compilation and device testing remain pending.

## v0.3 — 2026-11-19
- BWC-CHG-003: Fixed the reported Java/Kotlin JVM target mismatch by explicitly setting both compilation targets to JVM 17.
- Incremented app versionName from 0.2 to 0.3.
- Preserved all other Android source files unchanged.
- Build retest pending; no successful build or APK claimed.

## v0.2 — 2026-11-19
- BWC-CHG-002: Added initial native Kotlin Android project.
- Implemented Android AppWidget layout with 48sp TextClock and date.
- Integrated Open-Meteo current conditions and next-day daily forecast.
- Added manual coordinate settings, colour presets, transparency and WorkManager refresh.
- Added Android manifest, widget metadata, resources, Gradle configuration and README.
- Committed on `feature/android-starter`; `main` remains unchanged.
- All 17 tracked project and record files fetched successfully from the feature branch; branch comparison confirms 18 commits ahead of main and no commits behind.
- Source integrity fetch verification passed; compilation and runtime remain unverified.
- Not compiled; no APK, screenshots, emulator test or physical-device test produced.

## v0.1 — 2026-11-19
- Created initial project specification, change register and manifest.
- Recorded fixed widget layout and behaviour requirements.
- No Android application code or build existed at this version.
