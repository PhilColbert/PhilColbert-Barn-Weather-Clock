package uk.co.barnweatherclock

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class BarnWeatherWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        scheduleRefresh(context)
        WeatherRefresh.refresh(context, ids)
    }
    override fun onEnabled(context: Context) { scheduleRefresh(context) }

    companion object {
        fun scheduleRefresh(context: Context) {
            val request = PeriodicWorkRequestBuilder<WeatherWorker>(30, TimeUnit.MINUTES).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "barn-weather-refresh", ExistingPeriodicWorkPolicy.UPDATE, request
            )
        }

        fun updateAll(context: Context, snapshot: WeatherSnapshot?, error: Boolean = false) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, BarnWeatherWidgetProvider::class.java))
            updateWidgets(context, manager, ids, snapshot, error)
        }

        fun updateWidgets(context: Context, manager: AppWidgetManager, ids: IntArray,
                          snapshot: WeatherSnapshot?, error: Boolean = false) {
            val prefs = context.getSharedPreferences("barn_weather", Context.MODE_PRIVATE)
            ids.forEach { id ->
                val views = RemoteViews(context.packageName, R.layout.widget_barn_weather)
                views.setInt(R.id.widget_panel, "setBackgroundColor",
                    prefs.getInt("background_$id", 0xCC202833.toInt()))
                if (snapshot != null) {
                    views.setTextViewText(R.id.current_icon, WeatherData.icon(snapshot.current.code))
                    views.setViewVisibility(R.id.current_now, android.view.View.VISIBLE)
                    views.setTextViewText(R.id.current_temp, snapshot.current.temperature.toString() + "°")
                    views.setTextViewText(R.id.current_daynight,
                        WeatherData.temperaturePairLabel(snapshot.current.dayTemperature, snapshot.current.nightTemperature))
                    views.setTextViewText(R.id.current_wind,
                        "≋ " + snapshot.current.windDirection + " " + snapshot.current.windMph + " mph")
                    views.setTextViewText(R.id.current_gust, "⚑ " + snapshot.current.gustMph + " mph")
                    views.setTextViewText(R.id.forecast_icon, WeatherData.icon(snapshot.nextDay.code))
                    views.setTextViewText(R.id.forecast_temp,
                        WeatherData.temperaturePairLabel(snapshot.nextDay.dayTemperature, snapshot.nextDay.nightTemperature))
                    views.setTextViewText(R.id.forecast_wind,
                        "≋ " + snapshot.nextDay.windDirection + " " + snapshot.nextDay.windMph + " mph")
                    views.setTextViewText(R.id.forecast_gust, "⚑ " + snapshot.nextDay.gustMph + " mph")
                    views.setTextViewText(R.id.forecast_date, WeatherData.displayDate(snapshot.nextDay.date))
                } else {
                    views.setViewVisibility(R.id.current_now, android.view.View.GONE)
                    val configured = prefs.getString("latitude", null) != null
                    views.setTextViewText(R.id.current_temp, if (!configured) "Set location" else if (error) "Offline" else "Loading")
                    views.setTextViewText(R.id.current_daynight, "--")
                    views.setTextViewText(R.id.current_wind, if (!configured) "Configure widget" else "Weather unavailable")
                    views.setTextViewText(R.id.current_gust, "-- mph")
                    views.setTextViewText(R.id.forecast_temp, "--° / --°")
                    views.setTextViewText(R.id.forecast_wind, "-- mph --")
                    views.setTextViewText(R.id.forecast_gust, "-- mph")
                }
                manager.updateAppWidget(id, views)
            }
        }
    }
}
