package com.example.tg.ui.add_tree

import GetLocation
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import Location
import com.example.tg.databinding.FragmentAddTreeLocationBinding
import com.example.tg.databinding.FragmentMapBinding
import com.google.android.gms.maps.MapView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface LocationUpdateListener {
    fun onLocationUpdate(lat: Double, lon: Double)
}

class AddTreeFragment : Fragment(), LocationUpdateListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var get_location_btn: Button
    private lateinit var prev_btn: Button
    private lateinit var next_btn: Button

    private lateinit var addTreeMapFragment: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add any initialization logic here
        // For example, initialize variables or retrieve arguments from savedInstanceState
    }



    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(R.layout.fragment_add_tree_location, container, false)

        get_location_btn = view.findViewById(R.id.location_get_btn)

        requireActivity().supportFragmentManager.popBackStack()

        addTreeMapFragment =
            childFragmentManager.findFragmentById(R.id.add_tree_map) as SupportMapFragment

        addTreeMapFragment.getMapAsync { addTreeGoogleMap ->
            // Configure the map here
            addTreeGoogleMap.uiSettings.isZoomControlsEnabled = false
            addTreeGoogleMap.uiSettings.isMyLocationButtonEnabled = false

            val customMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.tree_basic)
            val resizedMarkerBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 128, 128, false)


            // Add a marker at a specific location (optional)
            val newTreeMarker = LatLng(51.88201959762641, -2.0511212095032993)


            addTreeGoogleMap.addMarker(
                MarkerOptions()
                    .position(newTreeMarker)
                    .title("New Tree")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap))
            )

            // Move the camera to a specific location with zoom level (optional)
            addTreeGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newTreeMarker, 12f))
        }

        get_location_btn.setOnClickListener {
            // Your function or logic goes here
            // For example:
            val (lat, lon) = Location().getLastLocation(requireContext())
            moveMarkerAndCamera(lat, lon)

            // TODO: add feature here
        }



        return view
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddTreeLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addTreeMapFragment = root.findViewById<MapView>(R.id.add_tree_map)

        addTreeMapFragment?.let {
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



        get_location_btn.setOnClickListener {
            // Create an instance of MyLocationListener
            Location.getLastLocation(requireContext(), this)
        }

        return root
    }

    override fun onLocationUpdate(lat: Double, lon: Double) {
        // This method will be called when a location update is received
        // Call moveMarkerAndCamera with the received locations
        moveMarkerAndCamera(lat, lon)
    }

    private fun moveMarkerAndCamera(newLat: Double, newLon: Double) {
        val newLocation = LatLng(newLat, newLon)

        // Check if googleMap is not null
        addTreeMapFragment?.let {
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