package com.ebenezer.gana.fcsibbul.ui.dailyVerse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.ebenezer.gana.fcsibbul.utils.Constants

class DailyBibleVerseFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE
    override var drawerState = Constants.DRAWER_STATE_LOCKED_CLOSED


    private lateinit var viewModel: DailyBibleVerseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_bible_verse, container, false)
    }


}