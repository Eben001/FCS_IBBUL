package com.fcsibbul.ui.signup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fcsibbul.R
import com.fcsibbul.data.models.WelcomeScreenImage
import com.fcsibbul.databinding.ItemWelcomeImageBinding

class WelcomeImageAdapter() :
    RecyclerView.Adapter<WelcomeImageAdapter.ImageViewHolder>() {
    private var imageUrls: List<WelcomeScreenImage> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWelcomeImageBinding.inflate(inflater, parent, false)
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
    fun updateImages(newImages: List<WelcomeScreenImage>) {
        val diffCallback = ImageDiffCallback(imageUrls, newImages)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        imageUrls = newImages
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ImageViewHolder(private val binding: ItemWelcomeImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            // Load the image into the ImageView using an image loading library like Glide or Picasso
            binding.welcomeImage.load(imageUrl){
                crossfade(true)
                placeholder(R.drawable.loading_animation)
                //transformations(CircleCropTransformation())

            }

        }
    }
}

class ImageDiffCallback(
    private val oldList: List<WelcomeScreenImage>,
    private val newList: List<WelcomeScreenImage>
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

