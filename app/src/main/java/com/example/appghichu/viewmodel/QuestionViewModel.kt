package com.example.appghichu.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appghichu.model.ApiResponse
import com.example.appghichu.model.Question
import com.example.appghichu.network.api.ApiService
import com.example.appghichu.network.api.QuestionApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionViewModel: ViewModel() {
    private val questionApi : QuestionApiService = QuestionApiService.invoke()

    private var questionResponse: MutableLiveData<Question>? = null

    fun getQuestionResponse(): MutableLiveData<Question>?{
        if(questionResponse == null){
            questionResponse = MutableLiveData()
        }
        return questionResponse
    }

    fun getQuestion(){
        questionApi.getQuestions().enqueue(object : Callback<Question>{
            override fun onResponse(p0: Call<Question>, response: Response<Question>) {
                if(response.code() == 200){
                    response.body().let { question ->
                        questionResponse.let {
                            it?.value = question
                        }
                    }
                }else{
                    questionResponse.let {
                        it?.value = null
                    }
                }
            }

            override fun onFailure(p0: Call<Question>, p1: Throwable) {
                questionResponse.let {
                    it?.value = null
                }
            }

        })
    }
}