package com.ebenezer.gana.fcsibbul.ui.admin.editExco

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.fcsibbul.databinding.FragmentEditExcoBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment

class EditExcoFragment : BaseFragment() {
    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentEditExcoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditExcoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editExco.setOnClickListener {
            val action = EditExcoFragmentDirections.actionEditExcoFragmentToAdminDashboardFragment()
            this.findNavController().navigate(action)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null

    }


}