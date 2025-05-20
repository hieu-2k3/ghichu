package com.example.appghichu

import android.app.Application
import android.content.Context
import com.example.appghichu.manager.ads.AppOpenManager

class NoteApplication: Application() {
    private var appOpenManager: AppOpenManager? = null
//    companion object {
//        lateinit var appInstance: NoteApplication
//            private set
//
//        val appContext: Context by lazy {
//            appInstance.applicationContext
//        }
//        init {
//            System.loadLibrary("libs")
//        }
//    }
//    external fun getSignature(string:String): String
    override fun onCreate() {
        super.onCreate()
        appOpenManager = AppOpenManager(this)
    }

}
