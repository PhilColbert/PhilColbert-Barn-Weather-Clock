package uk.co.barnweatherclock

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        val editor = context.getSharedPreferences("barn_weather", Context.MODE_PRIVATE).edit()
        appWidgetIds.forEach { id ->
            editor.remove("background_$id")
            editor.remove("colour_index_$id")
            editor.remove("transparency_$id")
        }
        editor.apply()
    }

    override fun onDisabled(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("barn-weather-refresh")
    }

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
                    prefs.getInt("background_$id", 0xFF202A36.toInt()))

                // The widget itself is the route back to this widget's settings.
                val settingsIntent = Intent(context, WidgetConfigActivity::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                val settingsPendingIntent = PendingIntent.getActivity(
                    context, id, settingsIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_root, settingsPendingIntent)

                if (snapshot != null) {
                    views.setTextViewText(R.id.current_icon, WeatherData.icon(snapshot.current.code))
                    views.setViewVisibility(R.id.current_now, android.view.View.VISIBLE)
                    views.setTextViewText(R.id.current_now, "Now")
                    views.setTextViewText(R.id.current_temp, snapshot.current.temperature.toString() + "°")
                    views.setTextViewText(R.id.current_daynight,
                        "Day " + snapshot.current.dayTemperature + "°\nNight " + snapshot.current.nightTemperature + "°")
                    views.setTextViewText(R.id.current_wind,
                        snapshot.current.windMph.toString() + " mph " + snapshot.current.windDirection)
                    views.setTextViewText(R.id.current_weather_date, WeatherData.displayDate(snapshot.current.date))
                    views.setTextViewText(R.id.forecast_icon, WeatherData.icon(snapshot.nextDay.code))
                    views.setTextViewText(R.id.forecast_temp,
                        "Day " + snapshot.nextDay.dayTemperature + "°\nNight " + snapshot.nextDay.nightTemperature + "°")
                    views.setTextViewText(R.id.forecast_wind,
                        snapshot.nextDay.windMph.toString() + " mph " + snapshot.nextDay.windDirection)
                    views.setTextViewText(R.id.forecast_weather_date, WeatherData.displayDate(snapshot.nextDay.date))
                } else {
                    views.setViewVisibility(R.id.current_now, android.view.View.GONE)
                    val configured = prefs.getString("latitude", null) != null
                    views.setTextViewText(R.id.current_temp, if (!configured) "Set location" else if (error) "Offline" else "Loading")
                    views.setTextViewText(R.id.current_daynight, "Day --°\nNight --°")
                    views.setTextViewText(R.id.current_wind, if (!configured) "Tap to configure" else "Weather unavailable")
                    views.setTextViewText(R.id.current_weather_date, "")
                    views.setTextViewText(R.id.forecast_temp, "Day --°\nNight --°")
                    views.setTextViewText(R.id.forecast_wind, "-- mph --")
                    views.setTextViewText(R.id.forecast_weather_date, "")
                }
                manager.updateAppWidget(id, views)
            }
        }
    }
}
