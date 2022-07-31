package com.ebenezer.gana.fcsibbul.ui.admin.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.fcsibbul.databinding.FragmentAdminDashboardBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.ebenezer.gana.fcsibbul.utils.Constants

private const val TAG = "AdminDashboardFragment"

class AdminDashboardFragment : BaseFragment() {
    override var bottomNavigationViewVisibility = View.GONE
    override var drawerState = Constants.DRAWER_STATE_LOCKED_CLOSED

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminDashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: Called")

        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: called")
        viewModel.verifyIfAdmin()
        viewModel.isAdmin.observe(viewLifecycleOwner) {
            if (it) {
                binding.llAdmin.visibility = View.VISIBLE
                binding.llNotAdmin.visibility = View.GONE
            } else {
                binding.llNotAdmin.visibility = View.VISIBLE
                binding.llAdmin.visibility = View.GONE
            }
        }

        viewModel.appBarTitle.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                it.asString(requireContext())
        }

        binding.postAnnouncement.setOnClickListener {
            val action =
                AdminDashboardFragmentDirections.actionAdminDashboardFragmentToPostAnnouncementFragment()
            this.findNavController().navigate(action)
        }
        binding.addExco.setOnClickListener {
            val action =
                AdminDashboardFragmentDirections.actionAdminDashboardFragmentToAddExcoFragment()
            this.findNavController().navigate(action)
        }
        binding.editExco.setOnClickListener {
            val action =
                AdminDashboardFragmentDirections.actionAdminDashboardFragmentToEditExcoFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}