package com.ebenezer.gana.fcsibbul.ui.admin.postAnnouncement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.FragmentPostAnnouncementBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class PostAnnouncementFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentPostAnnouncementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostAnnouncementViewModel by viewModels()

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
        binding.postAnnouncement.setOnClickListener {

            //Is the announcement details valid?
            /**
             * We need to also check if the user has internet connection.
             * This will enable us fetch the users role.
             */
            if (isValidDetails()) {

                // is the user is an admin
                if (viewModel.isAdmin.value == true) {
                    postNewAnnouncement()
                    val action =
                        PostAnnouncementFragmentDirections.actionPostAnnouncementFragmentToAdminDashboardFragment()
                    this.findNavController().navigate(action)
                } else {
                    val action =
                        PostAnnouncementFragmentDirections.actionPostAnnouncementFragmentToAdminDashboardFragment()
                    this.findNavController().navigate(action)
                }

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
                    isError = true)
                false
            }
            binding.etDetails.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_details),
                    isError = true)

                false
            }
            else -> true
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}