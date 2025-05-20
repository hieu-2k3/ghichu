package com.example.appghichu.network.state

import com.example.appghichu.model.ResponseData

sealed class NoteUIState {
    object Loading : NoteUIState()
    data class Success(val data: ResponseData) : NoteUIState()
    data class Error(val message: String) : NoteUIState()
    object Idle : NoteUIState() // trạng thái mặc định
}