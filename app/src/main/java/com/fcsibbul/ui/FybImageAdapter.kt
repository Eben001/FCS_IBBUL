package com.fcsibbul.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fcsibbul.R
import com.fcsibbul.data.models.FYBImage
import com.fcsibbul.databinding.ListItemFybBinding

class FybImageAdapter :
    RecyclerView.Adapter<FybImageAdapter.ImageViewHolder>() {
    private var imageUrls: List<FYBImage> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemFybBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (imageUrls.isNotEmpty()) {
            val imageUrl = imageUrls[position % imageUrls.size]
            holder.bind(imageUrl.image_url)
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size * 1000
    }

    fun updateImages(newImages: List<FYBImage>) {
        val diffCallback = ImageDiffCallback(imageUrls, newImages)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        imageUrls = newImages
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ImageViewHolder(private val binding: ListItemFybBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            // Load the image into the ImageView using an image loading library like Glide or Picasso
            binding.fybImage.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.loading_animation)
            }

        }
    }
}

class ImageDiffCallback(
    private val oldList: List<FYBImage>,
    private val newList: List<FYBImage>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].image_url == newList[newItemPosition].image_url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // For simplicity, assuming the content of an item never changes
        return true
    }
}

