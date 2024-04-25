import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import kotlinx.coroutines.*



object Location {
    public val DEFAULT_LAT = 51.88201959762641
    public val DEFAULT_LON = -2.0511212095032993
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    suspend fun fetchLocation(context: Context, fusedLocationClient: FusedLocationProviderClient): Pair<Double, Double> = coroutineScope{
        // Permission already granted, get last location
        var lat: Double = DEFAULT_LAT
        var lon: Double = DEFAULT_LON

        val deferred = CompletableDeferred<Pair<Double, Double>>()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }



        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    deferred.complete(Pair(lat, lon))
                } else {
                    deferred.complete(Pair(DEFAULT_LAT, DEFAULT_LON))
                }
            }
            .addOnFailureListener { exception ->
                deferred.complete(Pair(DEFAULT_LAT, DEFAULT_LON))
            }

        deferred.await()
    }

    // Function to get the last known location
    public suspend fun getLastLocation(context: Context): Pair<Double, Double> {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context as AppCompatActivity)
        return fetchLocation(context, fusedLocationClient)
    }

    fun check_location(lat:Double, lon:Double): Boolean {
        // TODO: Check if the location is within the boundary
        return true
    }
}


