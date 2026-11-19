package uk.co.barnweatherclock

import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.*

class WidgetConfigActivity : Activity() {
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var location: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var background: Spinner
    private lateinit var transparency: SeekBar
    private var pendingListener: LocationListener? = null
    private var activeLocationManager: LocationManager? = null
    private val locationHandler = Handler(Looper.getMainLooper())
    private val locationTimeout = Runnable {
        pendingListener?.let { listener -> activeLocationManager?.removeUpdates(listener) }
        pendingListener = null
        activeLocationManager = null
        Toast.makeText(this, "No location fix received within 30 seconds. Check Location is enabled, then try again or enter coordinates manually.", Toast.LENGTH_LONG).show()
    }
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
        root.addView(Button(this).apply {
            text = "Use current device location"
            setOnClickListener { obtainCurrentLocation() }
        })
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

    private fun obtainCurrentLocation() {
        val fineGranted = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!fineGranted && !coarseGranted) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST)
            return
        }

        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = try {
            // Approximate-location permission can still use the GPS provider; Android
            // simply limits the precision of the returned coordinates.
            when {
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                else -> null
            }
        } catch (_: Exception) { null }

        if (provider == null) {
            Toast.makeText(this, "No location provider is enabled. Turn on Location services and try again.", Toast.LENGTH_LONG).show()
            return
        }

        try {
            // Use a recent cached fix immediately when available.
            val cached = manager.getLastKnownLocation(provider)
            if (cached != null && System.currentTimeMillis() - cached.time in 0..120000L) {
                populateCoordinates(cached)
                return
            }

            Toast.makeText(this, "Waiting for a location fix from $provider…", Toast.LENGTH_SHORT).show()
            val listener = object : LocationListener {
                override fun onLocationChanged(result: Location) {
                    locationHandler.removeCallbacks(locationTimeout)
                    pendingListener = null
                    activeLocationManager?.removeUpdates(this)
                    activeLocationManager = null
                    populateCoordinates(result)
                }
                @Deprecated("Deprecated by Android")
                override fun onStatusChanged(providerName: String?, status: Int, extras: Bundle?) = Unit
                override fun onProviderEnabled(providerName: String) = Unit
                override fun onProviderDisabled(providerName: String) {
                    if (providerName == provider) {
                        locationHandler.removeCallbacks(locationTimeout)
                        pendingListener = null
                        activeLocationManager?.removeUpdates(this)
                        activeLocationManager = null
                        Toast.makeText(this@WidgetConfigActivity, "Location provider was disabled. Try again.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            pendingListener = listener
            activeLocationManager = manager
            manager.requestLocationUpdates(provider, 1000L, 0f, listener, Looper.getMainLooper())
            locationHandler.postDelayed(locationTimeout, 30000L)
        } catch (_: SecurityException) {
            pendingListener = null
            activeLocationManager = null
            Toast.makeText(this, "Location permission is required. Allow approximate or precise location and try again.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            pendingListener = null
            activeLocationManager = null
            Toast.makeText(this, "Could not start location: ${e.javaClass.simpleName}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) obtainCurrentLocation()
            else Toast.makeText(this, "Location permission was not granted. You can still enter coordinates manually.", Toast.LENGTH_LONG).show()
        }
    }

    private fun populateCoordinates(result: Location) {
        latitude.setText(String.format(java.util.Locale.US, "%.6f", result.latitude))
        longitude.setText(String.format(java.util.Locale.US, "%.6f", result.longitude))
        location.setText("Current device location")
        Toast.makeText(this, "Location added (±${result.accuracy.toInt()} m). Press Save settings to apply it.", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        locationHandler.removeCallbacks(locationTimeout)
        pendingListener?.let { activeLocationManager?.removeUpdates(it) }
        pendingListener = null
        activeLocationManager = null
        super.onDestroy()
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 4107
    }
}
