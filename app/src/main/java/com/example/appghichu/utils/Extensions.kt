package com.example.appghichu.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.hardware.display.DisplayManagerCompat

fun View.singleClick(debounceTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.screenSize() : DisplayMetrics {

    var displayMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        DisplayManagerCompat.getInstance(this).getDisplay(Display.DEFAULT_DISPLAY)?.let {
            val displayContext = createDisplayContext(it)
            val widthDevice = displayContext.resources.displayMetrics.widthPixels
            val heightDevice = displayContext.resources.displayMetrics.heightPixels

//            Logger.e("Screen Width : $widthDevice")
//            Logger.e("Screen Height : $heightDevice")
//            stageHeight = heightDevice
            displayMetrics = displayContext.resources.displayMetrics
        }
    } else {
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthDevice = displayMetrics.widthPixels
        val heightDevice = displayMetrics.heightPixels
//        stageHeight = heightDevice
        displayMetrics = displayMetrics
    }

    return displayMetrics
}