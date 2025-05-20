package com.example.appghichu.activities.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.appghichu.MainActivity
import com.example.appghichu.R
import com.example.appghichu.activities.intro.adapter.IntroAdapter
import com.example.appghichu.activities.intro.adapter.IntroItem
import com.example.appghichu.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private var adapter: IntroAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_intro)

        val onboardingItems = listOf(
            IntroItem(
                title = "TRY TATTOOS\nON YOUR SKIN",
                subtitle = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            IntroItem(
                title = "CREATE UNIQUE\nTATTOOS WITH AI",
                subtitle = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            IntroItem(
                title = "TURN YOUR WORDS INTO\nSTUNNING TATOO",
                subtitle = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            IntroItem(
                title = "DO YOU LIKE US MUCH\nAS WE LIKE YOU?",
                subtitle = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            )
        )

        adapter = IntroAdapter(onboardingItems)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)

//        binding.viewPager.isUserInputEnabled = false

        binding.btnContinue.setOnClickListener {
            if (binding.viewPager.currentItem < adapter!!.itemCount - 1) {
                binding.viewPager.currentItem += 1
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        binding.tvTitle.text = "TRY TATTOOS\nON YOUR SKIN"
                        binding.tvDes.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                    }
                    1 -> {
                        binding.tvTitle.text = "CREATE UNIQUE\nTATTOOS WITH AI"
                        binding.tvDes.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                    }
                    2 -> {
                        binding.tvTitle.text = "TURN YOUR WORDS INTO\nSTUNNING TATOO"
                        binding.tvDes.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                    }
                    3 -> {
                        binding.tvTitle.text = "DO YOU LIKE US MUCH\nAS WE LIKE YOU?"
                        binding.tvDes.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                    }
                }
            }
        })
    }
}