package com.example.tg.ui.tree_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tg.databinding.FragmentTreeListBinding

class TreeListFragment : Fragment() {

    private var _binding: FragmentTreeListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val TreeListViewModel =
            ViewModelProvider(this).get(TreeListViewModel::class.java)

        _binding = FragmentTreeListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTreeList
        TreeListViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}