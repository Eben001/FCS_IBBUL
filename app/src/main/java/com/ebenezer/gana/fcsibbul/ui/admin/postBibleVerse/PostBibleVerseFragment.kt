package com.ebenezer.gana.fcsibbul.ui.admin.postBibleVerse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ebenezer.gana.fcsibbul.R

class PostBibleVerseFragment : Fragment() {

    private lateinit var viewModel: PostBibleVerseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_bible_verse, container, false)
    }


}