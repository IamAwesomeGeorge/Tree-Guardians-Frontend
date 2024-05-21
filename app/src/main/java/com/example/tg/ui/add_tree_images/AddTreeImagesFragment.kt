package com.example.tg.ui.add_tree_images
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tg.R
import com.google.android.gms.maps.model.MarkerOptions
import Location
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tg.databinding.FragmentAddTreeLocationBinding
import com.example.tg.databinding.FragmentAddTreeMetadataBinding
import com.google.android.gms.maps.MapView
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import com.example.tg.databinding.FragmentAddTreeImagesBinding
import com.example.tg.models.SpeciesModel
import com.example.tg.models.TreeAdditionModel
import com.example.tg.models.TreeModel
import com.example.tg.models.TreeResponse
import com.example.tg.repositories.SpeciesDataCallback
import com.example.tg.repositories.SpeciesRepository
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import java.io.ByteArrayOutputStream


class AddTreeImagesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var finish_btn: Button
    private lateinit var prev_btn: Button
    private lateinit var camera_btn: Button
    private lateinit var treeRepository: TreeRepository


    private var location_lat:Double = 51.88201959762641
    private var location_lon:Double = -2.0511212095032993
    private var speciesFilter:String = "UNKNOWN"
    private var height:Float = 1.0F
    private var circ:Float = 1.0F
    private var healthStatus:String = "AVERAGE"





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
        val binding = FragmentAddTreeImagesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        arguments?.let {
            val pair = it.getSerializable("locationPair") as? Pair<*, *>
            pair?.let { (lat, lon) ->
                location_lat = lat as Double
                location_lon = lon as Double

                Log.d("PASSTHROUGHIMAGES", "LOCATION PASSED: $lat $lon")
            }
            speciesFilter = it.getString("speciesFilter").toString()
            healthStatus = if(it.getBoolean("healthChecked")) {
                "POOR"
            } else {
                "HEALTHY"
            }
            height = it.getFloat("height")
            circ = it.getFloat("circ")
        }

        finish_btn = binding.root.findViewById<Button>(R.id.image_add_next_btn)
        prev_btn = binding.root.findViewById<Button>(R.id.image_add_prev_btn)
        prev_btn = binding.root.findViewById<Button>(R.id.image_add_prev_btn)


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
                1
            )
        }

        prev_btn.setOnClickListener {
            findNavController().popBackStack(R.id.location_next_btn, false)
        }

        finish_btn.setOnClickListener {
            addTree()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_map, true)
                .build()

            findNavController().navigate(R.id.nav_map, null, navOptions)

        }

        return root
    }

    private fun addTree() {

        // id_user is set to a default value for testing.
        val treeData = TreeModel(
            id=0,
            creationDate = "",
            idUser = "11111111-1111-1111-1111-111111111111",
            species=speciesFilter,
            latitude=location_lat,
            longitude=location_lon,
            healthStatus=healthStatus,
            circumference= circ.toDouble(),
            planted="",
            height= height.toInt(),
            isDeleted=0
        )

        treeRepository = TreeRepository()
        treeRepository.createTree(treeData, object : TreeDataCallback {
            override fun onSuccess(trees: List<TreeModel>) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Tree added successfully!", Toast.LENGTH_SHORT).show()
                    // Add navigation back to map page
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.nav_map, true)
                        .build()

                    findNavController().navigate(R.id.nav_map, null, navOptions)

                }
            }

            override fun onError(errorMessage: String) {
                activity?.runOnUiThread {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.nav_map, true)
                        .build()

                    findNavController().navigate(R.id.nav_map, null, navOptions)

                }
            }
        })
    }

}


