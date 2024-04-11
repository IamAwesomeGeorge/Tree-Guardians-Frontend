package com.example.tg.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tg.R
import com.example.tg.databinding.FragmentMapBinding
import com.example.tg.ui.NewTree
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private lateinit var googleMap: GoogleMap

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addTreeFab.setOnClickListener { view ->
            addTree(view)
        }

        requireActivity().supportFragmentManager.popBackStack()

        // Access MapFragment using binding
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            // Configure the map here
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.isMyLocationButtonEnabled = true

            val customMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.tree_basic)
            val resizedMarkerBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 128, 128, false)


            // Add a marker at a specific location (optional)
            val mainOak = LatLng(51.88805651189536, -2.088274256170261)
            val mainOakSnippet = "Height: 24m | Diameter: 1.5m | Healthy"

            val mainOak1 = LatLng(51.88697515090959, -2.0899243290533973)
            val mainOakSnippet1 = "Height: 12m | Diameter: 0.5m | Not Healthy"

            /* This is where we need to add the code to */


            googleMap.addMarker(MarkerOptions()
                .position(mainOak)
                .title("Oak Tree")
                .snippet(mainOakSnippet)
                .icon(BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap))
            )

            googleMap.addMarker(MarkerOptions()
                .position(mainOak1)
                .title("Ash Tree")
                .snippet(mainOakSnippet1)
                .icon(BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap))
            )

            // Move the camera to a specific location with zoom level (optional)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainOak, 12f))
        }

        return root
    }

    // Inside your Activity or Fragment
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed to get location
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location
                getLocation()
            } else {
                // Permission denied
                // Handle this case as needed, for example, show a message or request permission again
            }
        }
    }

    private fun getLocation() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationManager?.let {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, handle this case
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Use latitude and longitude as needed
                    val locationString = "Added: $latitude, $longitude"
                    Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                    //Log.d(locationString)

                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTree(view: View) {
        // Get location and move to new page
        checkLocationPermissions()
        findNavController().navigate(R.id.add_tree_fab)
    }






}