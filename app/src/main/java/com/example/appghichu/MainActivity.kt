package com.example.appghichu

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.appghichu.db.NoteDatabase
import com.example.appghichu.model.ApiResponse
import com.example.appghichu.model.Messages
import com.example.appghichu.network.api.RetrofitClient
import com.example.appghichu.repository.NoteRepository
import com.example.appghichu.viewmodel.NoteViewModel
import com.example.appghichu.viewmodel.NoteViewModelFactory
import com.google.gson.Gson
import me.ibrahimsn.lib.SmoothBottomBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {
    private lateinit var bottomBar: SmoothBottomBar
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        val noteRepository = NoteRepository(NoteDatabase.getDatabase(this))
        val noteViewModelFactory = NoteViewModelFactory(noteRepository)
        noteViewModel = ViewModelProvider(this, noteViewModelFactory)[NoteViewModel::class.java]
        bottomBar = findViewById(R.id.bottomBar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomBar.onItemSelected = { position ->
            when (position) {
                0 -> {
                    navController.navigate(R.id.item_home)
                }
                1 -> {
                    navController.navigate(R.id.item_note_ai)
                }
                2 -> {
                    navController.navigate(R.id.item_news)
                }
                3 -> {
                    navController.navigate(R.id.item_setting)
                }
            }
        }

//        sendMessage("chào bạn")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Lời nhắc",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Kênh thông báo cho nhắc nhở"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC // Hiển thị trên màn hình khóa
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

//    private fun sendMessage(mess:String) {
//        val apiService = RetrofitClient.instance
//         val mess = listOf(Messages("user", mess))
//        // Token Authorization
//
//        val sha1 = "17:B8:E3:CE:34:ED:72:4F:44:E9:38:C2:45:83:F6:94:46:BA:FB:B0:E2:E9:38:66:75:C6:3A:4E:C6:21:6D:91"
//        val packageName = "newway.open.chatgpt.ai.chat.bot.free"
//        val keySecret = "NEWWAY-SM-HUNGMANH-CHATAI"
//
//        // 1. Tạo signature
//        val sign = generateHMACSignature(sha1, Gson().toJson(mess), packageName, keySecret)
//
//        Log.e("sign", sign)
//
//        // Tạo RequestBody cho các tham số
//        val signature = RequestBody.create("text/plain".toMediaTypeOrNull(), sign)
//        val versionApp = RequestBody.create("text/plain".toMediaTypeOrNull(), "8.2.1")
//        val service = RequestBody.create("text/plain".toMediaTypeOrNull(), "newwaylabs")
//        val model = RequestBody.create("text/plain".toMediaTypeOrNull(), "newwaylabs-1.0")
//        val app = RequestBody.create("text/plain".toMediaTypeOrNull(), "NoteAI")
//        val message = RequestBody.create("text/plain".toMediaTypeOrNull(), Gson().toJson(mess))
//
//        // Gửi file (nếu có)
////        val file = File("/path/to/file")  // Thay đường dẫn thực tế
////        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
////        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//        // Gọi API
//        apiService.sendMessage(signature, versionApp, service, model, app, message, file = null)
//            .enqueue(object : Callback<ApiResponse> {
//                override fun onResponse(p0: Call<ApiResponse>, p1: Response<ApiResponse>) {
//                    if (p1.isSuccessful) {
//                        Log.d("API_RESPONSE", "Success: ${p1.body()}")
//                    } else {
//                        Log.e("API_ERROR", "Error: ${p1.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(p0: Call<ApiResponse>, p1: Throwable) {
//                    Log.e("API_FAILURE", "Failed: ${p1.message}")
//
//                }
//            })
//    }
//    fun generateHMACSignature(sha1: String, message: String, packageName: String, secretKey: String): String {
//        val dataToSign = "$sha1&$message&$packageName" // Ghép chuỗi giống Postman
//        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
//        val mac = Mac.getInstance("HmacSHA256")
//        mac.init(secretKeySpec)
//        val bytes = mac.doFinal(dataToSign.toByteArray())
//        return bytes.joinToString("") { "%02x".format(it) } // Chuyển thành HEX String
//    }
}