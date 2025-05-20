package com.example.appghichu.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appghichu.model.ApiResponse
import com.example.appghichu.model.Messages
import com.example.appghichu.model.ResponseData
import com.example.appghichu.network.api.ApiService
import com.example.appghichu.network.api.RetrofitClient
import com.example.appghichu.network.state.NoteUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class NoteAIViewModel: ViewModel() {
    private val noteApi : ApiService = ApiService.invoke()

    private var noteAIResponse: MutableLiveData<ApiResponse>? = null

    private val _uiState = MutableStateFlow<NoteUIState>(NoteUIState.Idle)
    val uiState: StateFlow<NoteUIState> = _uiState

    fun getNoteResponse(): MutableLiveData<ApiResponse>?{
        if(noteAIResponse == null){
            noteAIResponse = MutableLiveData()
        }
        return noteAIResponse
    }

    fun getNoteAI(mess:String){
        val mess = listOf(Messages("user", mess))
        // Token Authorization

        val sha1 = ""
        val packageName = "app"
        val keySecret = "app"

        // 1. Tạo signature
        val sign = generateHMACSignature(sha1, Gson().toJson(mess), packageName, keySecret)

        Log.e("sign", sign)

        // Tạo RequestBody cho các tham số
        val signature = RequestBody.create("text/plain".toMediaTypeOrNull(), sign)
        val versionApp = RequestBody.create("text/plain".toMediaTypeOrNull(), "8.2.1")
        val service = RequestBody.create("text/plain".toMediaTypeOrNull(), "newwaylabs")
        val model = RequestBody.create("text/plain".toMediaTypeOrNull(), "newwaylabs-1.0")
        val app = RequestBody.create("text/plain".toMediaTypeOrNull(), "NoteAI")
        val message = RequestBody.create("text/plain".toMediaTypeOrNull(), Gson().toJson(mess))
        noteApi.sendMessage(signature, versionApp, service, model, app, message, file = null)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(p0: Call<ApiResponse>, response: Response<ApiResponse>) {
//                    if(response.code() == 200){
//                        response.body().let { _noteAI ->
//                            noteAIResponse.let {
//                                it?.value = _noteAI
//                            }
//                        }
//                    }else{
//                        noteAIResponse.let {
//                            it?.value = null
//                        }
//                    }
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!.data
                        _uiState.value = NoteUIState.Success(ResponseData(data.content, data.role))
                    } else {
                        _uiState.value = NoteUIState.Error("Lỗi khi lấy dữ liệu từ server")
                    }
                }

                override fun onFailure(p0: Call<ApiResponse>, p1: Throwable) {
//                    noteAIResponse.let {
//                        it?.value = null
//                    }
                    _uiState.value = NoteUIState.Error(p1.message ?: "Lỗi kết nối")
                }
            })
    }

    fun resetState() {
        _uiState.value = NoteUIState.Idle
    }

    private fun generateHMACSignature(sha1: String, message: String, packageName: String, secretKey: String): String {
        val dataToSign = "$sha1&$message&$packageName" // Ghép chuỗi giống Postman
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val bytes = mac.doFinal(dataToSign.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) } // Chuyển thành HEX String
    }
}