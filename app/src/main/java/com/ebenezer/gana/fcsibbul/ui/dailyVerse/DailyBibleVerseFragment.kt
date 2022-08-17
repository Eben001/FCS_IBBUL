package com.ebenezer.gana.fcsibbul.ui.dailyVerse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ebenezer.gana.fcsibbul.databinding.FragmentDailyBibleVerseBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.ebenezer.gana.fcsibbul.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyBibleVerseFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE
    override var drawerState = Constants.DRAWER_STATE_LOCKED_CLOSED
    private var _binding: FragmentDailyBibleVerseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DailyBibleVerseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyBibleVerseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDailyBibleVerse()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.bibleVerse.observe(viewLifecycleOwner) { bibleVerse ->
            if (bibleVerse.isEmpty()) {
                binding.cardDailyVerse.visibility = View.GONE
            } else {
                binding.dailyVerse.text = bibleVerse
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}