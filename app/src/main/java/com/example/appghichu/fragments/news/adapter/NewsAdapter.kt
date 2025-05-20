package com.example.appghichu.fragments.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appghichu.R
import com.example.appghichu.databinding.ItemNewsBinding
import com.example.appghichu.model.Article
import com.example.appghichu.utils.singleClick

interface NewsInterface{
    fun itemClicked(article: Article)
}

class NewsAdapter(private var context: Context) : RecyclerView.Adapter<NewsAdapter.itemHolder>() {
    private var arr = arrayListOf<Article>()
    var listener: NewsInterface? = null
    inner class itemHolder(private var binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root){
        fun setUpView(article: Article){
            Glide.with(context)
                .load(article.urlToImage)
                .placeholder(R.drawable.ic_placeholder_tattooo)
                .into(binding.imgStorage)

            binding.tvTitle.text = article.title

            binding.rootView.singleClick {
                listener?.itemClicked(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return itemHolder(binding)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: itemHolder, position: Int) {
        val article = arr[position]
        holder.setUpView(article)
    }

    fun setUpList(newArr: List<Article>){
        arr = newArr as ArrayList<Article>
        notifyDataSetChanged()
    }
}