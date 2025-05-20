package com.example.appghichu.fragments.news

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE
import com.example.appghichu.R
import com.example.appghichu.databinding.ActivityListNewBinding
import com.example.appghichu.fragments.news.adapter.NewsAdapter
import com.example.appghichu.fragments.news.adapter.NewsInterface
import com.example.appghichu.model.Article
import com.example.appghichu.model.NewsResponse
import com.example.appghichu.network.api.RetrofitClient1
import com.example.appghichu.utils.singleClick
import retrofit2.Call

class ListNewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListNewBinding
    private var adapter: NewsAdapter? = null
    private var arr: ArrayList<Article>? = null
    private var search: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindData()
        setUpView()
    }

    private fun bindData(){
        binding.rcListNews.visibility = View.INVISIBLE
        binding.viewLoading.visibility = View.VISIBLE
        RetrofitClient1.instance1.getNewsByQuery("car", apiKey = "087aba7ae1bf4fd78d4f94d6d6f4c3b5")
            .enqueue(object : retrofit2.Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: retrofit2.Response<NewsResponse>
                ) {
                    if (response.isSuccessful) {
                        binding.rcListNews.visibility = View.VISIBLE
                        binding.viewLoading.visibility = View.GONE
                        val articles = response.body()?.articles
//                        articles?.forEach {
//                            println("Title: ${it.title}")
//                        }
                        articles?.let { adapter?.setUpList(it) }
                    } else {
                        Toast.makeText(this@ListNewActivity, "Lỗi server: ${response.code()}", Toast.LENGTH_SHORT).show()
                        binding.rcListNews.visibility = View.INVISIBLE
                        binding.viewLoading.visibility = View.VISIBLE
                        Log.d("err", "Lỗi server: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Toast.makeText(this@ListNewActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                    binding.rcListNews.visibility = View.INVISIBLE
                    binding.viewLoading.visibility = View.VISIBLE
                    Log.d("loi mang", "Lỗi mạng: ${t.message}")
                }
            })
    }

    private fun setUpView(){
        adapter = NewsAdapter(this)
        adapter?.listener = object : NewsInterface {
            override fun itemClicked(article: Article) {
//                val url = article.url
//                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
//                startActivity(intent)
                val intent = Intent(this@ListNewActivity, NewsDetailActivity::class.java)
                intent.putExtra("urlToImage", article.urlToImage)
                intent.putExtra("url", article.url)
                intent.putExtra("title", article.title)
                intent.putExtra("content", article.content)
                intent.putExtra("author", article.author)
                intent.putExtra("publishedAt", article.publishedAt)
                startActivity(intent)
            }

        }
//        binding.rcListNews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcListNews.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
            gapStrategy = GAP_HANDLING_NONE
        }
        binding.rcListNews.adapter = adapter

        binding.edtNews.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                search = s.toString()
            }

        })

        binding.edtNews.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.rcListNews.visibility = View.INVISIBLE
                binding.viewLoading.visibility = View.VISIBLE
                val query = binding.edtNews.text.toString()

                search = query

                RetrofitClient1.instance1.getNewsByQuery(search, apiKey = "087aba7ae1bf4fd78d4f94d6d6f4c3b5")
                    .enqueue(object : retrofit2.Callback<NewsResponse> {
                        override fun onResponse(
                            call: Call<NewsResponse>,
                            response: retrofit2.Response<NewsResponse>
                        ) {
                            if (response.isSuccessful) {
                                binding.rcListNews.visibility = View.VISIBLE
                                binding.viewLoading.visibility = View.GONE
                                val articles = response.body()?.articles
//                        articles?.forEach {
//                            println("Title: ${it.title}")
//                        }
                                articles?.let { adapter?.setUpList(it) }
                            } else {
                                binding.rcListNews.visibility = View.INVISIBLE
                                binding.viewLoading.visibility = View.VISIBLE
                                Log.d("err", "Lỗi server: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                            Toast.makeText(this@ListNewActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                            binding.rcListNews.visibility = View.INVISIBLE
                            binding.viewLoading.visibility = View.VISIBLE
                            Log.d("loi mang", "Lỗi mạng: ${t.message}")
                        }
                    })
                true
            } else {
                false
            }
        }

        binding.btnBack.singleClick {
            finish()
        }
    }
}