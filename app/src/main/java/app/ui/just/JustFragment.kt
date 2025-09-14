package app.ui.just

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.compile.databinding.FragmentJustBinding
import app.compile.R
import app.compile.BuildConfig
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.File
import java.util.ArrayDeque
import java.util.Stack
import app.ui.just.FileAdapter
import app.ui.just.FileItem
import app.ui.just.RootFileAdapter
import app.ui.just.RootFileItem

public class JustFragment : Fragment() {

companion object {
    init {
        System.loadLibrary("${BuildConfig.CPP_NAME}")
    }
    
}
    private var _binding: FragmentJustBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var currentPathTextView: TextView
    private lateinit var fileAdapter: FileAdapter
    private lateinit var rootFileAdapter: RootFileAdapter
    private var currentPath: String = "/sdcard/Android/data/${BuildConfig.APPLICATION_ID}/"
    private val historyStack = Stack<String>()
    private var currentPath1: String = "/data/data/${BuildConfig.APPLICATION_ID}/"
    private val historyStack1 = Stack<String>()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    
    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
              handleBackPress()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化权限请求的 ActivityResultLauncher
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 权限请求被允许，加载文件
                loadFiles(currentPath)
                loadFiles1(currentPath1)
            } else {
                // 权限请求被拒绝，处理相应逻辑
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val justViewModel =
            ViewModelProvider(this).get(JustViewModel::class.java)
        _binding = FragmentJustBinding.inflate(inflater, container, false)
        val root: View = binding.root
        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView1 = binding.recyclerView1
        currentPathTextView = binding.currentPathTextView
        daysLoadFiles()
        
        // 注册回调
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun daysLoadFiles() {
          loadFiles(currentPath)
          loadFiles1(currentPath1)
    }
    
    private fun loadFiles1(path: String) {
        binding.currentPathTextView.text = path
        val files = getRootFilesInDirectory(path)
        rootFileAdapter = RootFileAdapter(files) { RootFileItem -> 
            if (RootFileItem.isDirectory) {
               historyStack1.push(currentPath1)
                currentPath1 = RootFileItem.path
                loadFiles1(currentPath1) // 递归加载
            }
        }
        binding.recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView1.adapter = rootFileAdapter
    }

    private fun loadFiles(path: String) {
        binding.currentPathTextView.text = path
        val files = getFilesInDirectory(path)
        fileAdapter = FileAdapter(files) { fileItem -> 
            if (fileItem.isDirectory) {
               historyStack.push(currentPath)
                currentPath = fileItem.path
                loadFiles(currentPath) // 递归加载
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = fileAdapter
    }

    private fun getRootFilesInDirectory(path: String): List<RootFileItem> {
        val directory = File(path)
        return if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles() ?: emptyArray()
            val folders = files.filter { it.isDirectory }.map { RootFileItem(it.name, it.absolutePath, true) }
            val regularFiles = files.filter { !it.isDirectory }.map { RootFileItem(it.name, it.absolutePath, false) }
            (folders + regularFiles).sortedBy { if (it.isDirectory) 0 else 1 } // 将文件夹放置在前面
        } else {
            emptyList()
        }
    }

    private fun getFilesInDirectory(path: String): List<FileItem> {
        val directory = File(path)
        return if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles() ?: emptyArray()
            val folders = files.filter { it.isDirectory }.map { FileItem(it.name, it.absolutePath, true) }
            val regularFiles = files.filter { !it.isDirectory }.map { FileItem(it.name, it.absolutePath, false) }
            (folders + regularFiles).sortedBy { if (it.isDirectory) 0 else 1 } // 将文件夹放置在前面
        } else {
            emptyList()
        }
    }
    
    private fun handleBackPress() {
        // 分别处理两个栈
        var handled = false

        if (historyStack.isNotEmpty()) {
            handleFirstStack()
            handled = true // 标记为已处理
        }

        if (historyStack1.isNotEmpty()) {
            handleSecondStack()
            handled = true // 标记为已处理
        }

        // 动态更新回调状态
        backPressCallback.isEnabled = handled
    }

private fun handleFirstStack() {
    if (historyStack.isNotEmpty()) {
        currentPath = historyStack.pop() // 回退到上一级路径
        loadFiles(currentPath) // 加载上一级文件
    }
}

private fun handleSecondStack() {
    if (historyStack1.isNotEmpty()) {
        currentPath1 = historyStack1.pop() // 从第二个栈中获取上一个路径
        loadFiles1(currentPath1) // 加载上一级文件
     }
  }
}