package app.ui.vote.ui.save

import android.Manifest
import android.util.Log
import android.net.Uri
import android.os.Bundle
import android.os.Build
import android.os.Process
import android.os.Environment
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import android.text.util.Linkify
import android.text.Editable
import android.text.TextWatcher
import android.app.AppOpsManager
import android.app.Activity

import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast

import android.content.Intent
import android.content.Context
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import android.provider.Settings
import androidx.cardview.widget.CardView
import androidx.annotation.Nullable

import app.compile.R
import app.compile.BuildConfig
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.compile.databinding.FragmentSaveBinding
import android.app.usage.UsageStatsManager

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.io.FileOutputStream
import java.io.File
import java.io.FileWriter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.ProcessBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResult

import app.ui.vote.ui.save.SaveViewModel

public class SaveFragment : Fragment(),CoroutineScope {

companion object {
   init {
        System.loadLibrary("NonNull")
    }
}
    private var _binding: FragmentSaveBinding? = null
    
    private var nestedScrollView: NestedScrollView? = null
    private var androidxCardView1: CardView? = null
    private var linearLayout1: LinearLayout? = null
    private var linearLayout2: LinearLayout? = null
    private var linearLayout3: LinearLayout? = null
    private var linearLayout4: LinearLayout? = null
    private var linearLayout5: LinearLayout? = null
    
    private lateinit var viewModel: SaveViewModel

    private val binding get() = _binding!!
    
    private var rules: Map<String, String> = emptyMap()
    private var lastToastMessage: String? = null
    private var lastToastTime: Long = 0
    
    // android.permission.SYSTEM_ALERT_WINDOW
  //  private lateinit var overlayPermissionLauncher: ActivityResultLauncher<Intent>
    
    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 初始化 ActivityResultLauncher
        overlayPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleOverlayPermissionResult(result)
        } */
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SaveViewModel::class.java)
        _binding = FragmentSaveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        nestedScrollView = null
        androidxCardView1 = null
        linearLayout1 = null
        linearLayout2 = null
        linearLayout3 = null
        linearLayout4 = null
        linearLayout5 = null
        job.cancel() // 取消协程
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nestedScrollView = binding.nestedScrollView
        androidxCardView1 = binding.androidxCardView1
        linearLayout1 = binding.linearLayout1
        linearLayout2 = binding.linearLayout2
        linearLayout3 = binding.linearLayout3
        linearLayout4 = binding.linearLayout4
        linearLayout5 = binding.linearLayout5
        
        binding.resultTextView.text = viewModel.outputText
        
        binding.executeButton.setOnClickListener {
            launch {
                executeShellCommand()
            }
        }
        binding.execButton.setOnClickListener {
            launch {
                executeShellCommands()
            }
        }
        // 检查并创建默认配置文件
        createDefaultConfigIfNeeded()

        // 读取规则
        loadRulesFromFile()

        // 添加文本变化监听器
        binding.commandEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { text ->
                    val message = rules[text.toString()]

                    val currentTime = System.currentTimeMillis()
                    if (message != null && (message != lastToastMessage || 
                        (currentTime - lastToastTime >= 30000))) {
                        showToast(message)
                        lastToastMessage = message
                        lastToastTime = currentTime
                    } else if (message == null) {
                        lastToastMessage = null // 清空上一个消息
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.rulsuTextView.setOnClickListener {
         requestPermission1()
         requestPermission2()
         requestPermission3()
         lxynpermissions(this)
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    
    private fun createDefaultConfigIfNeeded() {
        val filePath = "/data/user/0/${BuildConfig.APPLICATION_ID}/files/home/rules.json"
        val file = File(filePath)

        if (!file.exists()) {
            try {
                // 创建文件并写入默认内容
                val defaultContent = """
                // 重复字符可能会导致Toast无法弹出，并且每次更改配置文件时都需要重启软件包
                {
                    "好可爱": "你也很可爱哦~",
                    "为什么呢": "因为我是聪明幼女！！！",
                    "喵": "喵~",
                    "姐姐": "妹妹~",
                    "妹妹": "姐姐~",
                    "哥哥": "妹妹~",
                    "弟弟": "哥哥~",
                    "老公": "老婆~",
                    "老婆": "老公~",
                    "娘子": "相公~",
                    "相公": "娘子~",
                    "小姐姐": "小妹妹~",
                    "小妹妹": "小姐姐~",
                    "小哥哥": "小妹妹~",
                    "小弟弟": "小哥哥~",
                    "小公主": "小王子~"
                }
                """.trimIndent()

                FileOutputStream(file).use { outputStream ->
                    outputStream.write(defaultContent.toByteArray())
                }
                Log.d("SaveFragment", "Default config file created.")
            } catch (e: Exception) {
                Log.e("SaveFragment", "Error creating default config: ${e.message}")
            }
        }
    }

    private fun loadRulesFromFile() {
        try {
            val filePath = "/data/user/0/Make.View.XiaomiHyperOS/files/home/rules.json"
            val file = File(filePath)
            if (file.exists()) {
                val json = file.readText()
                val gson = Gson()
                val type = object : TypeToken<Map<String, String>>() {}.type
                rules = gson.fromJson(json, type)
            } else {
                Log.e("SaveFragment", "Rules file does not exist.")
            }
        } catch (e: Exception) {
            Log.e("SaveFragment", "Error loading rules: ${e.message}")
        }
    }
    
    private fun lxynpermissions(fragment: Fragment) {
    val permissions = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.TRANSMIT_IR,
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.CAMERA,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_BASIC_PHONE_STATE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.READ_SYNC_SETTINGS,
        Manifest.permission.READ_SYNC_STATS,
        Manifest.permission.USE_SIP,
        Manifest.permission.ADD_VOICEMAIL,
        Manifest.permission.RECORD_AUDIO
    )

    val permissionGranted = permissions.all {
        ContextCompat.checkSelfPermission(fragment.requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    if (!permissionGranted) {
        ActivityCompat.requestPermissions(fragment.requireActivity(), permissions, 3060)
    }
}
    // android.permission.MANAGE_EXTERNAL_STORAGE
     private fun requestPermission1() {
         if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:${requireContext().packageName}")
            startActivity(intent)
           } else {
        
        }
     }
     // android.permission.PACKAGE_USAGE_STATS
    private fun requestPermission3() {
         val context = requireContext()
         val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

         val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
          val mode = appOpsManager.unsafeCheckOp(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)

            if (mode == AppOpsManager.MODE_ALLOWED) {
        
                      return
               } else {
                  val packageName = context.packageName
                  val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                  intent.data = Uri.fromParts("package", packageName, null)
                  startActivity(intent)
         }
     }

     // android.permission.WRITE_SETTINGS
     private fun requestPermission2() {
         if (canAccessWriteSettings()) {
        
                return
            } else {
              val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
              intent.data = Uri.parse("package:${requireContext().packageName}")
              startActivity(intent)
           }
       }

    private fun canAccessWriteSettings(): Boolean {
      return Settings.System.canWrite(requireContext())
    }
    /*
    // android.permission.SYSTEM_ALERT_WINDOW
    private fun requestPermission3() {
        // 同时支持 Android 6.0 及以上版本
        if (!Settings.canDrawOverlays(requireContext())) {
            // 权限未被授予，引导用户到设置页面
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireContext().packageName}"))
            overlayPermissionLauncher.launch(intent)
        } else {
            // 权限已被授予，可以执行悬浮窗相关操作
            showOverlayWindow()
        }
    }

    private fun handleOverlayPermissionResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            if (Settings.canDrawOverlays(requireContext())) {
                // 权限已被授予
                showOverlayWindow()
            } else {
                // 用户拒绝了权限
                Toast.makeText(requireContext(), "Overlay permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOverlayWindow() {
        // 这里实现显示悬浮窗的逻辑
    }

    private fun lxynsushell() {
     val builder = MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.fragment_save_3)
        .setMessage(R.string.fragment_save_1)
        .setPositiveButton("ok") { _, _ ->
            
        }
    val dialog = builder.create()
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
} */

    @OptIn(ExperimentalCoroutinesApi::class)
    @Nullable
    private suspend fun executeShellCommand(): String? {
        val command = binding.commandEditText.text.toString().trim()
        // val mainfile = File("/data/data/${BuildConfig.APPLICATION_ID}/")
       var mainfilePath: String = "/data/user/0/${BuildConfig.APPLICATION_ID}/files/home"
       var mainfile = File(mainfilePath)
        
        if (command.isNotEmpty()) {
            try {
            withContext(Dispatchers.IO.limitedParallelism(18)) {
               // withContext(Dispatchers.IO) {
                    var Process = ProcessBuilder(command.split(" "))
                        .directory(mainfile)
                        .redirectErrorStream(true)
                        .apply {
                val currentEnv = System.getenv().toMutableMap()
                        val pathDirectories = setOf(
                            "/data/user/0/${BuildConfig.APPLICATION_ID}/files/bin",
                            "/sdcard/Android/data/${BuildConfig.APPLICATION_ID}/files/bin"
                        )
                        currentEnv["PATH"] = pathDirectories.joinToString(":") + ":" + (currentEnv["PATH"] ?: "")
                        currentEnv["HOME"] = "/data/user/0/${BuildConfig.APPLICATION_ID}/files/home"
                        for ((key, value) in currentEnv) {
                            environment()[key] = value
                           }
                        }
                        .start()
                        
                  //  val output = Process.inputStream.bufferedReader().use { it.readText() }
                    val output = StringBuilder()
                    val maxOutputSize = 1024 * 1024 // 设置最大输出大小为 1MB
                    val maxDisplayLength = 7000 // 设置最大显示字符数

                Process.inputStream.bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        output.appendLine(line) // 将每行添加到 StringBuilder 中
                        
                        // 检查输出大小
                        if (output.toString().toByteArray().size >= maxOutputSize) {
                            writeOutputToLogFile(output.toString()) // 将输出写入文件
                           // output.clear() // 清空 StringBuilder
                        }
                    }
                }

                // 写入剩余内容
                if (output.isNotEmpty()) {
                    writeOutputToLogFile(output.toString())
                }
                    

                 withContext(Dispatchers.Main) {
                        val displayedOutput = if (output.length > maxDisplayLength) {
                        output.substring(0, maxDisplayLength) // 只显示前 3000 个字符
                        } else {
                         output.toString() // 显示全部内容
                     }
                         binding.resultTextView.text = displayedOutput
                         viewModel.outputText = displayedOutput
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.resultTextView.text = "Command execution failed: ${e.message}"
                }
                null // 在异常情况下返回 null
            }
        }
        return null // 如果命令为空，返回 null
    }
   
     @OptIn(ExperimentalCoroutinesApi::class)
     @Nullable
     private suspend fun executeShellCommands(): String? {
       //  val workingDirectory = File("/data/data/${BuildConfig.APPLICATION_ID}/")
         var workDirectoryPath: String = "/data/user/0/${BuildConfig.APPLICATION_ID}/files"
         var workingDirectory = File(workDirectoryPath)
         
         val command = binding.commandEditText.text.toString().trim()

    if (command.isNotEmpty()) {
      withContext(Dispatchers.IO.limitedParallelism(18)) {
            try {
                val homeDirectory = "/data/user/0/${BuildConfig.APPLICATION_ID}/files/home"
                val currentEnv = System.getenv()
                val envList = mutableListOf<String>()

                for ((key, value) in currentEnv) {
                    envList.add("$key=$value")
                }

                val pathDirectories = setOf(
                    "/data/user/0/${BuildConfig.APPLICATION_ID}/files/bin",
                    "/sdcard/Android/data/${BuildConfig.APPLICATION_ID}/files/bin"
                )
                envList.add("PATH=${pathDirectories.joinToString(":")}:${currentEnv["PATH"] ?: ""}")
                envList.add("HOME=$homeDirectory")

             val envVariables = envList.toTypedArray()

               val process = Runtime.getRuntime().exec(command, envVariables, workingDirectory)
                process.waitFor()

                val exitCode = process.exitValue()
              //  val outputStream = process.inputStream.bufferedReader().use { it.readText() }
                val errorStream = process.errorStream.bufferedReader().use { it.readText() }
                
                val outputStream = StringBuilder()
                val maxOutputSize = 1024 * 1024 // 设置最大输出大小为 1MB

                process.inputStream.bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        outputStream.appendLine(line) // 将每行添加到 StringBuilder 中
                        
                        // 检查输出大小
                        if (outputStream.toString().toByteArray().size >= maxOutputSize) {
                            writeOutputStreamToLogFile(outputStream.toString()) // 将输出写入文件
                           // output.clear() // 清空 StringBuilder
                        }
                    }
                }

                // 写入剩余内容
                if (outputStream.isNotEmpty()) {
                    writeOutputStreamToLogFile(outputStream.toString())
                }

                withContext(Dispatchers.Main) {
                    if (exitCode == 0) {
                        binding.resultTextView.text = outputStream.toString()
                        viewModel.outputText = outputStream.toString()
                    } else {
                       binding.resultTextView.text = "Command failed with exit code: $exitCode\nError output: $errorStream"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.resultTextView.text = "Command execution failed: ${e.message}"
                }
                null // 在异常情况下返回 null
            }
         }
      }
      return null // 如果命令为空，返回 null
   }
    
    private fun writeOutputToLogFile(content: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
        val timestamp = dateFormat.format(Date()) // 获取当前时间并格式化
        val logFileName = "Log/process/$timestamp.log" // 文件名为时间戳
        val cacheDir = requireContext().cacheDir // 获取缓存目录
        val logFile = File(cacheDir, logFileName)
        
        try {
            logFile.appendText(content) // 逐行写入文件
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun writeOutputStreamToLogFile(content: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
        val timestamp = dateFormat.format(Date()) // 获取当前时间并格式化
        val logFileName = "Log/exec/$timestamp.log" // 文件名为时间戳
        val cacheDir = requireContext().cacheDir // 获取缓存目录
        val logFile = File(cacheDir, logFileName)
        
        try {
            logFile.appendText(content) // 逐行写入文件
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}