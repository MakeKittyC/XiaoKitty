package app.service

import android.app.Service
import android.app.PendingIntent
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationChannelGroup

import android.Manifest
import android.graphics.Color
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.view.WindowInsets
import android.annotation.SuppressLint
import android.provider.Settings

import android.net.Uri
import android.net.ConnectivityManager
import android.net.TrafficStats

import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.BroadcastReceiver

import android.os.IBinder
import android.util.Log
import android.os.Process
import android.os.Environment
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.RemoteException

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat.MessagingStyle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.util.ObjectsCompat
import androidx.core.util.ObjectsCompat.requireNonNull
import androidx.lifecycle.LifecycleService

import app.compile.R
import app.compile.BuildConfig

public class DebugService : Service() {

companion object {
    init {
        System.loadLibrary("${BuildConfig.CPP_NAME}")
    }
}
    private var job: Job? = null
    private val binder = LocalBinder()
    private val NOTIFICATION_ID = 1230
    private lateinit var notificationManager: NotificationManager
    
    private var lastTxBytes: Long = 0
    private var lastRxBytes: Long = 0
    
    inner class LocalBinder : Binder() {
        fun getService(): DebugService = this@DebugService
    }
    
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification("正在获取调试信息..."))
        job = CoroutineScope(Dispatchers.IO).launch {
            monitorNetworkTraffic()
        }
        return START_STICKY
    }

    private suspend fun monitorNetworkTraffic() {
        while (true) {
            val currentTxBytes = TrafficStats.getTotalTxBytes()
            val currentRxBytes = TrafficStats.getTotalRxBytes()

            // 计算上传和下载速率
            val uploadRate = currentTxBytes - lastTxBytes
            val downloadRate = currentRxBytes - lastRxBytes

            lastTxBytes = currentTxBytes
            lastRxBytes = currentRxBytes

            // 更新通知
            updateNotification(uploadRate, downloadRate)

            // 每秒更新一次
            delay(1000)
        }
    }

    private fun formatDataRate(rate: Long): String {
        return when {
            rate >= 1024 * 1024 * 1024 -> String.format("%.2f GB/s", rate / (1024 * 1024 * 1024).toDouble())
            rate >= 1024 * 1024 -> String.format("%.2f MB/s", rate / (1024 * 1024).toDouble())
            rate >= 1024 -> String.format("%.2f KB/s", rate / 1024.0)
            else -> "$rate B/s"
        }
    }

    private fun updateNotification(uploadRate: Long, downloadRate: Long) {
        val uploadRateString = formatDataRate(uploadRate)
        val downloadRateString = formatDataRate(downloadRate)
        val message = "上传速率: $uploadRateString\n下载速率: $downloadRateString"
        val notification = createNotification(message)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun createNotification(message: String): Notification {
        val channelId = "debug"
        val channelName = "调试/测试服务"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        // 注册通知渠道
        notificationManager.createNotificationChannel(notificationChannel)
        
        val titleText = "服务正在运行"
        
        // 创建通知构建器
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.image_004)
            .setContentTitle(titleText)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setAutoCancel(true)
            .setOngoing(true)
            .build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel() // 取消协程任务
        // 移除通知 STOP_FOREGROUND_REMOVE
        // 保留通知 STOP_FOREGROUND_DETACH
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
    
    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}