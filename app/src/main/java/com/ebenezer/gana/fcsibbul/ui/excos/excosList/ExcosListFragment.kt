package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.fcsibbul.databinding.ExcoslistFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "ExcosListFragment"

@AndroidEntryPoint
class ExcosListFragment : Fragment() {

    private var _binding: ExcoslistFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel:ExcosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExcoslistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ExcosListAdapter(requireContext()) {
            val action =
                ExcosListFragmentDirections.actionNavigationExcosToNavigationExcosDetails(it)
            findNavController().navigate(action)
        }
        binding.rvExcos.adapter = adapter
        viewModel.excos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.swipeRefresh.isRefreshing = false
        }
        binding.swipeRefresh.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getExcosList()
                binding.swipeRefresh.isRefreshing = true

            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Called")
        lifecycleScope.launch {
            viewModel.getExcosList()
            binding.swipeRefresh.isRefreshing = true
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}