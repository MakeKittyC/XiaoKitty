package app.ui.fast

import android.os.Bundle
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.system.Os
import android.system.OsConstants
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.LinkAddress
import android.net.VpnService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.compile.databinding.FragmentFastBinding
import app.compile.R
import app.compile.BuildConfig
import androidx.core.widget.NestedScrollView

public class FastFragment : Fragment() {

companion object {
    init {
        System.loadLibrary("${BuildConfig.CPP_NAME}")
    }
    
}
    private var _binding: FragmentFastBinding? = null
    private val binding get() = _binding!!
    
    private var androidxNestedScrollView: NestedScrollView? = null
    
    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable: Runnable = object : Runnable {
        override fun run() {
            // 获取当前的 PID 和 UID
            val pid = android.os.Process.myPid()
            val uid = android.os.Process.myUid()
            binding.androidxUidpid.text = "PID: $pid + UID: $uid"
            
            // 每隔8秒刷新一次
            handler.postDelayed(this, 8000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fastViewModel =
            ViewModelProvider(this).get(FastViewModel::class.java)
        _binding = FragmentFastBinding.inflate(inflater, container, false)
        val root: View = binding.root

        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        androidxNestedScrollView = binding.androidxNestedScrollView
        
        binding.targetSdkVersion.text = "targetSdk: ${BuildConfig.TARGET_SDK_VERSION}"
        binding.minSdkVersion.text = "minSdk: ${BuildConfig.MIN_SDK_VERSION}"
        binding.compileSdkVersion.text = "compileSdk: ${BuildConfig.COMPILE_SDK_VERSION}"
        binding.versionCode.text = "versionCode: ${BuildConfig.VERSION_CODE}"
        binding.versionName.text = "versionName: ${BuildConfig.VERSION_NAME}"
        binding.applicationId.text = "application id: ${BuildConfig.APPLICATION_ID}"
        binding.buildType.text = "Build Type: ${BuildConfig.BUILD_TYPE}"
        
        val pageSizeString = getPageSizeInKB()
        println(pageSizeString) // 输出结果
        val androidVersion = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        val securityPatch = Build.VERSION.SECURITY_PATCH
        binding.androidxVersion.text = "Android $androidVersion ($sdkVersion) ($pageSizeString) | 安全更新: $securityPatch"
        val kernelVersion = System.getProperty("os.version") ?: "N/A"
        binding.androidxKernel.text = "内核版本: $kernelVersion"
        val systemVersion = Build.FINGERPRINT
        binding.androidxSystem.text = "$systemVersion"
        val systemName = "${Build.MANUFACTURER} ${Build.BRAND} ${Build.MODEL}"
        binding.androidxDevice.text = "设备代号: $systemName"
        val systemArchitecture = Build.SUPPORTED_ABIS.joinToString(", ")
        binding.androidxSys.text = "系统架构: $systemArchitecture"
        handler.post(refreshRunnable)
        
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

    override fun onDestroyView() {
        super.onDestroyView()
        androidxNestedScrollView = null
        _binding = null
        handler.removeCallbacks(refreshRunnable)
    }
}