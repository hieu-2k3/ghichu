package com.example.appghichu.fragments.home.dialog

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.appghichu.R
import com.example.appghichu.databinding.FragmentDialogDeleteNoteItemBinding
import com.example.appghichu.utils.screenSize
import com.example.appghichu.utils.singleClick


interface DeleteNoteItemInterface{
    fun clickDelete()
    fun clickCancel()
}
class DialogDeleteNoteItemFragment : DialogFragment() {
    private var binding: FragmentDialogDeleteNoteItemBinding? = null
    var mListener: DeleteNoteItemInterface? = null
    var mActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialogDeleteNoteItemBinding.inflate(inflater, container, false)
        binding?.btnClose?.singleClick {
            hide()
        }

        binding?.btnDelete?.singleClick {
            mListener?.clickDelete()
        }

        binding?.btnCancel?.singleClick {
            hide()
            mListener?.clickCancel()
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpContentView()
    }

    private fun setUpContentView(){

        if (activity != null){
            activity?.screenSize()?.let { scSize ->
                val newLayoutParams = ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (scSize.heightPixels * 1.0).toInt()
                )

                binding?.contentView?.layoutParams = newLayoutParams
            }
        }
    }

    private fun allowShow(fragmentManager: FragmentManager) : Boolean {
        return (!isAdded && !fragmentManager.isDestroyed && !fragmentManager.isStateSaved && dialog == null && !isVisible)
    }

    fun showDialog(fragmentManager: FragmentManager) {
        if (allowShow(fragmentManager)){
            this.show(fragmentManager, "DialogCreateImageSuccess")
        }
    }

    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }

    open fun hide(){
        if (isAdded){
            dismissAllowingStateLoss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(lis: DeleteNoteItemInterface): DialogDeleteNoteItemFragment{
            val dialog = DialogDeleteNoteItemFragment()
            dialog.mListener = lis
            return dialog
        }
    }
}