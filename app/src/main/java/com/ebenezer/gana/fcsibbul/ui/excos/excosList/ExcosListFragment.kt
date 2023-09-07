package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.databinding.ExcoslistFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "ExcosListFragment"


class ExcosListFragment : BaseFragment() {

    private var _binding: ExcoslistFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExcosViewModel by viewModel()
    private lateinit var adapter: ExcosListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExcoslistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        adapter = ExcosListAdapter(requireContext())
        viewModel.verifyIfAdmin()
        viewModel.isAdmin.observe(viewLifecycleOwner) {isAdmin->
            if(isAdmin){
                adapter.setOnItemLongClickListener { excos ->
                    showConfirmDeleteDialog(excos)
                }
            }else{
                adapter.removeOnItemLongClickListener()
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect {
                    binding.prependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                    binding.swipeRefresh.isRefreshing = false

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.excoPagingFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        binding.rvExcos.adapter = adapter

    }

    private fun showConfirmDeleteDialog(excos: Exco) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_dialog_title))
            .setMessage(resources.getString(R.string.delete_exco_dialog_message))
            .setIcon(R.drawable.ic_vector_delete)
            .setNeutralButton(resources.getString(R.string.cancel_dialog_message)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                deleteExcoDetails(excos)
                dialog.dismiss()
                observeViewModels()
            }
            .show()

    }

    private fun deleteExcoDetails(excos: Exco) {
        viewModel.deleteExcoDetails(excos.documentId, excos.image_url)
    }

    private fun observeViewModels() {
        viewModel.result.observe(viewLifecycleOwner) {
            if (viewModel.isDeleteSuccess.value == true) {
                showSnackBar(it.asString(requireContext()), false)
                refreshList()
            } else {
                showSnackBar(it.asString(requireContext()), true)
            }
        }


    }

    private fun setOnClickListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            refreshList()
        }
    }

    private fun refreshList() {
        adapter.refresh()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}