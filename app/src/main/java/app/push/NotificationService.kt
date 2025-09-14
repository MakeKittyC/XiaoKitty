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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

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
import android.os.Binder
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
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

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

public class NotificationService : Service() {

    companion object {
        private const val CHANNEL_ID = "Shell"
        private const val CHANNEL_NAME = "交互通知"
        private const val NOTIFICATION_ID = 1730
    }

    override fun onCreate() {
        super.onCreate()
        
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "ACTION_SHOW_NOTIFICATION" -> handleShowNotification(it)
                "ACTION_REPLY" -> handleReply(it)
                "ACTION_MARK_AS_READ" -> handleMarkAsRead()
            }
        }
        return START_STICKY
    }

    private fun handleShowNotification(intent: Intent) {
        createNotificationChannel()

        val title = intent.getStringExtra("title") ?: "默认通知"
        val message = intent.getStringExtra("message") ?: "默认消息"
        val personName = intent.getStringExtra("personName") ?: "默认用户名"

        val notification = buildMessagingNotification(title, message, personName)

        // Start the service in the foreground
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun handleReply(intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val replyText = remoteInput?.getString("reply_key")

        if (!replyText.isNullOrEmpty()) {
            Toast.makeText(this, "收到回复: $replyText", Toast.LENGTH_SHORT).show()
            updateOriginalNotification(replyText)
        } else {
            Log.e("NotificationService", "回复内容为空")
        }
    }

    private fun handleMarkAsRead() {
        val readMessage = "消息已读"
        val personName = "小猫猫"
        updateNotificationAsRead(readMessage, personName)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
          description = "此通知用于与其他用户更好的进行交互-MessagingStyle"
        }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotificationAsRead(message: String, personName: String) {
        val title = "新消息"
        val notification = buildMessagingNotification(title, message, personName)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun updateOriginalNotification(replyText: String) {
        val title = "新回复"
        val personName = "小猫猫"
        val notification = buildMessagingNotification(title, replyText, personName)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildMessagingNotification(title: String, message: String, personName: String): Notification {
       
       val circularAvatar = getCircularBitmap(BitmapFactory.decodeResource(resources, R.mipmap.image_008))
    val personIcon = IconCompat.createWithBitmap(circularAvatar)
    
        val person = Person.Builder()
            .setName(personName)
            .setIcon(personIcon)
            .build()
        
        val notificationTime = System.currentTimeMillis()

        val replyLabel = "输入回复"
        val keyTextReply = "reply_key"
        val remoteInput: RemoteInput = RemoteInput.Builder(keyTextReply).setLabel(replyLabel).build()

        val replyIntent = Intent(this, NotificationService::class.java).apply {
            action = "ACTION_REPLY"
        }

        val markAsReadIntent = Intent(this, NotificationService::class.java).apply {
            action = "ACTION_MARK_AS_READ"
        }

        val replyPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val markAsReadPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            1,
            markAsReadIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val action: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.ic_xyn_telegram,
            replyLabel,
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val markAsReadAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.ic_xyn_telegram,
            "标为已读",
            markAsReadPendingIntent
        ).build()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.image_008)
            .setLargeIcon(circularAvatar)
            .setStyle(NotificationCompat.MessagingStyle(person)
                .setConversationTitle(title)
                .addMessage(message, notificationTime, person))
            .addAction(action)
            .addAction(markAsReadAction)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            .setAutoCancel(true)
            .build()
    }

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
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}