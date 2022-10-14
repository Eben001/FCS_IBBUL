package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.databinding.ListItemExcosBinding

class ExcosListAdapter(private val context: Context, private val onItemClicked:(Exco) -> Unit) : ListAdapter<Exco,
        ExcosListAdapter.ExcosViewHolder>(DiffCallback) {

    inner class ExcosViewHolder(private var binding: ListItemExcosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(excos: Exco) {
            binding.apply {
                binding.apply {
                    excoName.text = context.resources.getString(R.string.first_name_last_name, excos.firstName,excos.lastName)
                    excoPhone.text = excos.phone
                    excoPost.text = excos.office
                    excoDepartment.text = excos.department
                    excoLevel.text = excos.level
                    excoImage.setImageResource(R.drawable.img_eben)
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
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
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