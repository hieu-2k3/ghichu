package com.example.appghichu.fragments.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.appghichu.R
import com.example.appghichu.activities.createnote.CreateNoteActivity
import com.example.appghichu.configs.Config
import com.example.appghichu.databinding.FragmentSettingBinding
import com.example.appghichu.utils.singleClick
import java.io.File


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendFeedback.singleClick {
            val intent = Intent(requireContext(), CreateNoteActivity::class.java)
            startActivity(intent)
        }

        binding.btnShareApp.singleClick {
            shareApk()
        }

        binding.btnTerm.singleClick {
            openUrl(requireContext(), Config.url_terms)
        }

        binding.btnPolicy.singleClick {
            openUrl(requireContext(), Config.url_privacy)
        }
    }

    private fun shareApk() {
        val context = requireContext()
        val sourceApkPath = context.applicationInfo.sourceDir
        val sourceFile = File(sourceApkPath)

        // Sao chép APK vào thư mục cache để chia sẻ
        val destFile = File(context.externalCacheDir, "my_app.apk")
        sourceFile.copyTo(destFile, overwrite = true)

        // Lấy URI từ FileProvider
        val apkUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            destFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.android.package-archive"
            putExtra(Intent.EXTRA_STREAM, apkUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Chia sẻ ứng dụng qua"))
    }

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }
}