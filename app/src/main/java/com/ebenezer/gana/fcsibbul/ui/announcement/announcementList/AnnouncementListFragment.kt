package com.ebenezer.gana.fcsibbul.ui.announcement.announcementList

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebenezer.gana.fcsibbul.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.ebenezer.gana.fcsibbul.databinding.AnnouncementlistFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import kotlinx.coroutines.launch

class AnnouncementListFragment : BaseFragment() {
    override var bottomNavigationViewVisibility = View.VISIBLE

    private var _binding: AnnouncementlistFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnnouncementListViewModel by viewModel()


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

        binding.rvAnnouncement.layoutManager = LinearLayoutManager(this.context)
        val adapter = AnnouncementListAdapter {
            val action =
                AnnouncementListFragmentDirections.actionNavigationAnnouncementToAnnouncementDetailsFragment(
                    it
                )

            findNavController().navigate(action)
        }

        binding.rvAnnouncement.adapter = adapter
        viewModel.announcement.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.swipeRefresh.isRefreshing = false
        }
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getAnnouncements()
                binding.swipeRefresh.isRefreshing = true
            }

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
    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            viewModel.getAnnouncements()
            binding.swipeRefresh.isRefreshing = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}