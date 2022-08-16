package com.ebenezer.gana.fcsibbul.ui.dailyVerse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ebenezer.gana.fcsibbul.R

class DailyBibleVerseFragment : Fragment() {


    private lateinit var viewModel: DailyBibleVerseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_bible_verse, container, false)
    }


}