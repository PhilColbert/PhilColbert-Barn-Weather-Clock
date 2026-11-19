# Change Register

Project: Barn Weather Clock (BWC-001)  
Baseline: v0.4

| Change ID | Status | Request / scope | Baseline | Files | Result |
|---|---|---|---|---|---|
| BWC-CHG-001 | FIXED / SPECIFICATION | Establish initial widget requirements and layout baseline; documentation only | Empty repository | SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md | Initial records |
| BWC-CHG-002 | IMPLEMENTED / NOT BUILD-VERIFIED | Create native Kotlin Android project with live Open-Meteo current/next-day weather, TextClock/date widget, manual coordinates, appearance configuration, periodic refresh, manifest, resources and build files | v0.1 docs-only baseline | Gradle files, app/**, README.md, project records | Files committed and fetched for verification; Android build/device tests not run |

| BWC-CHG-003 | IMPLEMENTED / USER BUILD VERIFIED | Fix reported Kotlin/Java JVM-target mismatch by explicitly aligning Android Java and Kotlin compilation to JVM 17; bump app version to 0.3 | v0.2 / user build log | app/build.gradle.kts, project records | User reports the project now works; build/runtime details not independently inspected |
| BWC-CHG-004 | IMPLEMENTED / BUILD & DEVICE TEST PENDING | Add a configuration-screen action to obtain the device's current location and populate latitude/longitude, with runtime permission handling and a manual-entry fallback; bump app version to 0.4 | v0.3 (user-confirmed working) | AndroidManifest.xml, WidgetConfigActivity.kt, app/build.gradle.kts, project records | Source committed and fetched for verification; no local Android build or device test performed |

## Scope controls
- Changes require a Change ID, request, baseline and scope before edits.
- Preserve existing requirements unless the user explicitly changes them.
- Distinguish inspected, automated-tested, compiled, emulator-tested and physical-device-tested.
- Do not describe proposed or mock data as live data.


## BWC-CHG-002 implementation notes
- Added Kotlin AppWidget provider, configuration Activity, Open-Meteo client and WorkManager refresh worker.
- Added compact horizontal widget layout with 48sp clock, current weather and next-day forecast.
- Added manual coordinate entry with signed decimal input, background presets, transparency slider and Open-Meteo attribution in configuration.
- No GPS permission; no fake weather values are supplied.
- Verified all tracked files can be fetched from the branch; source-level review only, not a compiler check.
- Not yet compiled, installed, emulator-tested or physical-device-tested.


## BWC-CHG-003 implementation notes
- Set Java sourceCompatibility and targetCompatibility to VERSION_17.
- Set Kotlin jvmTarget to 17.
- Incremented app versionName to 0.3; versionCode remains 1 because no release has been issued.
- Preserved all other project source files byte-for-byte.
- The uploaded build log is the baseline failure evidence; no local Android build environment is available here to rerun the Windows build.

## BWC-CHG-004 implementation notes
- Added ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION manifest permissions. No background location permission or continuous tracking.
- Added a one-shot current-device location button in widget configuration; permission is requested only after the button is tapped.
- Supports precise GPS when available and network location when only coarse permission/provider is available.
- Populates coordinate fields; user must still press Save settings to persist and refresh the widget. Manual entry remains available.
- Added a 20-second timeout to the legacy Android 8–10 single-update path.
- Source files committed and fetched successfully from the feature branch; compile/runtime testing remains pending.
