package com.example.tg.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tg.databinding.FragmentMapBinding
import com.google.android.material.snackbar.Snackbar

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val MapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Add a tree", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
/*
        val textView: TextView = binding.textMap

        MapViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val textView2: TextView = binding.textMap2
        MapViewModel.text2.observe(viewLifecycleOwner) {
            textView2.text = it
        }
  */
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}