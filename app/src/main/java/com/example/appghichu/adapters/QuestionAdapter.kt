package com.example.appghichu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appghichu.databinding.ItemQuestionBinding
import com.example.appghichu.databinding.NoteItemLayoutBinding
import com.example.appghichu.model.Question
import com.example.appghichu.model.QuestionItem
import com.example.appghichu.utils.singleClick

interface QuestionInterface{
    fun itemClicked(question: String)
}
class QuestionAdapter(private var arr: ArrayList<QuestionItem>): RecyclerView.Adapter<QuestionAdapter.ItemHolder>() {
    var listener: QuestionInterface? = null
    inner class ItemHolder(var binding: ItemQuestionBinding): RecyclerView.ViewHolder(binding.root){
        fun setUpContentView(question: QuestionItem){
            binding.tvQuestion.text = question.question
            itemView.singleClick {
                listener?.itemClicked(question.question)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val question = arr[position]
        holder.setUpContentView(question)
    }

    fun setUpArr(newArr: ArrayList<QuestionItem>){
        arr = newArr
        notifyDataSetChanged()
    }
}