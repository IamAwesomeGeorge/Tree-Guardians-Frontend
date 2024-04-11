package com.example.tg.ui.tree_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tg.R
import com.example.tg.databinding.FragmentTreeListBinding
import com.example.tg.models.TreeModel
// Logging
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tg.repositories.TreeDataCallback
import com.example.tg.repositories.TreeRepository
import com.example.tg.ui.adapters.TreeListAdapter


class TreeListFragment : Fragment() {

    private var _binding: FragmentTreeListBinding? = null
    private lateinit var treeRepository: TreeRepository

    // This property is only valid between onCreateView and onDestroyView.
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

                val recyclerView = view.findViewById<RecyclerView>(R.id.treesRecyclerView)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = TreeListAdapter(trees)

                /*val numbersList = trees.mapIndexed { index, treeModel ->
                    "${index + 1}: ${treeModel.species}, ${treeModel.healthStatus} 🌳"
                }
                view.findViewById<TextView>(R.id.numbersTextView).text = numbersList.joinToString("\n")
                 */
            }

            override fun onError(errorMessage: String) {
                // Handle error
                Log.e("Tree Fetch Error", errorMessage)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}