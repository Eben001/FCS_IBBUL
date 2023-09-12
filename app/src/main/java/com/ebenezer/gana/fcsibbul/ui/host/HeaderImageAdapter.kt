package com.ebenezer.gana.fcsibbul.ui.host

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.HeaderImage
import com.ebenezer.gana.fcsibbul.databinding.ItemWelcomeImageBinding

class HeaderImageAdapter() :
    RecyclerView.Adapter<HeaderImageAdapter.ImageViewHolder>() {
    private var imageUrls: List<HeaderImage> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWelcomeImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position % imageUrls.size]
        holder.bind(imageUrl.image_url)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
    fun updateImages(newImages: List<HeaderImage>) {
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
            binding.welcomeImage.rotationY = 180F

        }
    }
}

class ImageDiffCallback(
    private val oldList: List<HeaderImage>,
    private val newList: List<HeaderImage>
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

