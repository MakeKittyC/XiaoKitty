package app.ktx

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.Handler
import android.os.Looper
import android.os.Environment

import app.compile.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics

import android.content.res.Resources
import android.content.res.Configuration
import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.content.pm.PackageManager
import android.content.DialogInterface

import androidx.appcompat.widget.AppCompatSeekBar
import androidx.cardview.widget.CardView

import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.provider.Settings

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Icon
import android.graphics.drawable.Drawable

import android.view.Display
import android.view.View
import android.view.WindowManager
import android.view.Menu
import android.view.MenuItem
import android.view.animation.LinearInterpolator

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider

import android.net.Uri
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import android.annotation.SuppressLint
import android.animation.AnimatorSet
import android.animation.ObjectAnimator

import java.security.Signature
import java.security.NoSuchAlgorithmException
import java.util.ArrayList
import java.util.Collections
import java.util.List
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

public class TaskManager(public val context: Context) {

    public val items = arrayOf(
        "Su: 15 Hz",
        "Su: 30 Hz",
        "Su: 45 Hz",
        "Su: 60 Hz",
        "Su: 90 Hz",
        "Su: 120 Hz",
        "Su: 144 Hz",
        "Su: 165 Hz",
        "Su: 200 Hz",
        "Su: 240 Hz",
        "Su: 300 Hz",
        "Su: 360 Hz",
        "Su: 720 Hz",
        "Su: 1080 Hz",
        "Su: 1200 Hz",
        "Su: 2400 Hz",
        "Su: 3600 Hz",
        "Su: 7200 Hz",
        "Su: 9999 Hz"
    )

    public var selectedIndex = 0

    public fun showRefreshRateDialog() {
        val builder = MaterialAlertDialogBuilder(context)
            .setTitle("可选屏幕刷新率")
            .setSingleChoiceItems(items, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton("执行") { _, _ ->
                setRefreshRate(selectedIndex)
            }
            .setNegativeButton("取消") { _, _ ->
                Toast.makeText(context, "好的呢! 猫猫知道啦~", Toast.LENGTH_SHORT).show()
            }

        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        
        dialog.window?.decorView?.backgroundTintList = ContextCompat.getColorStateList(context, R.color.task_dialog_background)
    }

    public fun setRefreshRate(index: Int) {
        when (index) {
            0 -> set15HzRefreshRate()
            1 -> set30HzRefreshRate()
            2 -> set45HzRefreshRate()
            3 -> set60HzRefreshRate()
            4 -> set90HzRefreshRate()
            5 -> set120HzRefreshRate()
            6 -> set144HzRefreshRate()
            7 -> set165HzRefreshRate()
            8 -> set200HzRefreshRate()
            9 -> set240HzRefreshRate()
            10 -> set300HzRefreshRate()
            11 -> set360HzRefreshRate()
            12 -> set720HzRefreshRate()
            13 -> set1080HzRefreshRate()
            14 -> set1200HzRefreshRate()
            15 -> set2400HzRefreshRate()
            16 -> set3600HzRefreshRate()
            17 -> set7200HzRefreshRate()
            18 -> set9999HzRefreshRate()
            else -> {}
        }
    }

    public fun set15HzRefreshRate() {
        val success = setRefreshRateViaCommand("15", "15", "15", "15", "15", "15")
        showToast(success, "15")
    }
    
    public fun set30HzRefreshRate() {
        val success = setRefreshRateViaCommand("30", "30", "30", "30", "30", "30")
        showToast(success, "30")
    }
    
    public fun set45HzRefreshRate() {
        val success = setRefreshRateViaCommand("45", "45", "45", "45", "45", "45")
        showToast(success, "45")
    }
    
    public fun set60HzRefreshRate() {
        val success = setRefreshRateViaCommand("60", "60", "60", "60", "60", "60")
        showToast(success, "60")
    }

    public fun set90HzRefreshRate() {
        val success = setRefreshRateViaCommand("90", "90", "90", "90", "90", "90")
        showToast(success, "90")
    }

    public fun set120HzRefreshRate() {
        val success = setRefreshRateViaCommand("120", "120", "120", "120", "120", "120")
        showToast(success, "120")
    }

    public fun set144HzRefreshRate() {
        val success = setRefreshRateViaCommand("144", "144", "144", "144", "144", "144")
        showToast(success, "144")
    }
    
    public fun set165HzRefreshRate() {
        val success = setRefreshRateViaCommand("165", "165", "165", "165", "165", "165")
        showToast(success, "165")
    }
    
    public fun set200HzRefreshRate() {
        val success = setRefreshRateViaCommand("200", "200", "200", "200", "200", "200")
        showToast(success, "200")
    }
    
    public fun set240HzRefreshRate() {
        val success = setRefreshRateViaCommand("240", "240", "240", "240", "240", "240")
        showToast(success, "240")
    }
    
    public fun set300HzRefreshRate() {
        val success = setRefreshRateViaCommand("300", "300", "300", "300", "300", "300")
        showToast(success, "300")
    }
    
    public fun set360HzRefreshRate() {
        val success = setRefreshRateViaCommand("360", "360", "360", "360", "360", "360")
        showToast(success, "360")
    }
    
    public fun set720HzRefreshRate() {
        val success = setRefreshRateViaCommand("720", "720", "720", "720", "720", "720")
        showToast(success, "720")
    }
    
    public fun set1080HzRefreshRate() {
        val success = setRefreshRateViaCommand("1080", "1080", "1080", "1080", "1080", "1080")
        showToast(success, "1080")
    }
    
    public fun set1200HzRefreshRate() {
        val success = setRefreshRateViaCommand("1200", "1200", "1200", "1200", "1200", "1200")
        showToast(success, "1200")
    }
    
    public fun set2400HzRefreshRate() {
        val success = setRefreshRateViaCommand("2400", "2400", "2400", "2400", "2400", "2400")
        showToast(success, "2400")
    }
    
    public fun set3600HzRefreshRate() {
        val success = setRefreshRateViaCommand("3600", "3600", "3600", "3600", "3600", "3600")
        showToast(success, "3600")
    }
    
    public fun set7200HzRefreshRate() {
        val success = setRefreshRateViaCommand("7200", "7200", "7200", "7200", "7200", "7200")
        showToast(success, "7200")
    }
    
    public fun set9999HzRefreshRate() {
        val success = setRefreshRateViaCommand("9999", "9999", "9999", "9999", "9999", "9999")
        showToast(success, "9999")
    }
    
public fun setRefreshRateViaCommand(
    userRefreshRate: String,
    refreshRateMode: String,
    miuiRefreshRate: String,
    peakRefreshRate: String,
    minRefrshRate: String,
    vivoRefreshRateMode: String
): Boolean {
    return try {
        val process1 =  Runtime.getRuntime().exec("su -c settings put global user_refresh_rate $userRefreshRate")
        val process2 =  Runtime.getRuntime().exec("su -c settings put global refresh_rate_mode $refreshRateMode")
        val process3 =  Runtime.getRuntime().exec("su -c settings put global miui_refresh_rate $miuiRefreshRate")
        val process4 =  Runtime.getRuntime().exec("su -c settings put global peak_refresh_rate $peakRefreshRate")
        val process5 =  Runtime.getRuntime().exec("su -c settings put global min_refresh_rate $minRefrshRate")
        val process6 =  Runtime.getRuntime().exec("su -c settings put global vivo_screen_refresh_rate_mode $vivoRefreshRateMode")
        val process7 =  Runtime.getRuntime().exec("su -c settings put secure user_refresh_rate $userRefreshRate")
        val process8 =  Runtime.getRuntime().exec("su -c settings put secure refresh_rate_mode $refreshRateMode")
        val process9 =  Runtime.getRuntime().exec("su -c settings put secure miui_refresh_rate $miuiRefreshRate")
        val process10 =  Runtime.getRuntime().exec("su -c settings put secure peak_refresh_rate $peakRefreshRate")
        val process11 =  Runtime.getRuntime().exec("su -c settings put secure min_refresh_rate $minRefrshRate")
        val process12 =  Runtime.getRuntime().exec("su -c settings put secure vivo_screen_refresh_rate_mode $vivoRefreshRateMode")
        val process13 =  Runtime.getRuntime().exec("su -c settings put system user_refresh_rate $userRefreshRate")
        val process14 =  Runtime.getRuntime().exec("su -c settings put system refresh_rate_mode $refreshRateMode")
        val process15 =  Runtime.getRuntime().exec("su -c settings put system miui_refresh_rate $miuiRefreshRate")
        val process16 =  Runtime.getRuntime().exec("su -c settings put system peak_refresh_rate $peakRefreshRate")
        val process17 =  Runtime.getRuntime().exec("su -c settings put system min_refresh_rate $minRefrshRate")
        val process18 =  Runtime.getRuntime().exec("su -c settings put system vivo_screen_refresh_rate_mode $vivoRefreshRateMode")

     process1.waitFor() == 0 && process2.waitFor() == 0 && process3.waitFor() == 0 && process4.waitFor() == 0 && process5.waitFor() == 0 && process6.waitFor() == 0 && process7.waitFor() == 0 && process8.waitFor() == 0 && process9.waitFor() == 0 && process10.waitFor() == 0 && process11.waitFor() == 0 && process12.waitFor() == 0 && process13.waitFor() == 0 && process14.waitFor() == 0 && process15.waitFor() == 0
    } catch (e: IOException) {
        false
    }
}

    public fun showToast(success: Boolean, rate: String) {
       Toast.makeText(
            context,
               if (success) "执行成功: 已设置刷新率为: $rate" else "执行失败: app权限不足",
           Toast.LENGTH_SHORT
         ).show()
    }


    public fun getRefreshRateFromCommand(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(command)
            val inputStream = process.inputStream
            val output = inputStream.bufferedReader().use { it.readText() }
            output.trim()
        } catch (e: IOException) {
            "N/A"
        }
    }
}
