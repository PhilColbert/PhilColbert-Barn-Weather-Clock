# Change Register

Project: Barn Weather Clock (BWC-001)  
Baseline: v0.9

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

| BWC-CHG-010 | IMPLEMENTED / CI BUILD VERIFIED | Add a reproducible GitHub Actions Android build to compile v0.8 and publish the debug APK as a downloadable workflow artifact | v0.8 source / no Gradle wrapper or local Android SDK available | .github/workflows/android-build.yml, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md, README.md | Run 44540883299 completed successfully; debug APK artifact 14537997709 uploaded and downloaded for verification |

| BWC-CHG-011 | IMPLEMENTED / CI VERIFIED | Correct GitHub Actions Android SDK setup after runner failure caused by removed SDK package `tools` | v0.8 / BWC-CHG-010 first CI run | .github/workflows/android-build.yml, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md | Replaced obsolete default SDK package list with `platform-tools`; run 44540883299 completed successfully through APK upload |


## BWC-CHG-012 — approved and implemented scope
- **Request:** Remove the words “Day” and “Night” from both current-weather and next-day forecast temperature pairs; retain the two values on separate lines. Display a small “Now” label immediately to the left of the larger live current temperature on the same line.
- **Baseline:** v0.8 at commit 86975d05eba185e6735df02a09e4a3bf2b77abd7.
- **Scope:** WeatherData temperature-pair formatter, widget provider text/visibility, widget XML current-temperature row and placeholders, app version metadata, CI artifact label, and project records. No weather API fields, temperature calculations, wind, icons, dates, location, refresh, or preferences changed.
- **Version:** v0.9 / versionCode 5.
- **Files changed:** WeatherData.kt, widget_barn_weather.xml, app/build.gradle.kts, SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md, README.md.
- **Validation:** Source-level changes and GitHub fetch verification completed; Android CI build, APK inspection, and device rendering are pending. All other project files are carried forward unchanged from v0.8 and are not claimed rebuilt or device-tested.

- BWC-CHG-012 refinement: the live current temperature is displayed on the same line as a smaller “Now” label. The label is hidden while weather is unavailable. High/low values remain unlabeled on separate lines.
- CI workflow artifact label updated to `barn-weather-clock-v0.9-debug` to match the package version.


## BWC-CHG-013 — approved scope and implementation record
- **Request:** Recreate the approved visual reference as the actual native Android widget.
- **Baseline:** v0.9 at commit 496bbe4b7282fa4ae2b981c3adece4bec24a66ac.
- **Scope:** Three balanced columns; rounded dark panel with blue-purple edge and vertical dividers; larger clock; current and forecast dates at the top of their columns; weather icons beside temperature details; each high/low pair on one line separated by `/`; wind glyph and direction-first wind text plus current and forecast gust rows; retain live current temperature with the small Now label; preserve weather provider, location, refresh, configuration and background colour/transparency preferences.
- **Version:** v1.0 / versionCode 6.
- **Files changed:** widget_barn_weather.xml, widget_frame.xml (new), barn_weather_widget_info.xml, BarnWeatherWidgetProvider.kt, WeatherData.kt, app/build.gradle.kts, android-build.yml, SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md, README.md.
- **Validation:** Source committed on feature/android-starter; Open-Meteo wind-gust fields are documented by the provider; CI build and visual/device checks pending. Other project files are carried forward unchanged and are not claimed rebuilt or tested.


## BWC-CHG-014 — approved scope and implementation record
- **Request:** Correct the native widget to match the previously approved screenshot exactly; make tapping the widget reopen its settings; provide clear, supported widget-removal guidance.
- **Baseline:** v1.0 at feature branch HEAD immediately before this change (widget layout SHA 2d146451f0a208738e955dfac592d27bd1dc5d65; provider SHA ebd1fe6982767bbdee1d2ad25519b6543124d5e5; settings Activity SHA f9f7775a0a223c3992bdd476d3369eb7bce97c92).
- **Scope:** Rebuild the widget XML around the approved screenshot: rounded dark slate panel, three columns with clock/date left, current conditions centre and next-day forecast right; weather icons left of text; Now/live temperature, separate Day/Night values, wind row and date at the bottom of each weather column. Remove unapproved top weather dates and gust rows. Set a widget-wide tap action to reopen the existing per-widget settings. Add explicit launcher removal instructions in settings. Keep the system launcher long-press/remove action as the actual deletion mechanism because Android AppWidget hosts own widget deletion; an ordinary app cannot safely delete a host-owned widget ID.
- **Version:** v1.1 / versionCode 7.
- **Files planned:** widget_barn_weather.xml, BarnWeatherWidgetProvider.kt, WidgetConfigActivity.kt, app/build.gradle.kts, CHANGE_REGISTER.md, SPECIFICATION.md, MANIFEST.md, VERSION_HISTORY.md, README.md.
- **Validation:** Pending source review, GitHub Actions compile, and device screenshot comparison. The approved reference is barn_weather_clock_widget_preview.png; its content was inspected before this change. No physical-device rendering will be claimed without a device capture.
