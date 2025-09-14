package app

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.os.Environment
import android.os.RemoteException
import android.system.Os
import android.system.OsConstants
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

import android.graphics.Point
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import android.graphics.Rect

import java.math.BigInteger
import java.security.KeyFactory
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.RSAPublicKeySpec
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.io.BufferedReader

import android.app.Dialog
import android.app.ActivityManager
import android.app.UiModeManager

import android.hardware.display.DisplayManager

import android.view.Display
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.WindowMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.Gravity
import android.view.GestureDetector
import android.view.MotionEvent

import android.content.DialogInterface
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Intent
import android.content.Context
import android.content.res.Resources
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature

import android.widget.Button
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView

import app.compile.databinding.ActivityMainBinding
import app.compile.R
import app.compile.BuildConfig
import app.TaskActivity
import app.push.NotificationService

import kotlin.math.sqrt

import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

public class MainActivity : AppCompatActivity() {

companion object {
    init {
        System.loadLibrary("${BuildConfig.CPP_NAME}")
    }
    
}
    private var _binding: ActivityMainBinding? = null
    
    private val binding: ActivityMainBinding
      get() = checkNotNull(_binding) { "MainActivity null" }
      
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        // 禁止截屏和录屏
      //  window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        // 登录按钮点击事件
        binding.mainButton.setOnClickListener {
            // 跳转到主界面
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
            finish() // 关闭当前 Activity
        }
        
        val allLink = "android-linux-test-xiaokitty-2025-v1-v2-v3-v4-v5-v6-v7-v8-v9-v0-app"
        // 获取 TextView 实例
        val signatureTextView: TextView = binding.myTextView
        signatureTextView.text = getMetaDataStatus() + "\n" + allLink
        
        val kernelVersion = System.getProperty("os.version") ?: "N/A"
        val systemAbi = getSystemSupportCpuAbi()
        println(systemAbi) // 输出结果
        val systemAbi64 = getSystemSupportCpuAbi64()
        println(systemAbi64) // 输出结果
        val systemAbi32 = getSystemSupportCpuAbi32()
        println(systemAbi32) // 输出结果
        val minVersion = getMinSupportedTargetSdk()
        println(minVersion) // 输出结果
        val pageSizeString = getPageSizeInKB()
        println(pageSizeString) // 输出结果
        val buildTime = Build.TIME
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(buildTime)
        val radioVersion = Build.getRadioVersion()
        val androidSys1 = "Android Version: ${Build.VERSION.RELEASE}"
        val androidSys2 = "Android SDK: ${Build.VERSION.SDK_INT}"
        val androidSys3 = "安全更新补丁级别: ${Build.VERSION.SECURITY_PATCH}"
        val androidSys4 = "Kernel Version: $kernelVersion"
        val androidSys5 = "System Version: ${Build.FINGERPRINT}"
        val androidSys6 = "System ABI: $systemAbi"
        val androidSys7 = "设备提供商: ${Build.MANUFACTURER}"
        val androidSys8 = "设备厂商: ${Build.BRAND}"
        val androidSys9 = "设备机型: ${Build.MODEL}"
        val androidSys10 = "设备内核页面大小: $pageSizeString"
        val androidSys11 = "Android minSDK: $minVersion"
        val androidSys12 = "主板: ${Build.BOARD}"
        val androidSys13 = "构建时间: $formattedDate"
        val androidSys14 = "硬件名称: ${Build.HARDWARE}"
        val androidSys15 = "构建主机: ${Build.HOST}"
        val androidSys16 = "基带版本: $radioVersion"
        val androidSys17 = "ABI-32: $systemAbi32"
        val androidSys18 = "ABI-64: $systemAbi64"
        val androidSysView: TextView = binding.appInfoMain.textView
        androidSysView.text = androidSys1 + "\n" + androidSys2 + "\n" + androidSys11 + "\n" + androidSys3 + "\n" + androidSys7 + "\n" + androidSys8 + "\n" + androidSys9 + "\n" + androidSys12 + "\n" + androidSys14 + "\n" + androidSys10 + "\n" + androidSys13 + "\n" + androidSys17 + "\n" + androidSys18
        
        val androidSystemView: TextView = binding.appInfoMain.textView3
        androidSystemView.text = androidSys4 + "\n" + androidSys5 + "\n" + androidSys6 + "\n" + androidSys15 + "\n" + androidSys16
        
        val androidApp1 = "${BuildConfig.APPLICATION_ID}"
        val androidApp2 = "targetSdk: ${BuildConfig.TARGET_SDK_VERSION}"
        val androidApp3 = "minSdk: ${BuildConfig.MIN_SDK_VERSION}"
        val androidApp4 = "compileSdk: ${BuildConfig.COMPILE_SDK_VERSION}"
        val androidApp5 = "versionCode: ${BuildConfig.VERSION_CODE}"
        val androidApp6 = "versionName: ${BuildConfig.VERSION_NAME}"
        val androidApp7 = "Build Type: ${BuildConfig.BUILD_TYPE}"
        val androidAppView: TextView = binding.appInfoMain.textView1
        androidAppView.text = androidApp1 + "\n" + androidApp2 + "\n" + androidApp3 + "\n" + androidApp4 + "\n" + androidApp5 + "\n" + androidApp6 + "\n" + androidApp7
        
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        val width: Int
        val height: Int
        
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    val windowMetrics = windowManager.currentWindowMetrics
    val bounds = windowMetrics.bounds
    width = bounds.width()
    height = bounds.height()
} else {
    @Suppress("DEPRECATION")
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    width = displayMetrics.widthPixels
    height = displayMetrics.heightPixels
}

        val supportedRefreshRates = getSupportedRefreshRates(this)
        val sortedRefreshRates = supportedRefreshRates.sorted()
        val refreshRateText = sortedRefreshRates.joinToString(", ") { "$it Hz" }
        
        val currentRefreshRate = getCurrentRefreshRate(this)
        val screenDensity = getScreenDensity(this)
        val screenDPI = getScreenDPI(this)
        val screenSizeInInches = getScreenSizeInInches(this)
        val AspectRatio = getAspectRatio(this)

        val iphone1 = "屏幕分辨率: ${width}x${height}"
        val iphone2 = "屏幕密度: $screenDensity"
        val iphone3 = "屏幕 DPI 值: $screenDPI"
        val iphone4 = "屏幕尺寸[英寸]: $screenSizeInInches"
        val iphone5 = "宽高比  $AspectRatio"
        val iphone6 = "当前刷新率: $currentRefreshRate"
        val iphone7 = "屏幕刷新率: \n$refreshRateText"
        val androidIphoneView: TextView = binding.appInfoMain.textView2
        androidIphoneView.text = iphone1 + "\n" + iphone2 + "\n" + iphone3 + "\n" + iphone4 + "\n" + iphone5 + "\n" + iphone6 + "\n" + iphone7 
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

fun getAspectRatio(context: Context): String {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics: DisplayMetrics

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        // 使用 WindowMetrics API
        val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
        displayMetrics = DisplayMetrics()
        displayMetrics.widthPixels = windowMetrics.bounds.width()
        displayMetrics.heightPixels = windowMetrics.bounds.height()
    } else {
        // 旧版本使用 getRealMetrics
        displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
    }

    val widthPixels = displayMetrics.widthPixels
    val heightPixels = displayMetrics.heightPixels

    // 计算 GCD
    val gcd = gcd(widthPixels, heightPixels)

    // 计算宽高比
    val aspectWidth = widthPixels / gcd
    val aspectHeight = heightPixels / gcd

    return "$aspectHeight:$aspectWidth"
} 

// 计算最大公约数的辅助函数
fun gcd(a: Int, b: Int): Int {
    return if (b == 0) a else gcd(b, a % b)
}


fun getScreenSizeInInches(context: Context): Double {
    val metrics = context.resources.displayMetrics
    val widthInPixels = metrics.widthPixels
    val heightInPixels = metrics.heightPixels
    val widthInInches = widthInPixels / metrics.xdpi
    val heightInInches = heightInPixels / metrics.ydpi

    // 使用勾股定理计算屏幕对角线长度
    return sqrt(widthInInches * widthInInches + heightInInches * heightInInches).toDouble()
} 

    fun getScreenDensity(context: Context): Float {
       val metrics = context.resources.displayMetrics
       return metrics.density // 返回密度值（如 1.0, 2.0, 3.0 等）
   }

    fun getScreenDPI(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.densityDpi // 返回 DPI 值（如 120, 160, 240 等）
   }

 private fun getCurrentRefreshRate(context: Context): String {
    val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    val display: Display? = displayManager.getDisplay(Display.DEFAULT_DISPLAY)

    // 获取当前的刷新率
    return display?.refreshRate?.let { "$it Hz" } ?: "无法获取"
}
    
    private fun getSupportedRefreshRates(context: Context): List<Float> {
    val refreshRates = mutableListOf<Float>()
    val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

    // 获取所有显示设备
    val displays: Array<Display> = displayManager.displays

    // 遍历所有显示设备
    for (display in displays) {
        val supportedModes = display.supportedModes
        for (mode in supportedModes) {
            refreshRates.add(mode.refreshRate)
        }
    }

    // 去重并返回
    return refreshRates.distinct()
} 

 fun getMinSupportedTargetSdk(): String {
    return try {
        val properties = Class.forName("android.os.SystemProperties")
        val get = properties.getMethod("get", String::class.java)
        get.invoke(properties, "ro.build.version.min_supported_target_sdk") as String
    } catch (e: Exception) {
        "N/A" // 处理异常
    }
}

 fun getSystemSupportCpuAbi(): String {
    return try {
        val properties = Class.forName("android.os.SystemProperties")
        val get = properties.getMethod("get", String::class.java)
        get.invoke(properties, "ro.system.product.cpu.abilist") as String
    } catch (e: Exception) {
        "N/A" // 处理异常
    }
}

  fun getSystemSupportCpuAbi64(): String {
    return try {
        val properties = Class.forName("android.os.SystemProperties")
        val get = properties.getMethod("get", String::class.java)
        get.invoke(properties, "ro.system.product.cpu.abilist64") as String
    } catch (e: Exception) {
        "N/A" // 处理异常
    }
}

  fun getSystemSupportCpuAbi32(): String {
    return try {
        val properties = Class.forName("android.os.SystemProperties")
        val get = properties.getMethod("get", String::class.java)
        get.invoke(properties, "ro.system.product.cpu.abilist32") as String
    } catch (e: Exception) {
        "N/A" // 处理异常
    }
}
    
    private fun showDialog() {
        val taskTitle = "开发进度"
        val taskMessage = "开发了30%，剩下70%，慢慢开发"
    
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(taskTitle)
            .setMessage(taskMessage)
            .setPositiveButton("ok") { _, _ ->
            Toast.makeText(this, "感谢您的理解与支持!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("no") { dialog, _ ->
            Toast.makeText(this, "呜呜X﹏X，NO!", Toast.LENGTH_SHORT).show()
            }
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.window?.decorView?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main_cos)
    }

    // 获取 meta-data 状态
    private fun getMetaDataStatus(): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData?.containsKey("lspatch") == true) {
                "警告：软件包已被 LSPatch 修改过"
            } else {
                "温馨提示：软件包未被 LSPatch 修改过"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            "无法判断软件包是否被 LSPatch 修改过"
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?):   Boolean {
          menuInflater.inflate(R.menu.menu_image, menu)
    
         return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
           return when (item.itemId) {
              R.id.action_image_1 -> {
              Toast.makeText(this, "无需多言……", Toast.LENGTH_SHORT).show()
               true
           }
              R.id.action_image_2 -> {
              showDialog()
                true
           }
              R.id.action_image_3 -> {
              val intent = Intent(this, NotificationService::class.java).apply {
    action = "ACTION_SHOW_NOTIFICATION"
    putExtra("title", "标题")
    putExtra("message", "消息内容")
    putExtra("personName", "发送者名称")
}
this.startService(intent)
                true
           }
             else -> super.onOptionsItemSelected(item)
        }
        
    }

    override fun onPause() {
        super.onPause()
        // 进入后台时仍然禁止截屏
      //  window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun onResume() {
        super.onResume()
        // 返回前台时依然禁止截屏
      //  window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        
    }
}