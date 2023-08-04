package com.ebenezer.gana.fcsibbul.ui.announcement.announcementList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.databinding.ListItemAnnouncementBinding

class AnnouncementListAdapter(private val onItemClicked: (Announcement) -> Unit) :
    PagingDataAdapter<Announcement,
            AnnouncementListAdapter.AnnouncementViewHolder>(DiffCallback) {


    class AnnouncementViewHolder(private var binding: ListItemAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(announcement: Announcement) {
            binding.apply {
                title.text = announcement.title
                announcementDateTime.text = announcement.date
                announcementDetails.text = announcement.details

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        return AnnouncementViewHolder(
            ListItemAnnouncementBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val current = getItem(position)

        current?.let {
            holder.bind(current)
            holder.itemView.setOnClickListener {
                onItemClicked(current)
            }
        }


    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Announcement>() {
            override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
                return oldItem.date == newItem.date && oldItem.title == newItem.title
            }
        }
    }


}