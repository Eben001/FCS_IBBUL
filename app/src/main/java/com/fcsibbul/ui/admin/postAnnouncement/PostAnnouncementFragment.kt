package com.fcsibbul.ui.admin.postAnnouncement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.fcsibbul.R
import com.fcsibbul.data.network.NetworkStatusChecker
import com.fcsibbul.databinding.FragmentPostAnnouncementBinding
import com.fcsibbul.ui.baseFragment.BaseFragment
import com.google.android.gms.ads.AdRequest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class PostAnnouncementFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentPostAnnouncementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostAnnouncementViewModel by viewModel()


    private val networkStatusChecker by inject<NetworkStatusChecker>()

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
        initializeAds()
        viewModel.verifyIfAdmin()

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
                showSnackBar(requireView(),it.asString(requireContext()), isError = false)
            } else {
                showSnackBar(requireView(),it.asString(requireContext()), isError = true)
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
                            sendNotification()
                            clearTextEntries()
                            hideKeyboard()
                        } else {
                            val action =
                                PostAnnouncementFragmentDirections.actionPostAnnouncementFragmentToAdminDashboardFragment()
                            this.findNavController().navigate(action)
                        }
                    },
                    onNoInternet = {
                        showSnackBar(requireView(),
                            resources.getString(R.string.msg_connect_to_the_internet),
                            isError = true
                        )
                    })

            }


        }

    }

    private fun sendNotification() {
        viewModel.sendNotification("/topics/announcements", binding.etTitle.text.toString(),
            binding.etDetails.text.toString(), resources.getString(R.string.announcement_notification_channel))
    }

    private fun postNewAnnouncement() {
        val dateFormat =
            SimpleDateFormat("h:mm a, dd MMM yyyy", Locale.getDefault()) // e.g  9:43AM, 1 Oct 2022
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
                showSnackBar(requireView(),
                    resources.getString(R.string.err_msg_title),
                    isError = true
                )
                false
            }
            binding.etDetails.text.toString().trim().isEmpty() -> {
                showSnackBar(requireView(),
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