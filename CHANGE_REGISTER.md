# Change Register

Project: Barn Weather Clock (BWC-001)  
Baseline: v0.2

| Change ID | Status | Request / scope | Baseline | Files | Result |
|---|---|---|---|---|---|
| BWC-CHG-001 | FIXED / SPECIFICATION | Establish initial widget requirements and layout baseline; documentation only | Empty repository | SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md | Initial records |
| BWC-CHG-002 | IMPLEMENTED / NOT BUILD-VERIFIED | Create native Kotlin Android project with live Open-Meteo current/next-day weather, TextClock/date widget, manual coordinates, appearance configuration, periodic refresh, manifest, resources and build files | v0.1 docs-only baseline | Gradle files, app/**, README.md, project records | Files committed and fetched for verification; Android build/device tests not run |

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
