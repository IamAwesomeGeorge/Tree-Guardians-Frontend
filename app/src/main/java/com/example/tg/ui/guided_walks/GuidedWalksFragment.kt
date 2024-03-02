package com.example.tg.ui.guided_walks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tg.databinding.FragmentGuidedWalksBinding

class GuidedWalksFragment : Fragment() {


    private var _binding: FragmentGuidedWalksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val GuidedWalksViewModel =
            ViewModelProvider(this).get(GuidedWalksViewModel::class.java)

        _binding = FragmentGuidedWalksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGuidedWalks
        GuidedWalksViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}