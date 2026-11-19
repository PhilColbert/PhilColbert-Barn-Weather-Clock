# Barn Weather Clock

Native Android home-screen widget built with Kotlin and Android AppWidget APIs.

## Included in v0.9
- 32sp Android TextClock and locale-aware date.
- Live current outdoor conditions and next-calendar-day forecast from Open-Meteo.
- Small Now label beside the larger live current temperature on one line, followed by today's maximum and minimum values on separate unlabeled lines; next-day maximum and minimum shown the same way. Includes condition symbols, wind speed in mph and compass direction. The current-weather date shows day/month without weekday.
- Manual latitude/longitude configuration, plus an optional one-shot current-device location button in settings.
- Runtime foreground location permission is requested only when the location button is tapped; coordinates are filled into the fields and applied when Save settings is pressed. Uses Google Fused Location Provider, combining available location sources. It checks for a recent cached fix, then requests a current fix with a bounded update fallback. A status line explains progress or failure. The settings screen displays the installed app version at the bottom.
- Background colour presets and adjustable transparency.
- Periodic weather refresh through WorkManager (30-minute interval).
- Resizable widget and dedicated configuration Activity.

## Build
1. Open this repository in Android Studio.
2. Allow Gradle sync and install Android SDK Platform 35 if prompted. The project uses Android Gradle Plugin 8.7.3 (Gradle 8.9); if Android Studio asks for a Gradle installation, select Gradle 8.9.
3. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
4. Add **Barn Weather Clock** from the launcher's widget picker.
5. Enter latitude and longitude during widget configuration, or tap **Use current device location** and grant location permission when Android asks.

Example coordinates near Bolton: latitude 53.58, longitude -2.43. Replace these with your chosen location.

## Weather source
Weather is retrieved live from the Open-Meteo forecast API. Internet access is required. The app requests foreground location permission only when you explicitly tap **Use current device location**. It does not continuously track your position or hard-code a private address.

## Limitations
- Reverse geocoding into a place name is not implemented. If location permission is denied or no fix is available, enter coordinates manually.
- Android may defer background refresh to preserve battery.
- The clock uses Android TextClock and updates independently of weather refresh.
- Launcher support for opening configuration from a widget long-press menu varies. The configure Activity is registered in AppWidget metadata.
- The settings screen displays the installed version at the bottom. A GitHub Actions workflow builds the debug APK on pushes to `feature/android-starter` and publishes it as a 14-day workflow artifact. v0.8 built successfully in run [44540883299](https://github.com/PhilColbert/PhilColbert-Barn-Weather-Clock/actions/runs/44540883299). Current source version: v0.9; its CI build is pending. Physical-device installation and visual testing are still required.
