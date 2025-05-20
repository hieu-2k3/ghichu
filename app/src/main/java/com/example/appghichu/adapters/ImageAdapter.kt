package com.example.appghichu.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.appghichu.R
import com.example.appghichu.utils.singleClick

interface DeleteImageInterface{
    fun deleteImage(img: String)
}
class ImageAdapter(private var imagePaths: List<String>?) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
                       var listener: DeleteImageInterface? = null
    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val btnRemove: ImageButton = view.findViewById(R.id.btnCloseImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imagePaths?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val bitmap = BitmapFactory.decodeFile(imagePaths?.get(position) ?: "")
        holder.imageView.setImageBitmap(bitmap)
        holder.btnRemove.singleClick {
            listener?.deleteImage(imagePaths!![position])
        }
    }

    fun updateImages(newImages: List<String>) {
        imagePaths = newImages.toMutableList() // Gán lại danh sách mới
        notifyDataSetChanged()
    }
}