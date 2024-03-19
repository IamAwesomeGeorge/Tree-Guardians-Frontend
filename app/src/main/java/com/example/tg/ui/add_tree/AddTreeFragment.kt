package com.example.tg.ui.add_tree

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tg.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTreeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var locationOutputTextView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_tree, container, false)
        locationOutputTextView = view.findViewById(R.id.location_output)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLastLocation()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        // Permission already granted, get last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    locationOutputTextView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                } else {
                    locationOutputTextView.text = "Location not available"
                }
            }
            .addOnFailureListener { exception ->
                locationOutputTextView.text = "Error getting location: ${exception.message}"
            }
    }

    // Handle permission result in onRequestPermissionsResult callback







    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddTree.
         */
        // TODO: Rename and change types and number of parameters

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTreeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}