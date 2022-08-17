package com.ebenezer.gana.fcsibbul.ui.admin.postBibleVerse

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.network.NetworkStatusChecker
import com.ebenezer.gana.fcsibbul.databinding.FragmentPostBibleVerseBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class PostBibleVerseFragment : BaseFragment() {

    private val viewModel: PostBibleVerseViewModel by viewModels()
    private var _binding: FragmentPostBibleVerseBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var networkStatusChecker: NetworkStatusChecker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBibleVerseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
        setOnclickListeners()
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
        binding.postVerse.setOnClickListener {
            networkStatusChecker.performIfConnectedToInternetOrNot(
                action = {
                    postBibleVerse()
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
        binding.etDetails.text?.clear()
    }

    private fun postBibleVerse() {
        viewModel.postBibleVerse(
            posterId = "",
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
        hideKeyboard()
        _binding = null
    }


}