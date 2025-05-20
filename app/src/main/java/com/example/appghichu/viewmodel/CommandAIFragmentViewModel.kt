package com.example.appghichu.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appghichu.NoteApplication
import com.example.appghichu.model.BodyRequestV2
import com.example.appghichu.model.DirectChat
import com.example.appghichu.model.ErrorMessage
import com.example.appghichu.model.Messages
import com.example.appghichu.network.api.FTApi
import com.example.appghichu.repository.FTRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale

open class CommandAIFragmentViewModel(val repository: FTRepository = FTRepository(FTApi())): ViewModel() {

    var content: String = ""

    private val _summaryResult = MutableLiveData<Response<DirectChat>>()
    val summaryResult: LiveData<Response<DirectChat>> get() = _summaryResult
    private suspend fun summary(
        messages: List<Messages>,
        path:String
    ): Response<DirectChat> {
        val appName = "NoteAI"
//        val signature = NoteApplication.appInstance.getSignature(Gson().toJson(messages))
        val signature = "b7c2abe15be55e0135e42c310c11c6711312767c964e09e277bc934372129b99"
        Log.e("TAG","signature=${signature}")
        val json = BodyRequestV2(
            signature = signature,
            model = getModel(),
            message = messages,
            service = getService(),
            app = appName
        )

        Log.e("signature","signature=${signature}")
        Log.e("model","model=${getModel()}")
        Log.e("message","message=${messages}")
        Log.e("service","service=${getService()}")
        Log.e("app","app=${appName}")


        return repository.noteChat(path, json)

    }

    fun getSummary(messages: List<Messages>, path: String) {
        viewModelScope.launch {
            try {
                val response = summary(messages, path)
                _summaryResult.postValue(response)
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun getModel():String{
        return "newwaylabs-1.0"
    }

    private fun getService():String{
        return "newwaylabs"
    }
}