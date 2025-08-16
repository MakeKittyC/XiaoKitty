package app.ui.vote.ui.atek

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.LinearLayout
import android.content.Context
import android.content.res.AssetManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.compile.databinding.FragmentAtekBinding
import app.compile.R
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.NestedScrollView

import androidx.core.content.ContextCompat
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.method.LinkMovementMethod

import app.ui.vote.ui.atek.Item
import app.ui.vote.ui.atek.ItemAdapter
import app.ui.vote.ui.atek.ItemClickListener
import app.TaskActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay

public class AtekFragment : Fragment(), ItemClickListener {

companion object {
    init {
        System.loadLibrary("NonNull")
    }
    
}
    private var _binding: FragmentAtekBinding? = null
    private val binding get() = _binding!!
    
    private var androidx: ConstraintLayout? = null
    
    private lateinit var itemAdapter1: ItemAdapter
    private lateinit var itemList1: List<Item>
    
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val atekViewModel =
            ViewModelProvider(this).get(AtekViewModel::class.java)
        _binding = FragmentAtekBinding.inflate(inflater, container, false)
        val root: View = binding.root
        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        androidx = binding.androidx
        
        binding.recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        
    coroutineScope.launch {
        val itemList1 = loadJSONFromAsset(requireContext())
        itemAdapter1 = ItemAdapter(requireContext().resources, itemList1, this@AtekFragment)
        binding.recyclerView1.adapter = itemAdapter1
    }
        
        binding.recyclerView1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isScrollingDown = false

            override fun onScrolled(recyclerView1: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView1, dx, dy)
                val activity = requireActivity() as TaskActivity
                if (dy > 0 && !isScrollingDown) {
                    // 向下滚动，隐藏底部导航栏
                    activity.hideBottomNavigation()
                    isScrollingDown = true
                } else if (dy < 0 && isScrollingDown) {
                    // 向上滚动，显示底部导航栏
                    activity.showBottomNavigation()
                    isScrollingDown = false
                }
            }
        })
    }
    
   private suspend fun loadJSONFromAsset(context: Context): List<Item> {
    return withContext(Dispatchers.Main) {
        val assetManager: AssetManager = context.assets
        val inputStream = assetManager.open("linkdata.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val gson = Gson()
        gson.fromJson(jsonString, object : TypeToken<List<Item>>() {}.type)
    }
}
    
    override fun onItemClick(link: String) {
        openLink(link)
    }

    private fun openLink(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    // 检查链接头并决定打开的应用
    when {
        url.startsWith("https://x.com/") -> {
            // 使用 Twitter 应用打开特定链接
            intent.setPackage("com.twitter.android")
        }
        url.startsWith("https://t.me/") -> {
            // 使用 Telegram 应用打开特定链接
            val telegramApps = listOf(
                "tw.nekomimi.nekogram",
                "org.telegram.messenger.web",
                "org.telegram.messenger",
                "org.telegram.messenger.beta"
            )

            for (app in telegramApps) {
                intent.setPackage(app)
                try {
                    startActivity(intent)
                    return // 成功启动后立即退出方法，避免重复
                } catch (e: Exception) {
                    // 捕获异常，继续尝试下一个 Telegram 应用
                }
            }

            // 如果没有找到 Telegram 应用，清除包名以使用默认浏览器
            intent.setPackage(null)
        }
        url.startsWith("https://play.google.com/") -> {
            // 使用 Google Play 商店打开链接
            intent.setPackage("com.android.vending")
        }
        url.startsWith("http://") || url.startsWith("https://") -> {
            // 如果是其他 http 或 https 链接，尝试打开指定的浏览器
            val browsers = listOf(
                "com.android.chrome",
                "com.chrome.beta",
                "com.chrome.dev",
                "com.chrome.canary",
                "com.android.browser"
            )

            for (browser in browsers) {
                intent.setPackage(browser)
                try {
                    startActivity(intent)
                    return // 成功启动后立即退出方法，避免重复
                } catch (e: Exception) {
                    // 捕获异常，继续尝试下一个浏览器
                }
            }

            // 如果没有找到任何浏览器，则清除包名以使用默认选择
            intent.setPackage(null)
        }
        // 其他类型的链接，使用默认选择
        else -> {
            intent.setPackage(null)
        }
    }

    // 最后的兜底逻辑
    try {
        startActivity(intent)
    } catch (e: Exception) {
        // 捕获可能发生的异常
        
    }
}
    
    override fun onResume() {
        super.onResume()
        // 启用保护
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun onPause() {
        super.onPause()
        // 恢复默认设置
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    // 可选：动态控制
    private fun toggleSecureMode(enable: Boolean) {
        if (enable) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel() // 取消所有协程
        androidx = null
    }
}