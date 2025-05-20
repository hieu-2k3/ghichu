package com.example.appghichu.activities.createnote

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appghichu.R
import com.example.appghichu.activities.createnote.fragment.AddToNoteClicked
import com.example.appghichu.activities.createnote.fragment.ChatAIFragment
import com.example.appghichu.adapters.DeleteImageInterface
import com.example.appghichu.adapters.ImageAdapter
import com.example.appghichu.databinding.ActivityCreateNoteBinding
import com.example.appghichu.databinding.BottomSheetLayoutBinding
import com.example.appghichu.db.NoteDatabase
import com.example.appghichu.model.Note
import com.example.appghichu.repository.NoteRepository
import com.example.appghichu.utils.NotificationReceiver
import com.example.appghichu.utils.singleClick
import com.example.appghichu.viewmodel.NoteViewModel
import com.example.appghichu.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    private var color: Int = -1
    //    private val noteViewModel: NoteViewModel by viewModels()
    private var note: Note? = null
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var imageAdapter: ImageAdapter

    private val imagesList = mutableListOf<ByteArray>()
    private val selectedImages = mutableListOf<String>()
    var allImages = mutableListOf<String>()

    private var selectedCalendar = Calendar.getInstance()

    private var noteAi: String? = null

    private var bottomSheetChatAI: ChatAIFragment? = null

    private val pickImagesLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val newImagePaths = uris.mapNotNull { getRealPathFromURI(it) }  // Chuyển Uri thành String
            Log.d("ImagePaths", newImagePaths.toString()) // Kiểm tra xem có đường dẫn nào không

            selectedImages.addAll(newImagePaths) // Thêm ảnh mới vào danh sách (không xóa ảnh cũ)

            allImages = ((note?.imagePaths ?: emptyList()) + selectedImages).toMutableList()

            if (!::imageAdapter.isInitialized) {
                setupRecyclerView(allImages) // Đảm bảo adapter đã gán vào RecyclerView
            } else {
                imageAdapter.updateImages(allImages) // Cập nhật RecyclerView
            }

            binding.rcImg.smoothScrollToPosition(imageAdapter.itemCount - 1)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NoteRepository(NoteDatabase.getDatabase(this)) // Khởi tạo Repository
        val factory = NoteViewModelFactory(repository) // Khởi tạo ViewModelFactory



        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java] // Sử dụng ViewModelProvider

        setUpNote()

        binding.btnSaveNote.singleClick {
            saveNote()
        }

        binding.btnChooseColor.singleClick {
            val bottomSheet = BottomSheetDialog(this)
            val bottomSheetView: View = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            with(bottomSheet){
                setContentView(bottomSheetView)
                show()
            }
            val bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                colorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener { value ->
                        color = value
                        binding.main.setBackgroundColor(color)
                    }
                }
            }
            bottomSheetView.post {
                bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.btnChatAi.singleClick {
            bottomSheetChatAI = ChatAIFragment.newInstance()
            bottomSheetChatAI?.show(supportFragmentManager, "bottomSheetChatAI")
            bottomSheetChatAI?.listener = object : AddToNoteClicked{
                override fun addToNote(mess: String) {
                    val currentText = binding.edtContent.text.toString()
                    binding.edtContent.renderMD(currentText + mess)
                }

            }
        }

        binding.btnChooseImg.singleClick {
            if (hasStoragePermission()) {
                openGallery() // Nếu đã có quyền thì mở thư viện ảnh
            } else {
                requestStoragePermission() // Nếu chưa có quyền thì yêu cầu quyền
            }
        }

        binding.btnCalendarReminder.singleClick {
            showDateTimePicker()
        }

        binding.btnBackCreateNote.singleClick {
            finish()
        }

        binding.edtContent.setOnFocusChangeListener { _, isFocus ->
            if(isFocus){
                binding.styleBar.visibility = View.VISIBLE
                binding.edtContent.setStylesBar(binding.styleBar)
            }else{
                binding.styleBar.visibility = View.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập ảnh đã được cấp", Toast.LENGTH_SHORT).show()
                openGallery() // Nếu được cấp quyền, mở thư viện ảnh
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun openGallery() {
        pickImagesLauncher.launch("image/*") // Chọn nhiều ảnh
    }

    private fun setUpNote(){
        note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("note_item", Note::class.java)
        } else {
            intent.getSerializableExtra("note_item") as? Note
        }
        noteAi = intent.getStringExtra("note_ai")
        if(note == null){
//            binding.btnSaveNote.text = "saved note"
            allImages = mutableListOf()
            if (noteAi != null) {
                binding.edtContent.renderMD(noteAi!!)
            }
        }else{
            color = note!!.color!!
//            binding.btnSaveNote.text = "update note"
            binding.edtTitle.setText(note!!.title)
            binding.edtContent.renderMD(note!!.content!!)
            note!!.color?.let { binding.main.setBackgroundColor(it) }
            allImages = note!!.imagePaths!!.toMutableList()
            setupRecyclerView(note!!.imagePaths)
        }

    }

    private fun setupRecyclerView(images: List<String>?){
        imageAdapter = ImageAdapter(images)
        imageAdapter.listener = object : DeleteImageInterface{
            override fun deleteImage(img: String) {
                allImages = ((note?.imagePaths ?: emptyList()) + selectedImages).toMutableList()
                allImages.remove(img)
                imageAdapter.updateImages(allImages)
            }

        }
        binding.rcImg.adapter = imageAdapter
        binding.rcImg.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val file = File(cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        return try {
            contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap, fileName: String, context: Context): String {
        val file = File(context.filesDir, "$fileName.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return file.absolutePath
    }

    private fun saveNote(){
        if(binding.edtTitle.text.toString().isEmpty() || binding.edtContent.text.toString().isEmpty()){
            Toast.makeText(this, "Something is Empty", Toast.LENGTH_SHORT).show()
        }else{
            if(note == null){
                Log.d("empty", "empty")
                noteViewModel.saveNote(Note(0, binding.edtTitle.text.toString(), binding.edtContent.text.toString(), getCurrentTime(), color, selectedImages))
                this.finish()
            }else{
                Log.d("id", note!!.id.toString())
                noteViewModel.updateNote(Note(
                    note!!.id,
                    binding.edtTitle.text.toString(),
                    binding.edtContent.text.toString(),
                    note!!.date,
                    color,
                    allImages
                    //(note!!.imagePaths?.plus(allImages))?.distinct()?.toMutableList() // Giữ lại ảnh cũ + ảnh mới
                ))
                this.finish()
            }
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        // Chọn ngày
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)

            // Chọn giờ
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                selectedCalendar = calendar

                // Đặt thông báo
                scheduleNotification(calendar.timeInMillis)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(triggerTime: Long) {
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("title", "Nhắc nhở!")
            putExtra("message", "Đã đến thời gian bạn đã đặt.")
            putExtra("note", note)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

        Toast.makeText(this, "Thông báo đã được hẹn giờ!", Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}