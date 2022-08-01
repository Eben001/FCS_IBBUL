package com.ebenezer.gana.fcsibbul.ui.announcement.announcementDetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.databinding.AnnouncementDetailsFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment

class AnnouncementDetailsFragment : BaseFragment() {


    private val viewModel: AnnouncementDetailsViewModel by viewModels()
    override var bottomNavigationViewVisibility = View.GONE

    private val navigationArgs: AnnouncementDetailsFragmentArgs by navArgs()
    private var _binding: AnnouncementDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AnnouncementDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val announcement = navigationArgs.announcement
        bind(announcement)

        observeViewModels()

        viewModel.getUpdatedLikedUsers(announcement.announcementId!!)
        viewModel.getUpdatedLikes(announcement.announcementId!!)

        binding.likeImage.setOnClickListener {
            viewModel.likeAnnouncement(
                viewModel.updatedLikeCount.value!!,
                viewModel.updatedLikedUsers.value!!,
                announcement.announcementId!!
            )

        }
        binding.share.setOnClickListener{
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,
                "${announcement.title} \n\n ${announcement.details} \n${announcement.date}")
                type = "text/plain"
            }.run { startActivity(Intent.createChooser(this, null)) }
        }

    }

    private fun observeViewModels() {
        viewModel.alreadyLiked.observe(viewLifecycleOwner) { alreadyLiked ->
            binding.likeImage.setImageResource(
                if (alreadyLiked)
                    R.drawable.ic_filled_like
                else R.drawable.ic_unfilled_like
            )
        }

        viewModel.likesCount.observe(viewLifecycleOwner) {
            binding.likes.text = it.toString()
        }

        viewModel.updatedLikedUsers.observe(viewLifecycleOwner) {
            viewModel.checkLike(it)
        }

    }


    private fun bind(announcement: Announcement) {
        binding.apply {
            title.text = announcement.title
            announcementDetails.text = announcement.details
            announcementDateTime.text = announcement.date
            likes.text = announcement.likeCount.toString()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}