package com.fcsibbul.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.fcsibbul.databinding.FragmentSongsBinding
import com.fcsibbul.ui.baseFragment.BaseFragment
import com.fcsibbul.utils.Constants

class SongsFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE
    override var drawerState = Constants.DRAWER_STATE_LOCKED_CLOSED


    private val viewModel: SongsViewModel by viewModel()
    private var _binding: FragmentSongsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSong()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.song.observe(viewLifecycleOwner){
            if(it.title.isEmpty()){
                binding.title.visibility = View.GONE
                binding.content.text = it.content
                if(it.content.isEmpty()){
                    binding.cardSong.visibility = View.GONE
                }
            }else{
                binding.title.visibility = View.VISIBLE
                binding.title.text = it.title
                binding.content.text = it.content
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}