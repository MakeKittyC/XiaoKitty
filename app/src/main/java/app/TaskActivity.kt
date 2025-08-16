package app

import android.Manifest
import android.os.Bundle
import android.os.Build
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.os.Environment
import android.os.RemoteException
import android.net.Uri
import android.text.TextUtils

import android.util.Log
import android.app.Dialog
import android.app.ActivityManager
import android.provider.Settings

import android.system.Os
import android.system.OsConstants

import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon

import android.view.Display
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.Menu
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.Gravity
import android.view.GestureDetector
import android.view.MotionEvent

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.DialogInterface
import android.content.Intent
import android.content.Context
import android.content.res.Resources
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.app.ActivityCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher

import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import app.compile.R
import app.compile.BuildConfig
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import app.compile.databinding.ActivityTaskBinding
import app.compile.databinding.NavHeaderMainBinding

import java.io.File
import java.io.FileWriter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.IOException
import java.io.BufferedReader
import java.io.FileReader
import java.util.zip.ZipInputStream
import java.util.Locale
import java.util.UUID
import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.text.SimpleDateFormat

import app.MainActivity
import app.ktx.TaskManager
import app.service.DebugService

import org.json.JSONObject
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.Suppress
import kotlin.Deprecated

public class TaskActivity : AppCompatActivity() {

companion object {
    init {
        System.loadLibrary("NonNull")
    }
}
    private lateinit var binding: ActivityTaskBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var androidxDrawerLayout: DrawerLayout
    
    private val androidxKts = "开发者：小猫猫\n性格：温柔体贴可爱、香香软软"
    private val androidxlibs = "·androidx.viewBinding\n·androidx.dataBinding\n·androidx.navigation:navigation-fragment-ktx\n·androidx.constraintlayout:constraintlayout\n·com.google.android.material:material\n·androidx.navigation:navigation-ui-ktx\n·androidx.appcompat:appcompat\n·androidx.lifecycle:lifecycle-livedata-ktx\n·androidx.lifecycle:lifecycle-viewmodel-ktx\n·androidx.lifecycle:lifecycle-process\n·androidx.lifecycle:lifecycle-runtime-ktx\n·androidx.lifecycle:lifecycle-reactivestreams-ktx\n·androidx.core:core-ktx\n·com.squareup.okio:okio\n·com.squareup.okhttp3:okhttp\n·androidx.activity:activity-ktx\n·androidx.fragment:fragment-ktx\n·org.jetbrains.kotlinx:kotlinx-coroutines-core\n·org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm\n·org.jetbrains.kotlinx:kotlinx-serialization-json-jvm\n·com.google.code.gson:gson\n·com.squareup.retrofit2:retrofit\n·com.squareup.retrofit2:converter-gson\n·androidx.annotation:annotation\n·androidx.annotation:annotation-experimental"
    
    private val jsonFileName = "user_response.json"
    private var debugService: DebugService? = null
    private var isBound = false
    
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as DebugService.LocalBinder
            debugService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            debugService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
       androidxDrawerLayout = binding.androidxDrawerLayout
        ViewSartsK()
       
        // Register a callback in your Activity or Fragment
val onBackPressedDispatcher = onBackPressedDispatcher
onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        // Your custom back handling logic here
        // You can finish the activity or perform other actions
        // If you want the default back navigation, call super.onBackPressed()
        if (isEnabled) {
            isEnabled = false // prevent multiple calls on same backpress
            onBackPressedDispatcher.onBackPressed() // for default navigation
        }
    }
})

        // 初始化NavController和AppBarConfiguration
        navController = findNavController(R.id.nav_host_fragment_activity_task)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_fragment_valo, 
                R.id.navigation_fragment_task,
                R.id.navigation_fragment_vote, 
                R.id.navigation_fragment_just,
                R.id.navigation_fragment_fast
            )
        )
        
      binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_xyn_menu)
      binding.appBarMain.toolbar.setNavigationOnClickListener {
            if (!binding.androidxDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.androidxDrawerLayout.openDrawer(GravityCompat.END)
            }
        }
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
        }

        // 将BottomNavigationView与NavController关联
        binding.appBarMain.navView.setupWithNavController(navController)
        
    }
    
    fun hideBottomNavigation() {
       binding.appBarMain.navView.animate()
           .translationY(binding.appBarMain.navView.height.toFloat())
           .setDuration(500)
           .withEndAction {
               binding.appBarMain.navView.visibility = View.GONE
           }
    }

    fun showBottomNavigation() {
        binding.appBarMain.navView.visibility = View.VISIBLE
        binding.appBarMain.navView.translationY = binding.appBarMain.navView.height.toFloat() // 确保它在隐藏状态
        binding.appBarMain.navView.animate()
           .translationY(0f)
           .setDuration(500)
           .start()
    }
    
    fun ViewSartsK(){
        // 检查是否已生成文件
        if (!hasUserResponded()) {
            val fileContent = readFromAssets(this, "application-user.txt")
            showAlertDialog(this, fileContent)
        }
       
        handleIntent(intent)
       
        val cacheNames = listOf("img", "Log/json", "Log/process", "Log/exec", "Log/data")
        val fileNames = listOf("bin", "data", "img", "home/.android")
        createPrivateExternalDirectories(fileNames)
        createInternalStorageDirectories(fileNames)
        createPrivateCacheDirectories(cacheNames)
        createPublicCacheDirectories(cacheNames)
       
        val navvView = binding.navvView
        val headerView = navvView.getHeaderView(0)
        val headerBinding = NavHeaderMainBinding.bind(headerView)
        val androidxTextView = headerBinding.textViewAndroid
        val androidVersion = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        val securityPatch = Build.VERSION.SECURITY_PATCH
       // val pageSize = Os.sysconf(OsConstants._SC_PAGE_SIZE)
        val pageSizeString = getPageSizeInKB()
        println(pageSizeString) // 输出结果

        androidxTextView.text = "安卓: Android $androidVersion ($sdkVersion) ($pageSizeString) | 安全更新: $securityPatch"
        val DeviceTextView = headerBinding.textViewDevice
        val systemName = "${Build.MANUFACTURER} ${Build.BRAND} ${Build.MODEL}"
        DeviceTextView.text = "机型: $systemName"
        val kernelTextView = headerBinding.textViewKernel
        val kernelVersion = System.getProperty("os.version") ?: "N/A"
        kernelTextView.text = "内核版本: $kernelVersion"
        val systemTextView = headerBinding.textViewSystem
        val systemVersion = Build.FINGERPRINT
        systemTextView.text = "系统指纹: $systemVersion"
        val hostTextView = headerBinding.textViewHost
        val host = Build.HOST
        hostTextView.text = "构建主机: $host"
        val sysTextView = headerBinding.textViewSystemArchitecture
        val systemArchitecture = Build.SUPPORTED_ABIS.joinToString(", ")
        sysTextView.text = "系统架构: $systemArchitecture"
        val androidTextView = headerBinding.textViewAndroidId
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        androidTextView.text = "Android SSAID: $androidId"
        
        val androidxlibsTextView = headerBinding.textViewLibs
        androidxlibsTextView.text = androidxlibs
        
        val androidxKtsTextView = headerBinding.textViewKts
        androidxKtsTextView.text = androidxKts
    }
    
    override fun onCreateOptionsMenu(menu: Menu?):   Boolean {
          menuInflater.inflate(R.menu.menu_task, menu)
    
         return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
           return when (item.itemId) {
              R.id.action_task_1 -> {
            Toast.makeText(this, "正在跳转到其他界面……", Toast.LENGTH_SHORT).show()
    val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
               true
           }
              R.id.action_task_2 -> {
               Toast.makeText(this, "正在启动调试服务……", Toast.LENGTH_SHORT).show()
               val serviceIntent = Intent(this, DebugService::class.java)
               startService(serviceIntent)
               bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
                true
           }
              R.id.action_task_3 -> {
               Toast.makeText(this, "正在停止调试服务……", Toast.LENGTH_SHORT).show()
               val serviceIntent = Intent(this, DebugService::class.java)
               stopService(serviceIntent)
               if (isBound) {
                  unbindService(connection)
                  isBound = false
               }
                true
           }
              R.id.action_task_4 -> {
                  Taskdialogs1()
                true
           }
              R.id.action_task_5 -> {
                  val taskManager = TaskManager(this)
                  taskManager.showRefreshRateDialog()
                true
           }
              R.id.action_task_6 -> {
                   ExitApp()
                true
           }
             else -> super.onOptionsItemSelected(item)
        }
        
    }
    
    private fun ExitApp() {
       finishAffinity()
       System.exit(0)
    }

    private fun getPageSizeInKB(): String {
           val pageSize = Os.sysconf(OsConstants._SC_PAGE_SIZE)
    return when (pageSize) {
        0L -> "0KB"
        4096L -> "4KB"
        8192L -> "8KB"
        16384L -> "16KB"
        65536L -> "64KB"
        else -> throw IllegalArgumentException("Unexpected page size: $pageSize") // 处理未列出的情况
    }
}
    
    private fun Taskdialogs1() {
        val taskTitle = "关于我们(应用架构/签名)"
        val taskMessage = "·应用完全本地化，无任何服务器端 \n·因应用架构以及代码逻辑架构，故此应用不适合混淆 \n·开发者不会添加任何签名校验，这样做的目的是为了让用户可以使用自己的签名密钥，从而不依靠开发者的签名密钥 \n·身为开发者，我并不觉得我有义务为我的软件包添加防寡改/防二改的安全防护措施 \n·您身为用户，您应自愿承担安装不可信来源的软件包所造成的一切责任"
    
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(taskTitle)
            .setMessage(taskMessage)
            .setPositiveButton("我已了解") { _, _ ->
                Toast.makeText(this, "我自愿承担在使用过程中所造成的一切责任！", Toast.LENGTH_SHORT).show()
        }
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.window?.decorView?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.task_dialog_background)
    }
    
    // 创建私有文件目录
    private fun createPrivateExternalDirectories(fileNames: List<String>) {
    fileNames.forEach { folderName ->
        val directory = getExternalFilesDir(folderName)
        
        if (directory != null && !directory.exists()) {
            val success = directory.mkdirs()
            if (success) {
                Log.d("Directory", "Private external directory created: ${directory.absolutePath}")
            } else {
                Log.e("Directory", "Failed to create private external directory")
            }
        } else {
            Log.d("Directory", "Private external directory already exists or is null")
        }
    }
}

// 创建公开文件目录
private fun createInternalStorageDirectories(fileNames: List<String>) {
    fileNames.forEach { folderName ->
        val directory = File(filesDir, folderName)
        
        if (!directory.exists()) {
            val success = directory.mkdirs()
            if (success) {
                Log.d("Directory", "Internal storage directory created: ${directory.absolutePath}")
            } else {
                Log.e("Directory", "Failed to create internal storage directory")
            }
        } else {
            Log.d("Directory", "Internal storage directory already exists")
        }
    }
}

// 创建私有缓存目录
private fun createPrivateCacheDirectories(cacheNames: List<String>) {
    // 遍历每个缓存目录名称
    cacheNames.forEach { folderName ->
        // 获取外部缓存目录，使用 File 组合路径
        val directory = File(getExternalCacheDir(), folderName)

        // 检查目录是否为 null 或者是否存在
        if (!directory.exists()) {
            // 尝试创建目录
            val success = directory.mkdirs()
            if (success) {
                Log.d("Directory", "Private cache directory created: ${directory.absolutePath}")
            } else {
                Log.e("Directory", "Failed to create private cache directory")
            }
        } else {
            Log.d("Directory", "Private cache directory already exists: ${directory.absolutePath}")
        }
    }
}

// 创建公开缓存目录
private fun createPublicCacheDirectories(cacheNames: List<String>) {
   cacheNames.forEach { folderName ->
       val directory = File(cacheDir, folderName)
       
       if (!directory.exists()) {
           val success = directory.mkdirs()
           if (success) {
               Log.d("Directory", "Public cache directory created: ${directory.absolutePath}")
           } else {
               Log.e("Directory", "Failed to create public cache directory")
           }
       } else {
           Log.d("Directory", "Public cache directory already exists")
       }
   }
}
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_VIEW == intent.action) {
            val uri: Uri? = intent.data
            uri?.let { uri ->
                // 提取查询参数
                val cosValue = uri.getQueryParameter("string")
                
                // 显示参数
                Toast.makeText(this, "猫鱼鱼：$cosValue", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "未接收到有效的 URI", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun readFromAssets(context: Context, fileName: String): String {
        val assetManager = context.assets
        return assetManager.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun showAlertDialog(context: Context, content: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("朗读用户协议以同意使用")
            .setMessage(content)
            .setPositiveButton("同意") { dialog, _ ->
                dialog.dismiss()
                // 生成 JSON 文件以保存用户响应
                saveUserResponse()
                requestPermission1()
                requestPermission2()
            }
            .setNegativeButton("不同意") { dialog, _ ->
                dialog.dismiss()
                finish()
                Toast.makeText(this, "说什么，我没听清……", Toast.LENGTH_SHORT).show()
            }

        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.window?.decorView?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.task_dialog_background)
    }

    private fun saveUserResponse() {
        val jsonObject = JSONObject()
        jsonObject.put("UesrXiaoKitty", 1)

        val file = File(filesDir, jsonFileName)
        FileOutputStream(file).use { outputStream ->
            outputStream.write(jsonObject.toString().toByteArray())
        }
    }

    private fun hasUserResponded(): Boolean {
        val file = File(filesDir, jsonFileName)
        return file.exists() && file.readText().contains("\"UesrXiaoKitty\":1")
    }
    
    private fun requestPermission1() {
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
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, 3060)
        }
    }

    private fun requestPermission2() {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:${packageName}")
                startActivity(intent)
            } else {
            // 权限已经被授予，可以执行文件操作
        }
    }
    
    override fun onStart() {
        super.onStart()
   /*     Intent(this, DebugService::class.java).also { intent ->
            startService(intent) // 启动服务
            bindService(intent, connection, Context.BIND_AUTO_CREATE) // 绑定服务
        }  */
    }

    override fun onStop() {
        super.onStop()
    /*    Intent(this, DebugService::class.java).also { intent ->
            startService(intent) // 启动服务
            bindService(intent, connection, Context.BIND_AUTO_CREATE) // 绑定服务
        }  */
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection) // 解绑服务
            isBound = false
        }
        Intent(this, DebugService::class.java).also { intent ->
            stopService(intent) // 停止服务
        }   
    }
}