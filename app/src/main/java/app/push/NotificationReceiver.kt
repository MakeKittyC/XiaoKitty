package app.push

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

import android.util.Log

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager

import android.os.IBinder
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.RemoteException

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.BroadcastReceiver

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

import android.app.Service
import android.app.PendingIntent
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationChannelGroup

import android.net.Uri
import android.net.ConnectivityManager

import android.provider.Settings

import java.util.concurrent.TimeUnit
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import app.compile.R
import app.TaskActivity
import app.compile.BuildConfig

public class NotificationReceiver : BroadcastReceiver() {

    companion object {
        init {
           System.loadLibrary("${BuildConfig.CPP_NAME}")
        }
        private const val CHANNEL_ID = "Msg"
        private const val CHANNEL_NAME = "消息通知"
        private const val NOTIFICATION_ID = 1234
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent?.action == "XiaoKitty.HyperOS.Class.USB_PERMISSION") {
            // 使用新的 getParcelableExtra 方法
            val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) && device != null) {
                Log.d("USB", "Permission granted for device: ${device.deviceName}")
            } else {
                Log.d("USB", "Permission denied for device: ${device?.deviceName}")
            }
        }
        when (intent.action) {
        "ACTION_REPLY" -> {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            val replyText = remoteInput?.getString("reply_key")

            if (!replyText.isNullOrEmpty()) {
                // 处理实际回复逻辑，例如显示回复或发送到服务器
                Toast.makeText(context, "收到回复: $replyText", Toast.LENGTH_SHORT).show()

                // 更新原来的通知
                updateOriginalNotification(context, replyText)
            } else {
                Log.e("BroadcastReceiver", "回复内容为空")
            }
        }
        // 原有的通知触发逻辑
        "ACTION_SHOW_NOTIFICATION" -> {
            // 原有代码...
            Toast.makeText(context, "广播已接收，通知已发送", Toast.LENGTH_SHORT).show()
            
            // 从 SharedPreferences 读取默认标题和用户名
            val sharedPreferences = context.getSharedPreferences("new_message", Context.MODE_PRIVATE)
            val defaultTitle = sharedPreferences.getString("title", "默认通知") ?: "默认通知"
            val defaultPersonName = sharedPreferences.getString("personName", "默认用户名") ?: "默认用户名"

           // 从 Intent 中获取消息内容
           val message = intent.getStringExtra("message") ?: "默认消息" // 直接使用默认消息
           val title = intent.getStringExtra("title") ?: defaultTitle
           val personName = intent.getStringExtra("personName") ?: defaultPersonName
           val oldMessage = intent.getStringExtra("oldMessage")

            createNotificationChannel(context)

        val notification = buildMessagingNotification(
            context,
            title,
            message,
            personName,
            oldMessage
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
          }
          // 标为已读
          "ACTION_MARK_AS_READ" -> {
            // 处理标记为已读的逻辑
            val readMessage = "消息已读"
            val personName = "小猫猫" // 指定的用户名
            updateNotificationAsRead(context, readMessage, personName)
            
          }
       }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
               // 默认 NotificationManager.IMPORTANCE_DEFAULT
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
           description = "消息样式类通知-MessagingStyle"
        }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun updateNotificationAsRead(context: Context, message: String, personName: String) {
    // 指定的标题
    val title = "新消息" // 更新的标题

    // 创建更新后的通知
    val notification = buildMessagingNotification(
        context,
        title,
        message,
        personName
    )

    // 获取 NotificationManager
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 使用相同的通知 ID 更新通知
    notificationManager.notify(NOTIFICATION_ID, notification)
}

    private fun updateOriginalNotification(context: Context, replyText: String) {
    // 从 SharedPreferences 读取配置
    val sharedPreferences = context.getSharedPreferences("new_message", Context.MODE_PRIVATE)
    val title = sharedPreferences.getString("titles", "新回复") ?: "新回复" // 默认值
    val personName = sharedPreferences.getString("personNames", "小猫猫") ?: "小猫猫" // 默认值

    // 使用读取的标题和用户名构建通知
    val message = replyText

    // 重新构建通知
    val notification = buildMessagingNotification(
        context,
        title,
        message,
        personName
    )

    // 获取 NotificationManager
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 使用相同的通知 ID 更新通知
    notificationManager.notify(NOTIFICATION_ID, notification)
}
    
    private fun buildMessagingNotification(
        context: Context,
        title: String,
        message: String,
        personName: String,
        oldMessage: String? = null
    ): Notification {
    
    // 从 SharedPreferences 读取头像路径
    val sharedPreferences = context.getSharedPreferences("new_message", Context.MODE_PRIVATE)
    val avatarPath = sharedPreferences.getString("imgPath", null)
    
    // 从配置文件读取时间字符串
    val timeString = sharedPreferences.getString("notification_time", null)
    val notificationTime = timeString?.let {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it)?.time
    } ?: System.currentTimeMillis() // 如果没有时间，则使用当前时间

    // 根据头像路径加载头像，使用默认头像
    val userAvatar = if (avatarPath != null) {
        BitmapFactory.decodeFile(avatarPath) // 从文件路径加载头像
    } else {
        BitmapFactory.decodeResource(context.resources, R.mipmap.image_004) // 默认头像
    }

    val circularAvatar = getCircularBitmap(userAvatar)
    val personIcon = IconCompat.createWithBitmap(circularAvatar)

    // 直接定义字符串常量
    val replyLabel = "输入回复"
    val markAsReadLabel = "标为已读"
    
    val keyTextReply = "reply_key" // 假设这是用于 RemoteInput 的键

    val remoteInput: RemoteInput = RemoteInput.Builder(keyTextReply).setLabel(replyLabel).build()

    // 创建或获取 Conversation 对象
    val conversation = Conversation(context) // 确保你创建了一个 Conversation 对象
    val conversationId = conversation.getConversationId() // 调用 getConversationId()
    val replyIntent = Intent(context, NotificationReceiver::class.java).apply {
        action = "ACTION_REPLY"
    }
    
    val markAsReadIntent = Intent(context, NotificationReceiver::class.java).apply {
        action = "ACTION_MARK_AS_READ"
    }
    
    val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context,
        conversationId,
        replyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE // 这里使用 FLAG_MUTABLE
    )
    
    val markAsReadPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context,
        conversationId + 1, // 确保唯一性
        markAsReadIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )

    val action: NotificationCompat.Action = NotificationCompat.Action.Builder(
        R.drawable.ic_xyn_telegram, // 确保该资源存在
        replyLabel,
        replyPendingIntent
    ).addRemoteInput(remoteInput)
     .build()
     
     // 创建标记为已读动作
    val markAsReadAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
        R.drawable.ic_xyn_telegram, // 确保这个图标存在
        markAsReadLabel,
        markAsReadPendingIntent
    ).build()

    val person = Person.Builder()
        .setName(personName)
        .setIcon(personIcon)
        .setImportant(true)
        .build()

        val messagingStyle = NotificationCompat.MessagingStyle(person)
        .setConversationTitle(title)
        // .setGroupConversation(true)
        .addMessage(message, notificationTime, person)

        if (oldMessage != null) {
            messagingStyle.addHistoricMessage(
                NotificationCompat.MessagingStyle.Message(
                    oldMessage,
                    notificationTime - TimeUnit.DAYS.toMillis(3650),
                    person
                )
            )
        }
        
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.image_004)
            .setLargeIcon(circularAvatar)
            .setStyle(messagingStyle)
            .setShowWhen(true)
            .setWhen(notificationTime) // 设置具体时间
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(message)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(action)
            .addAction(markAsReadAction)
            .build()
    }
    
    /* 辅助方法：将 Bitmap 转换为圆形
private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val size = Math.min(width, height)
    
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, size, size)
    val rectF = RectF(rect)

    paint.isAntiAlias = true
    canvas.drawOval(rectF, paint)
    
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, Rect(0, 0, width, height), rect, paint)

    return output
   } */
   
private fun getCircularBitmap(bitmap: Bitmap?): Bitmap {
    if (bitmap == null) {
        throw IllegalArgumentException("Bitmap cannot be null")
    }

    val width = bitmap.width
    val height = bitmap.height
    val size = Math.min(width, height)

    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, size, size)
    val rectF = RectF(rect)

    paint.isAntiAlias = true
    canvas.drawOval(rectF, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, Rect(0, 0, width, height), rect, paint)

     return output
   }
}