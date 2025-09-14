package app.prop

import android.app.Application
import android.app.Dialog
import android.util.Log
import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Build
import android.os.Process
import java.io.IOException
import java.io.BufferedReader
import app.compile.R
import app.compile.BuildConfig
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.widget.Toast

public class Prop : Application() {

companion object {
    init {
        System.loadLibrary("${BuildConfig.CPP_NAME}")
    }
    
}
    override fun onCreate() {
        super.onCreate()
        addStartupDelay()
    }
    
    private fun addStartupDelay() {
        try {
            Thread.sleep(700)
        } catch (e: InterruptedException) {
            e.printStackTrace()
           } finally {
           
        }
    }
}