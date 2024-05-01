package com.example.tg.ui.add_tree_metadata
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tg.R
import com.google.android.gms.maps.model.MarkerOptions
import Location
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tg.databinding.FragmentAddTreeLocationBinding
import com.example.tg.databinding.FragmentAddTreeMetadataBinding
import com.google.android.gms.maps.MapView
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import com.example.tg.models.SpeciesModel
import com.example.tg.models.TreeAdditionModel
import com.example.tg.models.TreeModel
import com.example.tg.repositories.SpeciesDataCallback
import com.example.tg.repositories.SpeciesRepository
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository


class AddTreeMetadataFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var get_location_btn: Button
    private lateinit var next_btn: Button
    private lateinit var prev_btn: Button
    private lateinit var species_spinner: Spinner

    private lateinit var addTreeMapFragment: MapView
    private lateinit var speciesRepository: SpeciesRepository

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
        val binding = FragmentAddTreeMetadataBinding.inflate(inflater, container, false)
        val root: View = binding.root
        arguments?.let {
            val pair = it.getSerializable("locationPair") as? Pair<*, *>
            pair?.let { (lat, lon) ->
                location_lat = lat as Double
                location_lon = lon as Double
                Log.d("PASSTHROUGH", "LOCATION PASSED: $lat $lon")
            }
        }

        next_btn = binding.root.findViewById<Button>(R.id.metadata_next_btn)
        prev_btn = binding.root.findViewById<Button>(R.id.metadata_prev_btn)
        species_spinner = binding.root.findViewById<Spinner>(R.id.spinnerSpecies)

        // Get spinner



        val viewModel: TreeAdditionModel by viewModels()
        val pair = Pair(location_lat, location_lon)
        val bundle = Bundle()
        bundle.putSerializable("locationPair", pair)
        findNavController().previousBackStackEntry?.savedStateHandle?.set("bundle_key", bundle)

        speciesRepository = SpeciesRepository()
        speciesRepository.getAllSpecies(object : SpeciesDataCallback {
            override fun onSuccess(species: List<SpeciesModel>) {
                val speciesUnique = species.map { it.species }.toSet().toList().filter { it != "UNKNOWN" }
                val speciesValues = listOf("UNKNOWN") + speciesUnique
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, speciesValues)
                binding.spinnerSpecies.adapter = adapter
            }

            override fun onError(errorMessage: String) {
                Log.e("Tree Fetch Error", errorMessage)
            }
        })




        prev_btn.setOnClickListener {
            findNavController().popBackStack(R.id.add_tree_fab, false)
        }













        return root
    }








}