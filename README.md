# Barn Weather Clock

Native Android home-screen widget built with Kotlin and Android AppWidget APIs.

## Included in v0.2
- Large Android TextClock and locale-aware date.
- Live current outdoor conditions and next-calendar-day forecast from Open-Meteo.
- Temperature, condition symbol, wind speed in mph and compass direction.
- Manual latitude/longitude configuration.
- Background colour presets and adjustable transparency.
- Periodic weather refresh through WorkManager (30-minute interval).
- Resizable widget and dedicated configuration Activity.

## Build
1. Open this repository in Android Studio.
2. Allow Gradle sync and install Android SDK Platform 35 if prompted.
3. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
4. Add **Barn Weather Clock** from the launcher's widget picker.
5. Enter latitude and longitude during widget configuration.

Example coordinates near Bolton: latitude 53.58, longitude -2.43. Replace these with your chosen location.

## Weather source
Weather is retrieved live from the Open-Meteo forecast API. Internet access is required. The app does not request device location permission or hard-code a private address.

## Limitations
- Location is entered as coordinates; place-name geocoding and GPS are not implemented.
- Android may defer background refresh to preserve battery.
- The clock uses Android TextClock and updates independently of weather refresh.
- Launcher support for opening configuration from a widget long-press menu varies. The configure Activity is registered in AppWidget metadata.
- No APK is committed; build locally in Android Studio.
