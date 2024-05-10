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
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.RequiresApi
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

    private lateinit var species_selection: String
    private lateinit var height_selection: String
    private lateinit var circ_selection: String
    private lateinit var health_flag: String

    private lateinit var speciesCustomEntry: EditText
    private lateinit var spinner: Spinner
    private lateinit var adapter: ArrayAdapter<String>


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

        prev_btn = binding.root.findViewById<Button>(R.id.metadata_prev_btn)
        species_spinner = binding.root.findViewById<Spinner>(R.id.spinnerSpecies)
        speciesCustomEntry = binding.root.findViewById<EditText>(R.id.customSpecies)

        val checkBox: CheckBox = binding.root.findViewById(R.id.healthCheckBox)
        val heightEditText: EditText = binding.root.findViewById(R.id.height_input_metadata)
        val circEditText: EditText = binding.root.findViewById(R.id.circ_input_metadata)


        // Get spinner



        val pair = Pair(location_lat, location_lon)
        val bundle = Bundle()
        bundle.putSerializable("locationPair", pair)
        findNavController().previousBackStackEntry?.savedStateHandle?.set("bundle_key", bundle)

        speciesRepository = SpeciesRepository()
        speciesRepository.getAllSpecies(object : SpeciesDataCallback {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSuccess(species: List<SpeciesModel>) {
                val speciesUnique = species.map { it.species }.toSet().toList().filter { it != "UNKNOWN" }
                val speciesValues = listOf("UNKNOWN") + speciesUnique + listOf("OTHER")
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, speciesValues)
                binding.spinnerSpecies.adapter = adapter
                // Convert the list to an array
                val hintsArray = speciesUnique.toTypedArray()
                speciesCustomEntry.setAutofillHints(*hintsArray)
            }

            override fun onError(errorMessage: String) {
                Log.e("Tree Fetch Error", errorMessage)
            }
        })

        // Set up TextChangedListener for the EditText
        speciesCustomEntry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if EditText has text, and select last item in Spinner if it does
                if (s?.isNotEmpty() == true) {
                    species_spinner.setSelection(species_spinner.count - 1)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })



        species_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Clear the text in the EditText box when an item is selected in the Spinner
                speciesCustomEntry.setText("")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }




        prev_btn.setOnClickListener {
            findNavController().popBackStack(R.id.add_tree_fab, false)
        }



        next_btn.setOnClickListener {
            var speciesFilter = "UNKNOWN"

            if (speciesCustomEntry.text.isNotEmpty()) {
                speciesFilter = speciesCustomEntry.text.toString()
                // Do something with textValue
            } else {
                speciesFilter = binding.spinnerSpecies.selectedItem.toString()
            }

            val health_checked: Boolean = checkBox.isChecked

            val heightString: String = heightEditText.text.toString()
            val height: Float = if (heightString.isNotEmpty()) heightString.toFloat() else 0.0f

            val circString: String = circEditText.text.toString()
            val circ: Float = if (circString.isNotEmpty()) circString.toFloat() else 0.0f


            Log.d("PREPEAREPSSS", "LOCATION PASSED: $location_lat $location_lon SP: $speciesFilter H: $height C: $circ HEALTH: $health_checked")

            val pair_initial = Pair(location_lat, location_lon)
            bundle.putSerializable("locationPair", pair_initial)
            bundle.putString("speciesFilter", speciesFilter)
            bundle.putBoolean("healthChecked", health_checked)
            bundle.putFloat("height", height)
            bundle.putFloat("circ", circ)
            findNavController().navigate(R.id.metadata_next_btn, bundle)
        }













        return root
    }








}