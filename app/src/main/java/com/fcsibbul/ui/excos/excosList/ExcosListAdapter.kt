package com.fcsibbul.ui.excos.excosList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fcsibbul.R
import com.fcsibbul.data.models.Exco
import com.fcsibbul.databinding.ListItemExcosBinding

class ExcosListAdapter(private val context: Context) : PagingDataAdapter<Exco,
        ExcosListAdapter.ExcosViewHolder>(DiffCallback) {

    private var onItemLongClickListener: ((Exco) -> Unit)? = null
    fun setOnItemLongClickListener(listener: (Exco) -> Unit) {
        onItemLongClickListener = listener
    }
    fun removeOnItemLongClickListener() {
        onItemLongClickListener = null
    }

    inner class ExcosViewHolder(private var binding: ListItemExcosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(excos: Exco) {
            binding.apply {
                binding.apply {
                    excoName.text = context.resources.getString(R.string.first_name_last_name, excos.firstName,excos.lastName)
                    excoPhone.text = excos.phone
                    excoPost.text = excos.office
                    excoDepartment.text = excos.department
                    excoLevel.text = context.resources.getString(R.string.exco_level, excos.level)
                    excoImage.load(excos.image_url){
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                    itemView.setOnLongClickListener {
                        onItemLongClickListener?.invoke(excos)
                        true // Return true to consume the long click event
                    }
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcosViewHolder {
        return ExcosViewHolder(
            ListItemExcosBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExcosViewHolder, position: Int) {
       val current = getItem(position)
        current?.let {
            holder.bind(current)
        }


    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Exco>() {
            override fun areItemsTheSame(oldItem: Exco, newItem: Exco): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Exco, newItem: Exco): Boolean {
                return oldItem.id == newItem.id && oldItem.image_url == newItem.image_url
            }
        }
    }


}