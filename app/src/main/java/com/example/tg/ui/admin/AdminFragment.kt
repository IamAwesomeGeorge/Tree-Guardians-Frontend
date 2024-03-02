package com.example.tg.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tg.databinding.FragmentAdminBinding
class AdminFragment : Fragment() {
    private var _binding: FragmentAdminBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val AdminViewModel =
            ViewModelProvider(this).get(AdminViewModel::class.java)

        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAdmin
        AdminViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}