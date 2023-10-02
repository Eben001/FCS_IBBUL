package com.fcsibbul.ui.dailyGuide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fcsibbul.R
import com.fcsibbul.databinding.FragmentDailyGuideBinding
import com.fcsibbul.ui.baseFragment.BaseFragment
import com.fcsibbul.utils.Constants

class DailyGuideFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE
    override var drawerState = Constants.DRAWER_STATE_LOCKED_CLOSED


    private var _binding: FragmentDailyGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDailyGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext()).load(R.drawable.coming_soon_gif).into(binding.comingSoonGif)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}