package uk.co.barnweatherclock

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*

class WidgetConfigActivity : Activity() {
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var location: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var background: Spinner
    private lateinit var transparency: SeekBar
    private val colours = intArrayOf(Color.rgb(32, 40, 51), Color.rgb(20, 55, 85),
        Color.rgb(45, 45, 45), Color.BLACK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID
        val prefs = getSharedPreferences("barn_weather", Context.MODE_PRIVATE)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }
        fun label(value: String) {
            root.addView(TextView(this).apply { text = value; textSize = 16f; setPadding(0, 12, 0, 4) })
        }
        label("Weather location")
        location = EditText(this).apply {
            hint = "Place name (for reference)"
            setText(prefs.getString("location_name", ""))
        }
        root.addView(location, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        label("Latitude")
        latitude = EditText(this).apply {
            hint = "e.g. 53.58"; inputType = 12290
            setText(prefs.getString("latitude", ""))
        }
        root.addView(latitude)
        label("Longitude")
        longitude = EditText(this).apply {
            hint = "e.g. -2.43"; inputType = 12290
            setText(prefs.getString("longitude", ""))
        }
        root.addView(longitude)
        label("Background colour")
        background = Spinner(this)
        background.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            listOf("Slate", "Blue", "Charcoal", "Black"))
        background.setSelection(prefs.getInt("colour_index_$widgetId", 0).coerceIn(0, 3))
        root.addView(background)
        label("Transparency")
        transparency = SeekBar(this).apply {
            max = 100
            progress = prefs.getInt("transparency_$widgetId", 20)
        }
        root.addView(transparency)
        root.addView(Button(this).apply {
            text = "Save settings"
            setOnClickListener { saveSettings() }
        })
        root.addView(TextView(this).apply {
            text = "Weather data provided by Open-Meteo"
            textSize = 12f
            setPadding(0, 12, 0, 0)
        })
        setContentView(ScrollView(this).apply { addView(root) })
    }

    private fun saveSettings() {
        val lat = latitude.text.toString().trim().toDoubleOrNull()
        val lon = longitude.text.toString().trim().toDoubleOrNull()
        if (lat == null || lon == null || lat !in -90.0..90.0 || lon !in -180.0..180.0) {
            Toast.makeText(this, "Enter valid latitude and longitude", Toast.LENGTH_LONG).show()
            return
        }
        val alpha = (100 - transparency.progress) * 255 / 100
        val base = colours[background.selectedItemPosition]
        val colour = Color.argb(alpha, Color.red(base), Color.green(base), Color.blue(base))
        getSharedPreferences("barn_weather", Context.MODE_PRIVATE).edit()
            .putString("location_name", location.text.toString().trim())
            .putString("latitude", lat.toString())
            .putString("longitude", lon.toString())
            .putInt("colour_index_$widgetId", background.selectedItemPosition)
            .putInt("transparency_$widgetId", transparency.progress)
            .putInt("background_$widgetId", colour)
            .apply()
        val result = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        setResult(RESULT_OK, result)
        BarnWeatherWidgetProvider.updateWidgets(this, AppWidgetManager.getInstance(this),
            intArrayOf(widgetId), null)
        WeatherRefresh.refresh(this, intArrayOf(widgetId))
        finish()
    }
}
