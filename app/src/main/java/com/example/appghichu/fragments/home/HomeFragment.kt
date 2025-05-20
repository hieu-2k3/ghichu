package com.example.appghichu.fragments.home

import android.content.Context
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
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appghichu.R
import com.example.appghichu.activities.createnote.CreateNoteActivity
import com.example.appghichu.adapters.NoteAdapter
import com.example.appghichu.adapters.NoteInterface
import com.example.appghichu.databinding.FragmentHomeBinding
import com.example.appghichu.fragments.home.dialog.DeleteNoteItemInterface
import com.example.appghichu.fragments.home.dialog.DialogDeleteNoteItemFragment
import com.example.appghichu.model.Note
import com.example.appghichu.utils.hideKeyboard
import com.example.appghichu.utils.singleClick
import com.example.appghichu.viewmodel.NoteViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var noteAdapter: NoteAdapter? = null
    private val noteViewModel: NoteViewModel by activityViewModels()
    private var dialogDeleteNoteItem: DialogDeleteNoteItemFragment? = null

    private var activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
        result.let {
            if (result != null) {
                Log.d("Result", "Result Code : ${result.resultCode}")

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressView.visibility = View.VISIBLE
        setUpNoteAdapter()
        lifecycleScope.launch {
            delay(1000)
            if(isAdded && view != null){
                getData()
                binding.progressView.visibility = View.GONE
            }
        }

        binding.btnAddNote.singleClick {
            val intent = Intent(requireContext(), CreateNoteActivity::class.java)
            startActivity(intent)
        }

        binding.edtSearchNote.addTextChangedListener(object : TextWatcher {
            private var job: Job? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.viewNotesEmpty.isVisible = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(300) // Đợi 300ms để tránh spam query
                    val text = p0.toString().trim()
                    if (text.isNotEmpty()) {
                        noteViewModel.searchNote("%$text%").observe(viewLifecycleOwner) {
                            noteAdapter?.submitList(it)
                        }
                    } else {
                        getData()
                    }
                }
//                if(p0.toString().isNotEmpty()){
//                    val text = p0.toString()
//                    val query = "%$text%"
//                    if(query.isNotEmpty()){
//                        noteViewModel.searchNote(query).observe(viewLifecycleOwner){
//                            noteAdapter?.setContentView(it)
//                        }
//                    }else{
//                        getData()
//                    }
//                }else{
//                    getData()
//                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.edtSearchNote.setOnEditorActionListener { v, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                v.clearFocus()
                requireView().hideKeyboard()
            }
            return@setOnEditorActionListener true
        }
    }

    private fun getData(){
        if(isAdded && view != null){
            noteViewModel.getAllNote().observe(viewLifecycleOwner){list ->
                binding.viewNotesEmpty.isVisible = list.isEmpty()
                noteAdapter?.submitList(list)
            }
        }

    }

    private fun setUpNoteAdapter(){
        noteAdapter = NoteAdapter()
//        getData()
        noteAdapter!!.listener = object : NoteInterface {
            override fun clickItemNote(note: Note) {
                val intent = Intent(requireContext(), CreateNoteActivity::class.java)
                intent.putExtra("note_item", note) // Đảm bảo note không null trước khi truyền
                startActivity(intent)
                Animatoo.animateZoom(requireContext())
            }

            override fun longClickItemNote(note: Note) {
                dialogDeleteNoteItem = DialogDeleteNoteItemFragment.newInstance(object : DeleteNoteItemInterface{
                    override fun clickDelete() {
                        noteViewModel.deleteNote(note)
                        dialogDeleteNoteItem?.dismiss()
                    }

                    override fun clickCancel() {
                        dialogDeleteNoteItem?.dismiss()
                    }

                })
                dialogDeleteNoteItem?.showDialog(requireActivity().supportFragmentManager)
            }

        }
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNotes.adapter = noteAdapter

    }

    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }
}