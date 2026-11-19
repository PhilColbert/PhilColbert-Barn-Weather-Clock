package uk.co.barnweatherclock

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

data class DayWeather(val date: String, val temperature: Int, val code: Int, val windMph: Int, val windDirection: String)
data class WeatherSnapshot(val current: DayWeather, val nextDay: DayWeather)

object WeatherData {
    fun fetch(latitude: Double, longitude: Double): WeatherSnapshot {
        val endpoint = "https://api.open-meteo.com/v1/forecast" +
            "?latitude=$latitude&longitude=$longitude" +
            "&current=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m" +
            "&daily=weather_code,temperature_2m_max,wind_speed_10m_max,wind_direction_10m_dominant" +
            "&timezone=auto&forecast_days=2&wind_speed_unit=mph"
        val connection = URL(endpoint).openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        try {
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(body)
            val current = json.getJSONObject("current")
            val daily = json.getJSONObject("daily")
            val currentWeather = DayWeather(
                current.optString("time").take(10),
                current.getDouble("temperature_2m").toInt(),
                current.getInt("weather_code"),
                current.getDouble("wind_speed_10m").toInt(),
                compass(current.getDouble("wind_direction_10m"))
            )
            val nextWeather = DayWeather(
                daily.getJSONArray("time").getString(1),
                daily.getJSONArray("temperature_2m_max").getDouble(1).toInt(),
                daily.getJSONArray("weather_code").getInt(1),
                daily.getJSONArray("wind_speed_10m_max").getDouble(1).toInt(),
                compass(daily.getJSONArray("wind_direction_10m_dominant").getDouble(1))
            )
            return WeatherSnapshot(currentWeather, nextWeather)
        } finally { connection.disconnect() }
    }

    fun icon(code: Int): String = when (code) {
        0 -> "☀"; 1, 2 -> "🌤"; 3 -> "☁"; 45, 48 -> "🌫"
        51, 53, 55, 56, 57 -> "🌦"
        61, 63, 65, 66, 67, 80, 81, 82 -> "🌧"
        71, 73, 75, 77, 85, 86 -> "❄"
        95, 96, 99 -> "⛈"; else -> "·"
    }

    private fun compass(degrees: Double): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        return directions[Math.round(degrees / 45.0).toInt() % 8]
    }

    fun displayDate(isoDate: String): String = try {
        val parts = isoDate.split("-")
        val month = java.text.DateFormatSymbols(Locale.getDefault()).shortMonths[parts[1].toInt() - 1]
        parts[2].toInt().toString() + " " + month
    } catch (_: Exception) { isoDate }
}
