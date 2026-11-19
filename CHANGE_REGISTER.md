# Change Register

Project: Barn Weather Clock (BWC-001)  
Baseline: v0.8

| Change ID | Status | Request / scope | Baseline | Files | Result |
|---|---|---|---|---|---|
| BWC-CHG-001 | FIXED / SPECIFICATION | Establish initial widget requirements and layout baseline; documentation only | Empty repository | SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md | Initial records |
| BWC-CHG-002 | IMPLEMENTED / NOT BUILD-VERIFIED | Create native Kotlin Android project with live Open-Meteo current/next-day weather, TextClock/date widget, manual coordinates, appearance configuration, periodic refresh, manifest, resources and build files | v0.1 docs-only baseline | Gradle files, app/**, README.md, project records | Files committed and fetched for verification; Android build/device tests not run |

| BWC-CHG-003 | IMPLEMENTED / USER BUILD VERIFIED | Fix reported Kotlin/Java JVM-target mismatch by explicitly aligning Android Java and Kotlin compilation to JVM 17; bump app version to 0.3 | v0.2 / user build log | app/build.gradle.kts, project records | User reports the project now works; build/runtime details not independently inspected |
| BWC-CHG-004 | IMPLEMENTED / USER DEVICE ISSUE REPORTED | Add a configuration-screen action to obtain the device's current location and populate latitude/longitude, with runtime permission handling and a manual-entry fallback; bump app version to 0.4 | v0.3 (user-confirmed working) | AndroidManifest.xml, WidgetConfigActivity.kt, app/build.gradle.kts, project records | Source committed and fetched for verification; no local Android build or device test performed |
| BWC-CHG-005 | IMPLEMENTED / USER DEVICE ISSUE REPORTED | Correct current-location retrieval: allow GPS provider with approximate permission, use a recent cached fix or a single location update, add a timeout and improve diagnostics; bump app version to 0.5 | v0.4 / user report: device shows GPS lock but coordinate fields remain blank | WidgetConfigActivity.kt, app/build.gradle.kts, project records | Updated source committed and fetched for verification; no local Android build or device test performed |
| BWC-CHG-006 | IMPLEMENTED / BUILD & DEVICE TEST PENDING | Replace LocationManager-only acquisition with Google fused location (cached/current request plus bounded update fallback); show app version dynamically in widget configuration; bump app version to 0.6 | v0.5 / user reports location remains waiting for fix | WidgetConfigActivity.kt, app/build.gradle.kts, project records | Fused location implementation, dependency and version label committed and fetched for verification; Android build/device tests pending |

## Scope controls
- Changes require a Change ID, request, baseline and scope before edits.
- Preserve existing requirements unless the user explicitly changes them.
- Distinguish inspected, automated-tested, compiled, emulator-tested and physical-device-tested.
- Do not describe proposed or mock data as live data.


## BWC-CHG-007 implementation notes
- Reduced the widget TextClock from 48sp to 32sp following user feedback that the time was too large.
- Incremented versionName to 0.7 and versionCode to 3 so Android recognises this update over v0.6 (versionCode 2).
- Corrected the v0.6 record: its actual Gradle versionCode was 2, not 1.
- Updated specification, manifest, version history and README.
- Source/record fetch verification pending; no Android compilation, emulator or device test performed.

## BWC-CHG-008 / BWC-CHG-009 implementation notes
- Open-Meteo daily data now requests temperature_2m_max and temperature_2m_min for the current date and following date.
- Current-weather column retains the live instantaneous temperature and adds today's Day maximum and Night minimum.
- Forecast column displays the next day's Day maximum and Night minimum.
- Day and night values are displayed on separate lines; current date is d MMM without weekday. The large clock's date remains unchanged.
- Incremented versionName from 0.7 to 0.8 and versionCode from 3 to 4 for Android update recognition.
- Source was committed in incremental file updates; final fetch verification and build checks pending. No APK or device test is claimed.

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
- Incremented app versionName to 0.3; versionCode incremented to 2 so Android recognises this as an update over the previously installed v0.5 (versionCode 1).
- Preserved all other project source files byte-for-byte.
- The uploaded build log is the baseline failure evidence; no local Android build environment is available here to rerun the Windows build.

## BWC-CHG-005 implementation notes
- The v0.4 implementation excluded GPS_PROVIDER whenever fine permission was not granted, even though Android can serve approximate/coarse results from the GPS provider. This could leave no eligible provider on devices without an enabled network provider.
- The v0.4 API 30+ path relied only on getCurrentLocation callback; a null callback result ended the attempt without requesting an update.
- v0.5 selects GPS whenever enabled, irrespective of fine-vs-coarse grant, and falls back to network provider when GPS is unavailable.
- A recent cached fix (up to two minutes) is applied immediately; otherwise a single location update is requested and the listener is removed after success, timeout, provider disable or Activity destruction.
- Added visible provider/accuracy/error feedback. No background permission or ongoing tracking.
- Source committed and fetched successfully; compile and physical-device test remain pending.

## BWC-CHG-004 implementation notes
- Added ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION manifest permissions. No background location permission or continuous tracking.
- Added a one-shot current-device location button in widget configuration; permission is requested only after the button is tapped.
- Supports precise GPS when available and network location when only coarse permission/provider is available.
- Populates coordinate fields; user must still press Save settings to persist and refresh the widget. Manual entry remains available.
- Added a 20-second timeout to the legacy Android 8–10 single-update path.
- Source files committed and fetched successfully from the feature branch; compile/runtime testing remains pending.

## BWC-CHG-006 implementation notes
- Added Google Play Services Location dependency 21.3.0 and enabled generated BuildConfig metadata.
- Replaced direct LocationManager polling with FusedLocationProviderClient using high-accuracy current location with a 120-second maximum cached age and 20-second request duration.
- If the current-location request returns null or fails, requests a single fused location update for up to 30 seconds.
- Displays location progress and error status in the settings screen; coordinates populate the existing fields and still require Save settings.
- Displays the installed version dynamically as "Barn Weather Clock v" plus BuildConfig.VERSION_NAME at the bottom of settings.
- Incremented versionName to 0.6; versionCode incremented to 2 so Android recognises the update over the previous install.
- Source and records fetched from GitHub for verification; compilation and physical-device location testing are not yet performed.

| BWC-CHG-007 | IMPLEMENTED / BUILD VERIFICATION PENDING | Reduce the widget's oversized clock from 48sp to 32sp; align the specification and correct the v0.6 versionCode record | v0.6 | app/src/main/res/layout/widget_barn_weather.xml, app/build.gradle.kts, SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md, README.md | Change logged before implementation; source/build/device checks pending |

| BWC-CHG-008 | IMPLEMENTED / BUILD VERIFICATION PENDING | Add daily maximum and minimum temperatures for today and the next-day forecast, while retaining the live current temperature and existing weather details | v0.7 | WeatherData.kt, BarnWeatherWidgetProvider.kt, widget_barn_weather.xml, SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md, README.md | Open-Meteo daily high/low parsing and widget display implemented; source verification pending; not compiled/device-tested |

| BWC-CHG-009 | IMPLEMENTED / BUILD VERIFICATION PENDING | Remove weekday from the current-weather date, showing only day/month; package the combined changes as v0.8 | v0.7 / partial BWC-CHG-008 source | widget_barn_weather.xml, app/build.gradle.kts, SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md, README.md | Current-weather date format set to d MMM; app versionName 0.8/versionCode 4; source verification pending; not compiled/device-tested |

| BWC-CHG-010 | IMPLEMENTED / CI RUN IN PROGRESS | Add a reproducible GitHub Actions Android build to compile v0.8 and publish the debug APK as a downloadable workflow artifact | v0.8 source / no Gradle wrapper or local Android SDK available | .github/workflows/android-build.yml, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md, README.md | Workflow committed; GitHub Actions run 44540743045 started successfully; build and APK artifact pending |
