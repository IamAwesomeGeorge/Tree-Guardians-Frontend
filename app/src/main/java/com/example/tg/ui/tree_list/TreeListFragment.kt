package com.example.tg.ui.tree_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tg.R
import com.example.tg.api.RetrofitClient
import com.example.tg.databinding.FragmentTreeListBinding
import com.example.tg.models.TreeModel
// Retrofit HTTP client for API calls
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
// Logging
import android.util.Log
import com.example.tg.models.TreeResponse
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository


class TreeListFragment : Fragment() {

    private var _binding: FragmentTreeListBinding? = null
    private lateinit var treeRepository: TreeRepository

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!  // What is this for? - Tom

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tree_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use the TreeRepository Class to fetch data relating to trees
        treeRepository = TreeRepository()
        treeRepository.getAllTrees(object : TreeDataCallback {
            override fun onSuccess(trees: List<TreeModel>) {
                val numbersList = trees.mapIndexed { index, treeModel ->
                    "${index + 1}: ${treeModel.species}, ${treeModel.healthStatus} 🌳"
                }
                view.findViewById<TextView>(R.id.numbersTextView).text = numbersList.joinToString("\n")
            }

            override fun onError(errorMessage: String) {
                // Handle error
                Log.e("Tree Fetch Error", errorMessage)
            }
        })

        /*
        RetrofitClient.instance.getAllTrees().enqueue(object : Callback<TreeResponse> {
            override fun onResponse(call: Call<TreeResponse>, response: Response<TreeResponse>) {
                if (response.isSuccessful) {
                    // Check data exists
                    val treeData = response.body()?.trees
                    if (treeData != null) {
                        val numbersList = mutableListOf<String>()
                        // Populate the list with tree species and health status (We can update later - Tom)
                        treeData.forEachIndexed { index, treeModel ->
                            val treeInfo = "Tree #${index + 1}: ${treeModel.species}, ${treeModel.healthStatus} 🌳"
                            numbersList.add(treeInfo)
                        }
                        val numbersTextView = view.findViewById<TextView>(R.id.numbersTextView)
                        numbersTextView.text = numbersList.joinToString("\n")
                    } else {
                        Log.e("API Error", "No data returned")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("API Error", "Failed to fetch tree data: $errorBody")
                }
            }

            override fun onFailure(call: Call<TreeResponse>, t: Throwable) {
                Log.e("API Error", "Failed to fetch tree data", t)
            }
        })*/


        /*
        -- Test code with a simple list - replaced by API call above --
        val numbersTextView = view.findViewById<TextView>(R.id.numbersTextView)
        val numbersList = mutableListOf<String>()
        for (i in 1..100) {
            numbersList.add("Tree #" + i.toString() + "🌳")
        }
        numbersTextView.text = numbersList.joinToString("\n")
        */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}