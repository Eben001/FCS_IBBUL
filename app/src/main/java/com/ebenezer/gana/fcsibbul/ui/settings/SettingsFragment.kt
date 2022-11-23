package com.ebenezer.gana.fcsibbul.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.ebenezer.gana.fcsibbul.databinding.FragmentSettingsBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedOut


class SettingsFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            viewModel.logoutUser()
        }

        viewModel.loggedOut.observe(viewLifecycleOwner) { loggedOut ->
            if (loggedOut) {
                val intent = Intent(requireContext(), HostActivityLoggedOut::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }


}