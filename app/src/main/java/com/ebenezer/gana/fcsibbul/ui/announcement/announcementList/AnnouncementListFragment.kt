package com.ebenezer.gana.fcsibbul.ui.announcement.announcementList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.AnnouncementlistFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.announcement.shared.SharedViewModel
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AnnouncementListFragment : BaseFragment() {
    override var bottomNavigationViewVisibility = View.VISIBLE

    private var _binding: AnnouncementlistFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter:AnnouncementListAdapter

    private val viewModel: AnnouncementListViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AnnouncementlistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSettingsMenu()
        Timber.d("${Firebase.auth.currentUser}")

        binding.rvAnnouncement.layoutManager = LinearLayoutManager(this.context)
        adapter = AnnouncementListAdapter {
            val action =
                AnnouncementListFragmentDirections.actionNavigationAnnouncementToAnnouncementDetailsFragment(
                    it
                )

            findNavController().navigate(action)
        }
        binding.rvAnnouncement.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                adapter.loadStateFlow.collect{
                    binding.prependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                    binding.swipeRefresh.isRefreshing = false

                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.announcementPagingFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)

                }
            }

        }
        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.announcementDeleted.observe(viewLifecycleOwner) { announcementDeleted ->
                    if (announcementDeleted) {
                        refreshList()
                        sharedViewModel.setAnnouncementDeleted(false) // Reset the value
                    }
                }
            }
        }

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            adapter.refresh()
        }
    }


    private fun setupSettingsMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.settings_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_settings -> {
                        val action =
                            AnnouncementListFragmentDirections.actionNavigationAnnouncementToSettingsFragment()
                        findNavController().navigate(action)
                        return true
                    }

                }

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun refreshList() {
        adapter.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}