package com.example.appghichu.activities.createnote.fragment

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appghichu.R
import com.example.appghichu.activities.createnote.CreateNoteActivity
import com.example.appghichu.adapters.ChatAIAdapter
import com.example.appghichu.adapters.ChatAIAssistantItemClick
import com.example.appghichu.databinding.FragmentChatAIBinding
import com.example.appghichu.model.ResponseData
import com.example.appghichu.network.state.NoteUIState
import com.example.appghichu.utils.singleClick
import com.example.appghichu.viewmodel.NoteAIViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface AddToNoteClicked{
    fun addToNote(mess: String)
}
class ChatAIFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChatAIBinding
    private var chatAIAdapter: ChatAIAdapter? = null
    private val messages = mutableListOf<ResponseData>()
    private var noteViewModel: NoteAIViewModel? = null
    var listener: AddToNoteClicked? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this)[NoteAIViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatAIBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setDimAmount(0.6f)

//            setOnShowListener {
//                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//                bottomSheet.setBackgroundResource(android.R.color.transparent)
//
//                val displayMetrics = resources.displayMetrics
//                val height = displayMetrics.heightPixels
//
//                binding.let { _binding ->
//                    val params: ViewGroup.LayoutParams = _binding.contentView.layoutParams
//                    params.height = height
//                    _binding.contentView.requestLayout()
//                }
//            }
            setOnShowListener {
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                //bottomSheet.setBackgroundResource(android.R.color.transparent)

                // Lấy chiều cao màn hình và set 60%
                val displayMetrics = resources.displayMetrics
                val height = (displayMetrics.heightPixels * 0.6).toInt()

                // Set chiều cao cho layout root
                binding.let { _binding ->
                    val params = _binding.contentView.layoutParams
                    params.height = height
                    _binding.contentView.layoutParams = params
                    _binding.contentView.requestLayout()
                }

                // Set chiều cao cho chính bottomSheet
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = height
                bottomSheet.layoutParams = layoutParams

                // Ép trạng thái mở rộng ngay từ đầu
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = true
                behavior.peekHeight = height
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView(){
        chatAIAdapter = ChatAIAdapter(requireContext(), messages)
        binding.rcChatAi.layoutManager = LinearLayoutManager(requireContext())
        binding.rcChatAi.adapter = chatAIAdapter

        if (messages.isEmpty()) {
            val welcomeMessage = ResponseData(
                content = "Chào bạn, tôi có thể giúp gì cho bạn?",
                role = "assistant"
            )
            addMessage(welcomeMessage)
        }

        chatAIAdapter?.listener = object : ChatAIAssistantItemClick{
            override fun btnShareClicked(mess: String) {
                Log.d("shareClicked", mess)
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, mess);
                startActivity(Intent.createChooser(shareIntent,"Share"))
            }

            override fun btnAddToNoteClicked(mess: String) {
                Log.d("addToNoteClicked", mess)
                listener?.addToNote(mess)
                dismiss()
            }

        }

        binding.btnSendChat.singleClick {
            binding.loadingMess.visibility = View.VISIBLE
            val messUser = binding.edtChatAi.text.toString()
            if (messUser.isNotEmpty()) {
                // Thêm tin nhắn user vào danh sách
                addMessage(ResponseData(messUser, "user"))


                // Gọi API lấy phản hồi từ assistant
//                sendChat(messUser)

                noteViewModel?.getNoteAI(messUser)
                binding.edtChatAi.text?.clear()
            }else{
                Toast.makeText(requireContext(), "mess is empty", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            noteViewModel?.uiState?.collect { state ->
                when (state) {
                    is NoteUIState.Loading -> {
                        binding.loadingMess.visibility = View.VISIBLE
                    }

                    is NoteUIState.Success -> {
                        binding.loadingMess.visibility = View.GONE
                        addMessage(state.data) // Thêm tin nhắn của assistant
                        noteViewModel!!.resetState()
                    }

                    is NoteUIState.Error -> {
                        binding.loadingMess.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        noteViewModel!!.resetState()
                    }

                    is NoteUIState.Idle -> {
                        binding.loadingMess.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun addMessage(message: ResponseData) {
        messages.add(message)
        chatAIAdapter?.notifyItemInserted(messages.size - 1)
        binding.rcChatAi.scrollToPosition(messages.size - 1)
    }

//    private fun sendChat(mess: String){
//        noteViewModel?.getNoteAI(mess)
//
//        noteViewModel?.getNoteResponse()?.observe(requireActivity()){
//            if(it != null){
//                Log.e("status", "success")
//                addMessage(ResponseData(it.data.content, it.data.role))
//                Log.d("message", messages.toString())
//                binding.edtChatAi.isFocusable = true
//                binding.loadingMess.visibility = View.INVISIBLE
//            }else{
//                Log.e("status", "error")
//                binding.edtChatAi.isFocusable = false
//            }
//        }
//    }

    companion object {
        @JvmStatic
        fun newInstance(): ChatAIFragment{
            return ChatAIFragment()
        }
    }
}