import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.tg.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// Define the LocationListener interface
interface LocationListener {
    fun onLocationReceived(lat: Double, lon: Double)
    fun moveMarkerAndCamera(lat: Double, lon: Double)
}

class GetLocation : LocationListener {
    override fun onLocationReceived(lat: Double, lon: Double) {
        // This method will be called when location is received
        Log.d("LOCATION", "Received location: $lat, $lon")

        // Call moveMarkerAndCamera with the received location
        moveMarkerAndCamera(lat, lon)
    }

    override fun moveMarkerAndCamera(lat: Double, lon: Double) {
        Log.d("TEMP", "MOVE MAP TO LOCATION: $lat, $lon")
    }
}

object Location {
    public val DEFAULT_LAT = 51.88201959762641
    public val DEFAULT_LON = -2.0511212095032993
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Function to get the last known location
    public fun getLastLocation(context: Context, listener: LocationListener) {
        var lat: Double = this.DEFAULT_LAT
        var lon: Double = this.DEFAULT_LON
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context as AppCompatActivity)

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
        // Permission already granted, get last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude

                    Log.d("LOCATION", "Got location $lat, $lon")
                    listener.onLocationReceived(lat, lon)
                }
            }
            .addOnFailureListener { exception ->
                lat = this.DEFAULT_LAT
                lon = this.DEFAULT_LON
                Log.d("LOCATION", "Fail location $lat, $lon")
                listener.onLocationReceived(lat, lon)
            }
    }
}


