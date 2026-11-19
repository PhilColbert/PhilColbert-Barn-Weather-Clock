# Barn Weather Clock

Native Android home-screen widget built with Kotlin and Android AppWidget APIs.

## v1.1
- Three-column widget matching the approved reference: clock/date on the left, current weather in the centre and next-day forecast on the right.
- Dark slate rounded panel, restrained outline and vertical dividers.
- Weather icons sit to the left of the text. Current temperature has a small Now label. Day and Night temperatures are on separate lines. Wind speed and direction are below, with the date at the bottom of each weather column.
- Tapping the widget opens its settings.
- Settings include manual coordinates, optional one-shot device location, background colour and transparency, Open-Meteo attribution, app version and a Remove widget help action.
- To remove the widget, return to the home screen, press and hold the widget, then choose Remove (or drag it to Remove). Android's launcher performs the deletion.
- Live Open-Meteo weather, 30-minute WorkManager refresh, Celsius and mph.

## Build
1. Open the repository in Android Studio.
2. Allow Gradle sync and install Android SDK Platform 35 if prompted. The project uses Java/Kotlin JVM 17 and Gradle 8.9.
3. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
4. Add Barn Weather Clock from the launcher's widget picker.
5. Enter latitude/longitude or use the current-device-location button.

## Weather source and privacy
Weather is retrieved live from Open-Meteo. Internet access is required. Foreground location permission is requested only when the user taps the location button. The app does not continuously track location or hard-code a private address.

## Verification
The v1.1 CI build and physical-device screenshot comparison are pending. Source-level inspection is not a substitute for compilation or device testing.
