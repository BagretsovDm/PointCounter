package com.example.foolcounter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.foolcounter.databinding.ItemViewBinding
import com.example.foolcounter.model.Player

class PlayerAdapter(val onButtonsClicked: (player: Player, addPoint: Boolean) -> Unit) :
    ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = PlayerViewHolder(binding)

        binding.addPoint.setOnClickListener {
            onButtonsClicked(getItem(holder.adapterPosition), true)
        }

        binding.removePoint.setOnClickListener {
            onButtonsClicked(getItem(holder.adapterPosition), false)
        }

        return holder
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlayerViewHolder(private val binding: ItemViewBinding) : ViewHolder(binding.root) {
        fun bind(player: Player) {
            with(binding) {
                nameField.text = player.name
                counter.text = player.count.toString()
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Player>() {
            override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem == newItem
            }
        }
    }
}
