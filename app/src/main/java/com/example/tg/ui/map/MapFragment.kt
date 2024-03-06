package com.example.tg.ui.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tg.R
import com.example.tg.databinding.FragmentMapBinding
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

        // Access MapFragment using binding
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            // Configure the map here
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true

            val customMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.tree_basic)
            val resizedMarkerBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 128, 128, false)


            // Add a marker at a specific location (optional)
            val mainOak = LatLng(51.88805651189536, -2.088274256170261)
            val mainOakSnippet = "Height: 24m | Diameter: 1.5m | Healthy"

            val mainOak1 = LatLng(51.88697515090959, -2.0899243290533973)
            val mainOakSnippet1 = "Height: 12m | Diameter: 0.5m | Not Healthy"


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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}