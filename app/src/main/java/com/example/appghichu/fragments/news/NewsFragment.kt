package com.example.appghichu.fragments.news

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appghichu.R
import com.example.appghichu.databinding.FragmentNewsBinding
import com.example.appghichu.utils.singleClick


class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAppleNews.singleClick {
            startActivity(Intent(requireActivity(), ListNewActivity::class.java))
        }

        binding.btnTeslaNews.singleClick {
            startActivity(Intent(requireActivity(), WeatherActivity::class.java))
        }
    }
}