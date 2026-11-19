# Change Register

Project: Barn Weather Clock (BWC-001)  
Baseline: v0.1

| Change ID | Status | Request / scope | Baseline | Files | Result |
|---|---|---|---|---|---|
| BWC-CHG-001 | FIXED / SPECIFICATION | Establish initial widget requirements and layout baseline; documentation only | Empty repository | SPECIFICATION.md, CHANGE_REGISTER.md, MANIFEST.md, VERSION_HISTORY.md | Initial records |
| BWC-CHG-002 | IN PROGRESS | Create native Kotlin Android project with live Open-Meteo current/next-day weather, clock/date widget, manual coordinates, appearance configuration, refresh worker, manifest, resources and build files | v0.1 docs-only baseline | app/**, Gradle files, README.md, project records | Implementation underway; no build/device tests claimed |

## Scope controls
- Changes require a Change ID, request, baseline and scope before edits.
- Preserve existing requirements unless the user explicitly changes them.
- Distinguish inspected, automated-tested, compiled, emulator-tested and physical-device-tested.
- Do not describe proposed or mock data as live data.
