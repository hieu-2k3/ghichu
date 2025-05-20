package com.example.appghichu.activities.intro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appghichu.databinding.ItemIntroBinding

data class IntroItem(
    val title: String,
    val subtitle: String,
    val imageResId: Int? = null
) {
}

class IntroAdapter(private val items: List<IntroItem>): RecyclerView.Adapter<IntroAdapter.ItemHolder>() {
    inner class ItemHolder(private var binding: ItemIntroBinding): RecyclerView.ViewHolder(binding.root){
//        fun bind(item: IntroItem) {
//            binding.imgItemIntro.setImageResource(item.imageResId)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIntroBinding.inflate(inflater, parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
//        holder.bind(items[position])
    }
}