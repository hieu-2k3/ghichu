package com.example.appghichu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appghichu.databinding.ItemChatAssistantBinding
import com.example.appghichu.databinding.ItemChatUserBinding
import com.example.appghichu.model.ResponseData
import com.example.appghichu.utils.singleClick
import io.noties.markwon.Markwon

interface ChatAIAssistantItemClick{
    fun btnShareClicked(mess: String)
    fun btnAddToNoteClicked(mess: String)
}
class ChatAIAdapter(private val context: Context, private var messages: List<ResponseData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: ChatAIAssistantItemClick? = null
    private var markwon: Markwon? = null
    class ChatUserItemHolder(private var binding: ItemChatUserBinding): RecyclerView.ViewHolder(binding.root){
        fun setContentView(mess: ResponseData){
            binding.tvChatUser.text = mess.content
        }
    }
    inner class ChatAssistantItemHolder(private var binding: ItemChatAssistantBinding): RecyclerView.ViewHolder(binding.root){
        fun setContentView(mess: ResponseData){
            markwon = Markwon.create(context)
            binding.tvChatAssistant.let {
                markwon?.setMarkdown(it, mess.content)
            }
            binding.btnShare.singleClick {
                listener?.btnShareClicked(mess.content)
            }
            binding.btnAddToNote.singleClick {
                listener?.btnAddToNoteClicked(mess.content)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].role == "user") 0 else 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0){
            val binding = ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatUserItemHolder(binding)
        }else{
            val binding = ItemChatAssistantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatAssistantItemHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is ChatUserItemHolder) {
            holder.setContentView(message)
        } else if (holder is ChatAssistantItemHolder) {
            holder.setContentView(message)
        }
    }

    fun updateMess(newMess: List<ResponseData>) {
        messages = newMess // Gán lại danh sách mới
        notifyDataSetChanged()
    }
}