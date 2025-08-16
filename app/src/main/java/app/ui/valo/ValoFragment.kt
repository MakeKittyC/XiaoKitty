package app.ui.valo

import android.Manifest
import android.os.Bundle
import android.os.Build
import android.os.Process
import android.os.IBinder
import android.os.RemoteException
import android.os.Environment

import android.text.TextUtils

import android.net.Uri

import android.util.Log

import android.app.PendingIntent

import android.provider.MediaStore

import android.database.Cursor

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import android.widget.Toast

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.Intent
import android.content.Context
import android.content.ContentResolver
import android.content.SharedPreferences

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager

import android.system.Os
import android.system.OsConstants

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.compile.databinding.FragmentValoBinding
import app.compile.R
import app.compile.BuildConfig
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher

import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.DataOutputStream

import com.google.android.material.snackbar.Snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton

import androidx.constraintlayout.widget.ConstraintLayout

import app.push.NotificationReceiver

public class ValoFragment : Fragment() {

companion object {
    init {
        System.loadLibrary("NonNull")
    }
    
}
    private var _binding: FragmentValoBinding? = null
    private val binding get() = _binding!!
    
    private var androidxConstraintLayout: ConstraintLayout? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ValoViewModel =
            ViewModelProvider(this).get(ValoViewModel::class.java)
        _binding = FragmentValoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        androidxConstraintLayout = binding.androidxConstraintLayout
        
        sharedPreferences = requireContext().getSharedPreferences("new_message", Context.MODE_PRIVATE)
        
        // 自动填充已保存的配置
        autoFillConfig()

        // 初始化图片选择器
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    copyImageToInternalStorage(uri)
                }
            }
        }

        binding.idkButton.setOnClickListener {
            saveNotificationConfig()
        }

        binding.androidButton.setOnClickListener {
            sendBroadcast()
        }

        binding.androidTextView6.setOnClickListener {
            selectImage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        androidxConstraintLayout = null
        _binding = null
        
    }
    
    private fun autoFillConfig() {
        binding.nameTextInputEditText.setText(sharedPreferences.getString("personName", ""))
        binding.titleTextInputEditText.setText(sharedPreferences.getString("title", ""))
        binding.idTextInputEditText.setText(sharedPreferences.getInt("conversationId", 0).toString())
        binding.idaTextInputEditText.setText(sharedPreferences.getString("personNames", ""))
        binding.idkTextInputEditText.setText(sharedPreferences.getString("titles", ""))
        binding.timeTextInputEditText.setText(sharedPreferences.getString("notification_time", ""))
    }
    
    private fun saveNotificationConfig() {
        val title = binding.titleTextInputEditText.text.toString()
        val personName = binding.nameTextInputEditText.text.toString()
        val conversationId = binding.idTextInputEditText.text.toString().toIntOrNull() ?: 0
        val personNames = binding.idaTextInputEditText.text.toString()
        val titles = binding.idkTextInputEditText.text.toString()
        val notificationTime = binding.timeTextInputEditText.text.toString()

    // 验证时间格式
        val timePattern = Regex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")
        if (!timePattern.matches(notificationTime)) {
               Toast.makeText(requireContext(), "时间格式不正确，请使用 yyyy-MM-dd HH:mm:ss", Toast.LENGTH_SHORT).show()
           return
        }

        with(sharedPreferences.edit()) {
            putString("title", title)
            putString("personName", personName)
            putInt("conversationId", conversationId)
            putString("personNames", personNames)
            putString("titles", titles)
            putString("notification_time", notificationTime)
            apply()
        }

        Toast.makeText(requireContext(), "配置已保存", Toast.LENGTH_SHORT).show()
    }

    private fun sendBroadcast() {
        val message = binding.googleTextInputEditText.text.toString()
        val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
            action = "ACTION_SHOW_NOTIFICATION"
            putExtra("message", message)
        }
        requireContext().sendBroadcast(intent)
        Toast.makeText(requireContext(), "广播已发送", Toast.LENGTH_SHORT).show()
    }

    private fun selectImage() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            openImagePicker()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openImagePicker()
        } else {
            Toast.makeText(requireContext(), "权限被拒绝", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun copyImageToInternalStorage(uri: Uri) {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

        val newFileName = "new_img1.${getFileExtension(uri)}"
        val directory = File(requireContext().filesDir, "img")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val newFile = File(directory, newFileName)

        try {
            val outputStream = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()

            // 更新配置文件中的 imgPath
            with(sharedPreferences.edit()) {
                putString("imgPath", newFile.absolutePath)
                apply()
            }

            Toast.makeText(requireContext(), "图片已保存: ${newFile.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "图片保存失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: Uri): String {
        val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            it.moveToFirst()
            val name = it.getString(index)
            return name.substringAfterLast('.')
        }
        return "png" // 默认后缀
    }
    
  /*  private fun sendBroadcast() {
        val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
            action = "ACTION_SHOW_NOTIFICATION"
            putExtra("title", "可爱小喵")
            putExtra("message", "😂😂😂😂😂😂😂")
            putExtra("personName", "喵喵")
        }
        requireContext().sendBroadcast(intent)
    }
    
    private fun startNotificationService() {
        val intent = Intent(requireContext(), NotificationService::class.java)
        // 可以在这里传递需要的参数,如标题、消息、发送者等
        requireContext().startService(intent)
    }  */
}