package com.ebenezer.gana.fcsibbul.ui.announcement.announcementDetails

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.network.NetworkStatusChecker
import com.ebenezer.gana.fcsibbul.databinding.AnnouncementDetailsFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.announcement.shared.SharedViewModel
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@RequiresApi(Build.VERSION_CODES.M)

class AnnouncementDetailsFragment : BaseFragment() {

    private val viewModel: AnnouncementDetailsViewModel by viewModel()
    override var bottomNavigationViewVisibility = View.GONE
    private val navigationArgs: AnnouncementDetailsFragmentArgs by navArgs()
    private var _binding: AnnouncementDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val networkStatusChecker by inject<NetworkStatusChecker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AnnouncementDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.verifyIfAdmin()

        val announcement = navigationArgs.announcement
        viewModel.getUpdatedLikedUsers(announcement.announcementId!!)
        viewModel.getUpdatedLikes(announcement.announcementId!!)

        bind(announcement)
        setOnClickListeners(announcement)
        observeViewModels()
    }

    private fun bind(announcement: Announcement) {
        binding.apply {
            title.text = announcement.title
            announcementDetails.text = announcement.details
            announcementDateTime.text = announcement.date
            //likes.text = announcement.likeCount.toString()
        }
    }

    private fun setOnClickListeners(announcement: Announcement) {
        viewModel.checkDocumentExists(navigationArgs.announcement.announcementId!!){exists ->
            if(exists){
                binding.deleteImage.setOnClickListener {
                    /**
                     * Performs a network check
                     * @param action the action to perform when there is internet connection
                     * @param onNoInternet shows a no internet dialog
                     */
                    networkStatusChecker.performIfConnectedToInternetOrNot(
                        action = {
                            // is the user is an admin

                            if (viewModel.isAdmin.value == true) {
                                showConfirmDeleteDialog(announcement)
                                viewModel.result.observe(viewLifecycleOwner) {
                                    if (viewModel.isDeleteSuccess.value == true) {
                                        showSnackBar(it.asString(requireContext()), isError = false)
                                        findNavController().navigateUp()
                                    } else {
                                        showSnackBar(it.asString(requireContext()), isError = true)
                                        findNavController().navigateUp()
                                    }
                                }

                            } else {
                                showSnackBar(
                                    resources.getString(R.string.text_unauthorized_action),
                                    isError = true
                                )
                            }
                        },
                        onNoInternet = {
                            showSnackBar(
                                resources.getString(R.string.msg_connect_to_the_internet_to_delete),
                                isError = true
                            )
                        })

                }
                binding.likeImage.setOnClickListener {
                    viewModel.likeOrUnlikeAnnouncement(
                        viewModel.updatedLikeCount.value!!,
                        viewModel.updatedLikedUsers.value!!,
                        announcement.announcementId!!
                    )


                }
                binding.share.setOnClickListener {
                    Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "${announcement.title} \n\n ${announcement.details} \n${announcement.date}"
                        )
                        type = "text/plain"
                    }.run { startActivity(Intent.createChooser(this, null)) }
                }
            }else{
                // when the announcement doesn't exist
                showSnackBar("This announcement Doesn't exist", true)
                sharedViewModel.setAnnouncementDeleted(true)
                findNavController().navigateUp()

            }
        }

    }

    private fun observeViewModels() {
        viewModel.isAdmin.observe(viewLifecycleOwner) { isAdmin ->
            if (isAdmin) {
                binding.deleteImage.visibility = View.VISIBLE
            } else {
                binding.deleteImage.visibility = View.GONE
            }
        }


        viewModel.alreadyLiked.observe(viewLifecycleOwner) { alreadyLiked ->
            binding.likeImage.setImageResource(
                if (alreadyLiked)
                    R.drawable.ic_filled_like
                else R.drawable.ic_unfilled_like
            )
        }

        viewModel.updatedLikeCount.observe(viewLifecycleOwner) {
            binding.likes.text = it.toString()

        }

        viewModel.updatedLikedUsers.observe(viewLifecycleOwner) {
            viewModel.checkLike(it)
        }

    }

    private fun checkAnnouncementExist(id:String){

    }

    private fun showConfirmDeleteDialog(announcement: Announcement) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_dialog_title))
            .setMessage(resources.getString(R.string.delete_dialog_message))
            .setIcon(R.drawable.ic_vector_delete)
            .setNeutralButton(resources.getString(R.string.cancel_dialog_message)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                announcement.announcementId?.let { id -> viewModel.deleteAnnouncement(id) }
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .show()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}