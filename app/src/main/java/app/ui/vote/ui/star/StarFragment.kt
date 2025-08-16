package app.ui.vote.ui.star

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.hardware.usb.UsbDeviceConnection

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.graphics.Color
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import android.widget.Toast
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.JavascriptInterface

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.app.AlertDialog
import android.app.Activity
import android.app.PendingIntent

import app.compile.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.core.widget.NestedScrollView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.constraintlayout.widget.ConstraintLayout

import app.compile.databinding.FragmentStarBinding

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

public class StarFragment : Fragment() {

companion object {
    init {
        System.loadLibrary("NonNull")
    }
    
}
    private var _binding: FragmentStarBinding? = null
    private val binding get() = _binding!!
    private var androidxNestedScrollView: NestedScrollView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val starViewModel =
            ViewModelProvider(this).get(StarViewModel::class.java)
        _binding = FragmentStarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        androidxNestedScrollView = binding.androidxNestedScrollView
        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        androidxNestedScrollView = null
    }
}