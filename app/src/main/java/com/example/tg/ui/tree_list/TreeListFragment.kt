package com.example.tg.ui.tree_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tg.databinding.FragmentTreeListBinding
import com.example.tg.models.TreeModel
// Logging
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Switch
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository
import com.example.tg.ui.adapters.TreeListAdapter
import com.example.tg.util.Haversine
import kotlinx.coroutines.launch

class TreeListFragment : Fragment() {
    private var _binding: FragmentTreeListBinding? = null // Declare the binding
    private val binding get() = _binding!!  // This ensures safe access to binding throughout the fragment lifecycle.
    private var allTrees: List<TreeModel> = listOf() // Maintains the list of all trees for filtering
    private lateinit var treeListAdapter: TreeListAdapter
    private lateinit var treeRepository: TreeRepository
    private var proximity: Double = 100000.0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTreeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.treesRecyclerView.layoutManager = LinearLayoutManager(context)
        treeListAdapter = TreeListAdapter(listOf())
        binding.treesRecyclerView.adapter = treeListAdapter

        setupSpinners()
        getTrees()
    }

    // Listeners for spinners to call the filterTrees function.
    private fun setupSpinners() {
        binding.spinnerSpecies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Need to check for null here as this was causing the app
                // to crash when switching back to the TreeList page
                if (view != null) {
                    filterTrees()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spinnerHealth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                if (view != null) {
                    filterTrees()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spinnerProximity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view != null) {
                    setProximity()
                    filterTrees()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getTrees() {
        // Gets all trees from the API
        treeRepository = TreeRepository()
        treeRepository.getAllTrees(object : TreeDataCallback {
            override fun onSuccess(trees: List<TreeModel>) {
                allTrees = trees
                treeListAdapter.updateData(trees)
                setSpeciesSpinner()
                setHealthSpinner()
            }

            override fun onError(errorMessage: String) {
                Log.e("Tree Fetch Error", errorMessage)
            }
        })
    }

    private fun setSpeciesSpinner() {
        //Updates the Species Spinner to include all unique species
        val speciesUnique = allTrees.map { it.species }.toSet().toList().sorted()
        val speciesValues = listOf("All Species") + speciesUnique
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, speciesValues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSpecies.adapter = adapter
    }

    private fun setHealthSpinner() {
        //Updates the Species Spinner to include all unique species
        val healthUnique = allTrees.map { it.healthStatus }.toSet().toList().sorted()
        val healthValues = listOf("All Statuses") + healthUnique
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, healthValues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHealth.adapter = adapter
    }

    private fun setProximity() {
        val proximityFilter = binding.spinnerProximity.selectedItem.toString()
        proximity = when (proximityFilter) {
            "50m" -> 0.05
            "100m" -> 0.1
            "200m" -> 0.2
            "300m" -> 0.3
            "400m" -> 0.4
            "500m" -> 0.5
            "1km" -> 1.0
            "1.5km" -> 1.5
            else -> {
                100000.0
            }
        }
    }

    private fun filterTrees() {
        val speciesFilter = binding.spinnerSpecies.selectedItem.toString()
        val healthFilter = binding.spinnerHealth.selectedItem.toString()
        var latitude: Double = 0.0
        var longitude: Double = 0.0

        lifecycleScope.launch {
            try {
                var (lat, lon) = Location.getLastLocation(requireContext())
                latitude = lat
                longitude = lon
                Log.d("LOCTEST", "LOCATION RECEIVED: $lat $lon")
            } catch (e: Exception) {
                Log.e("LOCTEST", "ERROR IN LOCATION, $e")
            }
        }

        Log.d("LOCTEST", "LOCATION RECEIVED: $latitude $longitude")

        var filteredList = allTrees.filter { tree ->
            val matchesFilter =
                (speciesFilter == "All Species" || tree.species == speciesFilter.uppercase()) &&
                        (healthFilter == "All Statuses" || tree.healthStatus == healthFilter.uppercase())
            matchesFilter
        }

        var h = Haversine(latitude, longitude)
        filteredList = filteredList.filter { tree ->
            val h2 = Haversine(tree.latitude, tree.longitude)
            Log.d("INFO", h.getDistance(h2).toString())
            h.getDistance(h2) < proximity
        }

        treeListAdapter.updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clears the binding when the view is destroyed
    }
}
