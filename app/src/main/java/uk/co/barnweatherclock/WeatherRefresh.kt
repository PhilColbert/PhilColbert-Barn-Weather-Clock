package uk.co.barnweatherclock

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WeatherRefresh {
    fun refresh(context: Context, ids: IntArray? = null) {
        val app = context.applicationContext
        Thread {
            val prefs = app.getSharedPreferences("barn_weather", Context.MODE_PRIVATE)
            val lat = prefs.getString("latitude", null)?.toDoubleOrNull()
            val lon = prefs.getString("longitude", null)?.toDoubleOrNull()
            val manager = AppWidgetManager.getInstance(app)
            val widgetIds = ids ?: manager.getAppWidgetIds(ComponentName(app, BarnWeatherWidgetProvider::class.java))
            if (lat == null || lon == null) {
                BarnWeatherWidgetProvider.updateWidgets(app, manager, widgetIds, null)
                return@Thread
            }
            try {
                val data = WeatherData.fetch(lat, lon)
                BarnWeatherWidgetProvider.updateWidgets(app, manager, widgetIds, data)
            } catch (_: Exception) {
                BarnWeatherWidgetProvider.updateWidgets(app, manager, widgetIds, null, true)
            }
        }.start()
    }
}

class WeatherWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val prefs = applicationContext.getSharedPreferences("barn_weather", Context.MODE_PRIVATE)
            val lat = prefs.getString("latitude", null)?.toDoubleOrNull()
            val lon = prefs.getString("longitude", null)?.toDoubleOrNull()
            if (lat != null && lon != null) {
                val weather = WeatherData.fetch(lat, lon)
                BarnWeatherWidgetProvider.updateAll(applicationContext, weather)
            } else {
                BarnWeatherWidgetProvider.updateAll(applicationContext, null)
            }
            Result.success()
        } catch (_: Exception) { Result.retry() }
    }
}
