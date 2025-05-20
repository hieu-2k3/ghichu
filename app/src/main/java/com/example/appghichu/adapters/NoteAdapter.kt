package com.example.appghichu.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appghichu.databinding.NoteItemLayoutBinding
import com.example.appghichu.model.Note
import com.example.appghichu.utils.singleClick
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import org.commonmark.node.SoftLineBreak

interface NoteInterface{
    fun clickItemNote(note: Note)
    fun longClickItemNote(note: Note)
}

class NoteAdapter: ListAdapter<Note, NoteAdapter.ItemViewHolder>(DiffCallback()) {
    var listener: NoteInterface? = null

    inner class ItemViewHolder(var binding: NoteItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun setContentView(note: Note){
            binding.tvTitle.text = note.title
//            binding.tvContent.text = note.content
            val markWon = Markwon.builder(itemView.context)
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TaskListPlugin.create(itemView.context))
                .usePlugin(object : AbstractMarkwonPlugin(){
                    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                        super.configureVisitor(builder)
                        builder.on(SoftLineBreak::class.java){ visitor, _, ->
                            visitor.forceNewLine()
                        }
                    }
                })
                .build()
            markWon.setMarkdown(binding.tvContent, note.content!!)
//            note.color?.let { binding.viewParent.setBackgroundColor(it) }
            binding.viewParent.setCardBackgroundColor(note.color!!)
            if(note.imagePaths!!.isNotEmpty()){
                binding.imgNote.visibility = View.VISIBLE
                val bitmap = BitmapFactory.decodeFile(note.imagePaths[0])
                binding.imgNote.setImageBitmap(bitmap)
            }else{
                binding.imgNote.visibility = View.GONE
            }

            binding.tvDate.text = note.date

            binding.btnDeleteNote.singleClick {
                listener?.longClickItemNote(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = NoteItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setContentView(getItem(position))
        holder.itemView.singleClick {
            listener?.clickItemNote(getItem(position))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}