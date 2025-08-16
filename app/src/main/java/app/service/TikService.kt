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

import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
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

import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.DecimalFormat

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

public class TikService : Service() {

companion object {
    init {
        System.loadLibrary("NonNull")
    }
}
    private var job: Job? = null
    private val binder = LocalBinder()
    private val NOTIFICATION_ID = 1230
    private lateinit var notificationManager: NotificationManager
    
    inner class LocalBinder : Binder() {
        fun getService(): TikService = this@TikService
    }
    
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        job = CoroutineScope(Dispatchers.IO).launch {
            performBackgroundTask()
        }

        return START_STICKY
    }

    private suspend fun performBackgroundTask() {
        // 模拟一个耗时操作
        // 使用 withContext 切换回主线程
        withContext(Dispatchers.Main) {
            // 在这里可以更新 UI 或执行其他操作
        }
    }
    
    private fun createNotification(): Notification {
        // 创建通知渠道
        val channelId = "usb"
        val channelName = "void Service"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        // 注册通知渠道
        notificationManager.createNotificationChannel(notificationChannel)
         val titleText = "Android设备USB通信框架"
         val message = "开发者专用服务(不建议用户启动)"
        // 创建通知构建器，并应用自定义布局和图标
    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_xyn_dat)
        .setContentTitle(titleText) // 通知标题
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true) // 设置为可取消
        .setOngoing(true)

        return builder.build()
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
