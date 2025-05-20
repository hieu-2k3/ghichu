package com.example.appghichu.fragments.noteai

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appghichu.R
import com.example.appghichu.activities.createnote.CreateNoteActivity
import com.example.appghichu.adapters.QuestionAdapter
import com.example.appghichu.adapters.QuestionInterface
import com.example.appghichu.databinding.FragmentHomeBinding
import com.example.appghichu.databinding.FragmentNoteAIBinding
import com.example.appghichu.manager.ads.AdsManager
import com.example.appghichu.manager.ads.RewardStatus
import com.example.appghichu.model.Messages
import com.example.appghichu.model.QuestionItem
import com.example.appghichu.network.state.NoteUIState
import com.example.appghichu.utils.singleClick
import com.example.appghichu.viewmodel.CommandAIFragmentViewModel
import com.example.appghichu.viewmodel.NoteAIViewModel
import com.example.appghichu.viewmodel.QuestionViewModel


class NoteAIFragment : Fragment() {

    private lateinit var binding: FragmentNoteAIBinding
    var adsManager : AdsManager? = null
    private var rewardStatus:String = "WATCHED"
    private var noteViewModel: NoteAIViewModel? = null
    private var questionViewModel: QuestionViewModel? = null
//    private var textNote: String? = null
    private var questionAdapter: QuestionAdapter? = null
    private var questions = arrayListOf<QuestionItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this)[NoteAIViewModel::class.java]
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        initAds()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteAIBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()

        getQuestion()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            noteViewModel?.uiState?.collect { state ->
                when (state) {
                    is NoteUIState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }
                    is NoteUIState.Success -> {
                        binding.loading.visibility = View.INVISIBLE
                        val intent = Intent(requireActivity(), CreateNoteActivity::class.java)
                        intent.putExtra("note_ai", state.data.content)
                        startActivity(intent)
                        Animatoo.animateZoom(requireContext())
                        noteViewModel!!.resetState()
                    }
                    is NoteUIState.Error -> {
                        binding.loading.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        noteViewModel!!.resetState()
                    }
                    is NoteUIState.Idle -> {
                        binding.loading.visibility = View.INVISIBLE
                    }
                }
            }
        }

        binding.edtNoteAi.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.btnDeleteNoteAi.singleClick {
            binding.edtNoteAi.setText("")
        }

        binding.btnCreateNoteAi.singleClick {
//            showAds()
            var textNote = binding.edtNoteAi.text.toString()

            if(textNote.isNotEmpty()){
                noteViewModel?.getNoteAI(textNote)
                binding.edtNoteAi.text?.clear()
            }else{
                Toast.makeText(requireContext(), "prompt is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getQuestion(){
        if(isAdded && view != null){
//            questionViewModel?.getQuestion()
//
//            questionViewModel?.getQuestionResponse()?.observe(viewLifecycleOwner){
//                if(it != null){
//                    Log.d("data", it.toString())
//                    binding.loading.visibility = View.INVISIBLE
//                    questionAdapter?.setUpArr(it.results)
//                }else{
//                    Log.d("data", "empty")
//                    binding.loading.visibility = View.VISIBLE
//                }
//            }
            val newArr = arrayListOf(
                QuestionItem("", "", "", "What is the difference between mitosis and meiosis?", ""),
                QuestionItem("", "", "", "Can you explain Newton's three laws of motion?", ""),
                QuestionItem("", "", "", "How do you write a persuasive essay?", ""),
                QuestionItem("", "", "", "What are some effective ways to learn a new language?", ""),
                QuestionItem("", "", "", "What is the Pythagorean theorem and how is it used?", ""),
                QuestionItem("", "", "", "What are some unique traditions in Japanese culture?", ""),
                QuestionItem("", "", "", "What is the history behind Halloween?", ""),
                QuestionItem("", "", "", "How does climate change affect global ecosystems?", ""),
                QuestionItem("", "", "", "What is the difference between socialism and capitalism?", ""),
                QuestionItem("", "", "", "Why is learning about other cultures important?", ""),
                QuestionItem("", "", "", "How do I write a professional resume?", ""),
                QuestionItem("", "", "", "What are common interview questions and how should I answer them?", ""),
                QuestionItem("", "", "", "How can I become a better public speaker?", ""),
                QuestionItem("", "", "", "What are some time management tips for students or professionals?", ""),
                QuestionItem("", "", "", "How do I start a small business?", ""),
                QuestionItem("", "", "", "How does artificial intelligence work?", ""),
                QuestionItem("", "", "", "What are the benefits of using cloud computing?", ""),
                QuestionItem("", "", "", "Can you explain the basics of blockchain technology?", ""),
                QuestionItem("", "", "", "What programming languages should I learn in 2025?", ""),
                QuestionItem("", "", "", "How can I protect my personal data online?", "")
            )
            questionAdapter?.setUpArr(newArr)
        }
    }

    private fun setUpAdapter(){
        questionAdapter = QuestionAdapter(questions)
        questionAdapter?.listener = object : QuestionInterface{
            override fun itemClicked(question: String) {
                binding.edtNoteAi.setText(question)
            }

        }
        binding.rcQuestion.layoutManager = LinearLayoutManager(requireContext())
        binding.rcQuestion.adapter = questionAdapter
    }

    private fun initAds(){
        adsManager = AdsManager()
        adsManager?.preload(requireActivity())
    }

    private fun showAds(){
        var isWatch = false
        adsManager?.showReward(requireActivity()) { status ->
            when (status){
                RewardStatus.EARNED ->{
                    isWatch = true
                    rewardStatus = "WATCHED"
                }
                RewardStatus.DISMISS ->{
                    rewardStatus = if (isWatch){
                        "WATCHED"
                    }else{
                        "DISMISS"
                    }
                    Log.e("RewardStatus", "RewardStatus - $rewardStatus")
                    binding.loading.visibility = View.VISIBLE

                    var textNote = binding.edtNoteAi.text.toString()

                    if(textNote.isNotEmpty()){
                        noteViewModel?.getNoteAI(textNote)
                        binding.edtNoteAi.text?.clear()
                    }else{
                        Toast.makeText(requireContext(), "prompt is empty", Toast.LENGTH_SHORT).show()
                    }
                }
                RewardStatus.FAILED ->{
                    rewardStatus = "WATCHED"
                    Log.e("RewardStatus","RewardStatus - $rewardStatus")
                }
            }
        }
    }
}