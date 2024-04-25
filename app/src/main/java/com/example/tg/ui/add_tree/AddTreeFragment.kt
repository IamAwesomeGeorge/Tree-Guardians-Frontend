package com.example.tg.ui.add_tree

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tg.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import Location
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.tg.databinding.FragmentAddTreeLocationBinding
import com.google.android.gms.maps.MapView
import kotlinx.coroutines.launch

interface LocationUpdateListener {
    fun onLocationUpdate(lat: Double, lon: Double)
}

class AddTreeFragment : Fragment(), LocationUpdateListener {
    // TODO: Rename and change types of parameters
    private lateinit var get_location_btn: Button
    private lateinit var next_btn: Button

    private lateinit var addTreeMapFragment: MapView

    private var location_lat:Double = 51.88201959762641
    private var location_lon:Double = -2.0511212095032993

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add any initialization logic here
        // For example, initialize variables or retrieve arguments from savedInstanceState
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddTreeLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addTreeMapFragment = root.findViewById<MapView>(R.id.add_tree_map)

        addTreeMapFragment.let {
            it.onCreate(savedInstanceState)
            it.getMapAsync { addTreeGoogleMap ->
                // Configure the map here
                addTreeGoogleMap.uiSettings.isZoomControlsEnabled = false
                addTreeGoogleMap.uiSettings.isMyLocationButtonEnabled = false

                val customMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.tree_basic)
                val resizedMarkerBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 128, 128, false)

                // Add a marker at a specific location (optional)
                val newTreeMarker = LatLng(Location.DEFAULT_LAT, Location.DEFAULT_LON)

                addTreeGoogleMap.addMarker(
                    MarkerOptions()
                        .position(newTreeMarker)
                        .title("New Tree")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap))
                )

                // Move the camera to a specific location with zoom level (optional)
                addTreeGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newTreeMarker, 16f))
            }
        }

        get_location_btn = binding.root.findViewById<Button>(R.id.location_get_btn)
        next_btn = binding.root.findViewById<Button>(R.id.location_next_btn)

        next_btn.isEnabled = false




        get_location_btn.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val (lat, lon) = Location.getLastLocation(requireContext())
                    Log.d("LOCTEST", "LOCATION RECEIVED: $lat $lon")
                    locationSet(lat, lon)
                } catch (e: Exception) {
                    Log.e("LOCTEST", "ERROR IN LOCATION, $e")
                }
            }
        }

        return root
    }

    private fun locationSet(lat: Double, lon: Double) {
        moveMarkerAndCamera(lat, lon)
        location_lat = lat
        location_lon = lon

        if(location_lat != 51.88201959762641 && location_lon != -2.0511212095032993) {
            if (Location.check_location(location_lat, location_lon))
            next_btn.isEnabled = true
        }
    }



    override fun onLocationUpdate(lat: Double, lon: Double) {
        // This method will be called when a location update is received
        // Call moveMarkerAndCamera with the received locations
        moveMarkerAndCamera(lat, lon)
    }

    private fun moveMarkerAndCamera(newLat: Double, newLon: Double) {
        val newLocation = LatLng(newLat, newLon)

        // Check if googleMap is not null
        addTreeMapFragment.let {
            it.getMapAsync { addTreeGoogleMap ->
                // Clear existing markers
                addTreeGoogleMap.clear()

                val customMarkerBitmap =
                    BitmapFactory.decodeResource(resources, R.drawable.tree_basic)
                val resizedMarkerBitmap =
                    Bitmap.createScaledBitmap(customMarkerBitmap, 128, 128, false)

                // Add a marker to the new location
                addTreeGoogleMap.addMarker(
                    MarkerOptions()
                        .position(newLocation)
                        .title("New Tree")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap))
                )

                // Move camera to the new marker location with animation
                addTreeGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16f))
            }
        }
    }


}