package com.example.tg.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tg.R
import com.example.tg.databinding.FragmentMapBinding
import com.example.tg.models.TreeModel
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonFeature
import org.json.JSONObject
import com.google.maps.android.data.geojson.GeoJsonLayer // GeoJsonLayer Class for area boundary on google maps
import com.google.maps.android.data.geojson.GeoJsonPolygon
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var treeRepository: TreeRepository

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addTreeFab.setOnClickListener { view ->
            addTree(view)
        }

        requireActivity().supportFragmentManager.popBackStack()

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            this.googleMap = googleMap
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.isMyLocationButtonEnabled = true

            loadGeoJson("area_charlton.geojson") // Charlton Kings area coordinates

            val customMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.tree_basic)
            val resizedMarkerBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 128, 128, false)

            treeRepository = TreeRepository()
            treeRepository.getAllTrees(object : TreeDataCallback {
                override fun onSuccess(trees: List<TreeModel>) {
                    activity?.runOnUiThread {
                        trees.forEach { tree ->
                            val position = LatLng(tree.latitude, tree.longitude)
                            val snippet = "Height: ${tree.height} | Diameter: ${tree.circumference} | ${tree.healthStatus}"

                            googleMap.addMarker(MarkerOptions()
                                .position(position)
                                .title(tree.species)
                                .snippet(snippet)
                                .icon(BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap)))
                        }
                        trees.firstOrNull()?.let {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(it.latitude, it.longitude), 12f))
                        }
                    }
                }

                override fun onError(errorMessage: String) {
                    Log.e("Tree Fetch Error", errorMessage)
                }
            })
        }
        return root
    }



    private fun loadGeoJson(fileName: String) {
        try {
            val inputStream = context?.assets?.open(fileName)
            val jsonString = inputStream?.bufferedReader().use { it?.readText() }
            val geoJson = GeoJsonLayer(googleMap, JSONObject(jsonString))

            // Style the polygon
            val polygonStyle = GeoJsonPolygonStyle().apply {
                fillColor = Color.parseColor("#70565756") // Set fill color with transparency
                strokeColor = Color.TRANSPARENT // Make stroke color transparent
            }

            // Apply the style to all polygons in the layer
            geoJson.features.forEach { feature ->
                feature.polygonStyle = polygonStyle
            }

            geoJson.addLayerToMap()
        } catch (e: Exception) {
            Log.e("MapFragment", "Error loading GeoJSON: ${e.localizedMessage}")
        }
    }

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
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                // Permission denied
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
                return
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                location?.let        {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTree(view: View) {
        checkLocationPermissions()
        findNavController().navigate(R.id.add_tree_fab)
    }
}







/*
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tg.R
import com.example.tg.databinding.FragmentMapBinding
import com.example.tg.models.TreeModel
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var treeRepository: TreeRepository

    // This property is only valid between onCreateView and onDestroyView.
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

            // Use the TreeRepository Class to fetch data relating to trees
            treeRepository = TreeRepository()
            treeRepository.getAllTrees(object : TreeDataCallback {
                override fun onSuccess(trees: List<TreeModel>) {
                    activity?.runOnUiThread {
                        val mainTree = trees.firstOrNull()
                        for (tree in trees) {
                            val position = LatLng(tree.latitude, tree.longitude)
                            val snippet = "Height: ${tree.height} | Diameter: ${tree.circumference} | ${tree.healthStatus}"

                            googleMap.addMarker(MarkerOptions()
                                .position(position)
                                .title(tree.species)
                                .snippet(snippet)
                                .icon(BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap))
                            )
                        }

                        // Move the camera to the location of the first tree (If it exists)
                        mainTree?.let {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(it.latitude, it.longitude), 12f))
                        }
                    }
                }

                override fun onError(errorMessage: String) {
                    // Handle error
                    Log.e("Tree Fetch Error", errorMessage)
                }
            })

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
        findNavController().navigate(R.id.add_tree_fab_start)
    }
}
*/