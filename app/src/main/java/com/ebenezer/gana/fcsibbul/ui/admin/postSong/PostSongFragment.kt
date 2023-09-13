package com.ebenezer.gana.fcsibbul.ui.admin.postSong

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import androidx.annotation.RequiresApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.network.NetworkStatusChecker
import com.ebenezer.gana.fcsibbul.databinding.FragmentPostSongBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.google.android.gms.ads.AdRequest
import org.koin.android.ext.android.inject

@RequiresApi(Build.VERSION_CODES.M)
class PostSongFragment : BaseFragment() {
    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentPostSongBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostSongViewModel by viewModel()

    private val networkStatusChecker by inject<NetworkStatusChecker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAds()
        observeViewModels()
        setOnclickListeners()


    }
    private fun initializeAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

    }


    private fun observeViewModels() {
        viewModel.result.observe(viewLifecycleOwner) {
            if (viewModel.isPostSuccess.value == true) {
                showSnackBar(it.asString(requireContext()), isError = false)
            } else {
                showSnackBar(it.asString(requireContext()), isError = true)
            }
        }
    }


    private fun setOnclickListeners() {
        binding.postSong.setOnClickListener {
            networkStatusChecker.performIfConnectedToInternetOrNot(
                action = {
                    postSong()
                    clearTextEntries()
                    hideKeyboard()
                },
                onNoInternet = {
                    hideKeyboard()
                    showSnackBar(
                        resources.getString(R.string.msg_connect_to_the_internet),
                        isError = true
                    )
                }
            )
        }
    }

    private fun clearTextEntries() {
        binding.etTitle.text?.clear()
        binding.etDetails.text?.clear()
    }

    private fun postSong() {
        viewModel.postSong(
            posterId = "",
            binding.etTitle.text.toString(),
            binding.etDetails.text.toString()
        )
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Hide keyboard.
        hideKeyboard()
        _binding = null

    }


}