package com.example.tg.ui.add_tree

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tg.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.activity.result.contract.ActivityResultContracts

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
    private lateinit var get_location_btn: Button
    private lateinit var get_camera_btn: Button
    private lateinit var image_preview: ImageView


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
        get_location_btn = view.findViewById(R.id.location_get_btn)
        get_camera_btn = view.findViewById(R.id.get_camera_btn)
        image_preview = view.findViewById(R.id.camera_preview)



                // Set OnClickListener and define logic inside onClick
        get_location_btn.setOnClickListener {
            // Your function or logic goes here
            // For example:
            getLastLocation()
            // TODO: add feature here
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }

        get_camera_btn.setOnClickListener{
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);

        }

        /*get_camera_btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(intent)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }*/


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            var bitmap = data?.extras?.get("data")
            image_preview.setImageBitmap(bitmap as Bitmap?)
        }
    }


    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("PERMISSIONS", "GRANTED")
        } else {
            Log.d("PERMISSIONS", "DENIED")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLastLocation()
    }

    private fun getLastLocation() {
        var lat: Double = 0.0
        var lon: Double = 0.0
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
                    lat = location.latitude
                    lon = location.longitude
                    locationOutputTextView.text = "Latitude: ${lat}, Longitude: ${lon}"
                } else {
                    lat = 0.0
                    lon = 0.0
                    locationOutputTextView.text = "Latitude: 0.0, Longitude: 0.0"
                }
            }
            .addOnFailureListener { exception ->
                lat = 0.0
                lon = 0.0
                locationOutputTextView.text = "Latitude: 0.0, Longitude: 0.0"
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

        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA = 2

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