package com.example.appghichu.fragments.news

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.appghichu.R
import com.example.appghichu.databinding.ActivityNewsDetailBinding
import com.example.appghichu.utils.singleClick

class NewsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView(){
        var url = intent.getStringExtra("url")
        var img = intent.getStringExtra("urlToImage")
        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")
        var author = intent.getStringExtra("author")
        var publishedAt = intent.getStringExtra("publishedAt")

        Glide.with(this)
            .load(img)
            .thumbnail(0.1f)
            .into(binding.imgNewsDetail)

        binding.tvTitleNewsDetail.text = title

        binding.tvDesNewsDetail.text = content

        binding.tvAuthor.text = author

        binding.tvPublishedAt.text = publishedAt

        binding.btnReadMore.singleClick {
            val intent = Intent(Intent.ACTION_VIEW, url?.toUri())
            startActivity(intent)
        }

        binding.btnBack.singleClick {
            finish()
        }
    }
}