package uk.co.barnweatherclock

import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class WidgetConfigActivity : Activity() {
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var location: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var background: Spinner
    private lateinit var transparency: SeekBar
    private lateinit var locationStatus: TextView
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private var cancellationTokenSource: CancellationTokenSource? = null
    private var locationCallback: LocationCallback? = null
    private val locationHandler = Handler(Looper.getMainLooper())
    private val locationTimeout = Runnable {
        stopLocationRequest()
        locationStatus.text = "Location: no fix received within 30 seconds. Try outdoors, check Google Play services and Location settings, or enter coordinates manually."
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
        locationStatus = TextView(this).apply {
            text = "Location: manual entry available"
            textSize = 12f
            setPadding(0, 6, 0, 4)
        }
        root.addView(locationStatus)
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
        root.addView(Button(this).apply {
            text = "Remove widget"
            setOnClickListener {
                android.app.AlertDialog.Builder(this@WidgetConfigActivity)
                    .setTitle("Remove Barn Weather Clock")
                    .setMessage("Android does not let an app delete a widget from its home-screen host. To remove it, return to your home screen, press and hold the Barn Weather Clock widget, then choose Remove (or drag it to Remove).")
                    .setPositiveButton("Got it", null)
                    .show()
            }
        })
        root.addView(TextView(this).apply {
            text = "To return here at any time, simply tap the widget."
            textSize = 13f
            setPadding(0, 10, 0, 0)
        })
        root.addView(TextView(this).apply {
            text = "Weather data provided by Open-Meteo"
            textSize = 12f
            setPadding(0, 12, 0, 0)
        })
        root.addView(TextView(this).apply {
            text = "Barn Weather Clock v${BuildConfig.VERSION_NAME}"
            textSize = 12f
            setTextColor(Color.DKGRAY)
            setPadding(0, 8, 0, 0)
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

        stopLocationRequest()
        locationStatus.text = "Location: asking Android for a recent position…"
        cancellationTokenSource = CancellationTokenSource()
        try {
            val request = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setDurationMillis(20000L)
                .setMaxUpdateAgeMillis(120000L)
                .build()
            fusedLocationClient.getCurrentLocation(request, cancellationTokenSource!!.token)
                .addOnSuccessListener { result ->
                    if (result != null) populateCoordinates(result)
                    else requestLocationUpdateFallback()
                }
                .addOnFailureListener { error ->
                    locationStatus.text = "Location: current request failed; trying live updates…"
                    requestLocationUpdateFallback(error.message)
                }
        } catch (_: SecurityException) {
            stopLocationRequest()
            locationStatus.text = "Location: permission required. Allow approximate or precise access."
        } catch (failure: Exception) {
            stopLocationRequest()
            locationStatus.text = "Location: could not start (${failure.javaClass.simpleName})."
        }
    }

    private fun requestLocationUpdateFallback(previousError: String? = null) {
        if (isFinishing || isDestroyed) return
        try {
            locationStatus.text = if (previousError.isNullOrBlank()) {
                "Location: no cached fix; waiting up to 30 seconds for GPS/Wi-Fi…"
            } else {
                "Location: retrying with live updates…"
            }
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                .setWaitForAccurateLocation(false)
                .setMaxUpdates(1)
                .setDurationMillis(30000L)
                .build()
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val fix = result.lastLocation
                    if (fix != null) populateCoordinates(fix)
                    else {
                        stopLocationRequest()
                        locationStatus.text = "Location: Android returned no position. Try outdoors or enter coordinates manually."
                    }
                }
            }
            locationCallback = callback
            fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
                .addOnSuccessListener {
                    locationHandler.removeCallbacks(locationTimeout)
                    locationHandler.postDelayed(locationTimeout, 30000L)
                }
                .addOnFailureListener { failure ->
                    stopLocationRequest()
                    locationStatus.text = "Location failed: ${failure.localizedMessage ?: failure.javaClass.simpleName}"
                }
        } catch (_: SecurityException) {
            stopLocationRequest()
            locationStatus.text = "Location: permission required. Allow approximate or precise access."
        } catch (failure: Exception) {
            stopLocationRequest()
            locationStatus.text = "Location failed: ${failure.localizedMessage ?: failure.javaClass.simpleName}"
        }
    }

    private fun populateCoordinates(result: Location) {
        stopLocationRequest()
        latitude.setText(String.format(java.util.Locale.US, "%.6f", result.latitude))
        longitude.setText(String.format(java.util.Locale.US, "%.6f", result.longitude))
        location.setText("Current device location")
        locationStatus.text = "Location added (accuracy about ${result.accuracy.toInt()} m). Press Save settings to apply."
        Toast.makeText(this, "Current coordinates added.", Toast.LENGTH_SHORT).show()
    }

    private fun stopLocationRequest() {
        locationHandler.removeCallbacks(locationTimeout)
        cancellationTokenSource?.cancel()
        cancellationTokenSource = null
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) obtainCurrentLocation()
            else locationStatus.text = "Location permission denied. You can still enter coordinates manually."
        }
    }

    override fun onDestroy() {
        stopLocationRequest()
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
