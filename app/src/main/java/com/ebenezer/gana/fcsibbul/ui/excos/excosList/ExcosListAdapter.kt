package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.databinding.ListItemExcosBinding

class ExcosListAdapter(private val onItemClicked:(Exco) -> Unit) : ListAdapter<Exco,
        ExcosListAdapter.ExcosViewHolder>(DiffCallback) {

    class ExcosViewHolder(private var binding: ListItemExcosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exco: Exco) {
            binding.apply {
                excoName.text = exco.name
                excoImage.setImageResource(R.drawable.img_eben)
                excoLevel.text = exco.level
                excoDepartment.text = exco.department
                excoPhone.text = exco.phone.toString()
                excoPost.text = exco.post

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
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }
        }
    }


}