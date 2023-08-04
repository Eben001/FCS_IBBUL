package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.ebenezer.gana.fcsibbul.databinding.ExcoslistFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG = "ExcosListFragment"


class ExcosListFragment : BaseFragment() {

    private var _binding: ExcoslistFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExcosViewModel by viewModel()

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

       lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED){
               adapter.loadStateFlow.collect{
                   binding.prependProgress.isVisible = it.source.prepend is LoadState.Loading
                   binding.appendProgress.isVisible = it.source.append is LoadState.Loading
               }
           }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.excoPagingFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        binding.rvExcos.adapter = adapter
        /* viewModel.excos.observe(viewLifecycleOwner) {
             adapter.submitList(it)
             binding.swipeRefresh.isRefreshing = false
         }*/
        binding.swipeRefresh.setOnClickListener {
            lifecycleScope.launch {
                //viewModel.getExcosList()
              //  binding.swipeRefresh.isRefreshing = true

            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(TAG).d("onStart: Called")
        /*lifecycleScope.launch {
            viewModel.getExcosList()
            binding.swipeRefresh.isRefreshing = true
        }*/
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}