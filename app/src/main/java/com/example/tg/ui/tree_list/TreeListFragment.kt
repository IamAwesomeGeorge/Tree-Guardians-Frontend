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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository
import com.example.tg.ui.adapters.TreeListAdapter
import kotlinx.coroutines.launch

class TreeListFragment : Fragment() {
    private var _binding: FragmentTreeListBinding? = null // Declare the binding
    private val binding get() = _binding!!  // This ensures safe access to binding throughout the fragment lifecycle.
    private var allTrees: List<TreeModel> = listOf() // Maintains the list of all trees for filtering
    private lateinit var treeListAdapter: TreeListAdapter
    private lateinit var treeRepository: TreeRepository

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

    private fun filterTrees() {
        val speciesFilter = binding.spinnerSpecies.selectedItem.toString()
        val healthFilter = binding.spinnerHealth.selectedItem.toString()
        val proximityFilter = binding.spinnerProximity.selectedItem.toString()
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

        val filteredList = allTrees.filter { tree ->
            val matchesSpecies =
                (speciesFilter == "All Species" || tree.species == speciesFilter.uppercase()) &&
                        (healthFilter == "All Statuses" || tree.healthStatus == healthFilter.uppercase())
            matchesSpecies
        }
        treeListAdapter.updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clears the binding when the view is destroyed
    }
}
