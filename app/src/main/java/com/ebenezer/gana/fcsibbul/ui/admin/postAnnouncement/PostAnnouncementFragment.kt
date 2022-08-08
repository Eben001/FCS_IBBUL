package com.ebenezer.gana.fcsibbul.ui.admin.postAnnouncement

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.network.NetworkStatusChecker
import com.ebenezer.gana.fcsibbul.databinding.FragmentPostAnnouncementBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class PostAnnouncementFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentPostAnnouncementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostAnnouncementViewModel by viewModels()

    private val networkStatusChecker by lazy {
        NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostAnnouncementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.verifyIfAdmin()

        observeViewModels()
        setOnclickListeners()

    }

    private fun observeViewModels() {
        viewModel.result.observe(viewLifecycleOwner) {
            if(viewModel.isPostSuccess.value == true){
                showSnackBar(it.asString(requireContext()), isError = false)
            }else{
                showSnackBar(it.asString(requireContext()), isError = true)
            }
        }
    }
    private fun setOnclickListeners() {
        binding.postAnnouncement.setOnClickListener {
            if (isValidDetails()) {

                /**
                 * Performs a network check
                 * @param action the action to perform when there is internet connection
                 * @param onNoInternet shows a no internet dialog
                 */
                networkStatusChecker.performIfConnectedToInternetOrNot(
                    action = {
                        // is the user is an admin
                        if (viewModel.isAdmin.value == true) {
                            postNewAnnouncement()
                            clearTextEntries()
                            hideKeyboard()
                        } else {
                            val action =
                                PostAnnouncementFragmentDirections.actionPostAnnouncementFragmentToAdminDashboardFragment()
                            this.findNavController().navigate(action)
                        }
                    },
                    onNoInternet = {
                        showSnackBar(
                            resources.getString(R.string.msg_connect_to_the_internet),
                            isError = true
                        )
                    })

            }


        }

    }
    private fun postNewAnnouncement() {
        val dateFormat = SimpleDateFormat("h:mm a, dd MMM yyyy") // e.g  9:43AM, 1 Oct 2022
        val currentDateAndTime: String = dateFormat.format(Calendar.getInstance().timeInMillis)
        viewModel.postAnnouncement(
            userId = "",
            announcementId = "",
            binding.etTitle.text.toString(),
            binding.etDetails.text.toString(),
            date = currentDateAndTime,
            likeCount = 0L

        )
    }
    private fun isValidDetails(): Boolean {
        return when {
            binding.etTitle.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_title),
                    isError = true
                )
                false
            }
            binding.etDetails.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_details),
                    isError = true
                )

                false
            }
            else -> true
        }

    }

    private fun clearTextEntries() {
        binding.etTitle.text?.clear()
        binding.etDetails.text?.clear()
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