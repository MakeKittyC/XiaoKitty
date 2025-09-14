package app.ui.vote.ui.arts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import app.compile.databinding.FragmentArtsBinding
import app.compile.R
import app.compile.BuildConfig
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.ui.vote.ui.arts.ArtsAdapter
import app.ui.vote.ui.arts.ArtItem
import app.ui.vote.ui.arts.ArtsViewModel
import app.TaskActivity

import android.content.Context
import androidx.core.content.ContextCompat
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.method.LinkMovementMethod

public class ArtsFragment : Fragment(R.layout.fragment_arts), ItemClickListener {

companion object {
    init {
        System.loadLibrary("${BuildConfig.CPP_NAME}")
    }
    
}
    private var _binding: FragmentArtsBinding? = null
    private val binding get() = _binding!!
    private var androidxConstraintLayout: ConstraintLayout? = null
    
    private lateinit var artsViewModel: ArtsViewModel
    private lateinit var artsAdapter: ArtsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artsViewModel =
            ViewModelProvider(this).get(ArtsViewModel::class.java)
        _binding = FragmentArtsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        androidxConstraintLayout = binding.androidxConstraintLayout
        
    // 确保在这里初始化 artsViewModel
        artsViewModel = ViewModelProvider(this).get(ArtsViewModel::class.java)
        
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        artsAdapter = ArtsAdapter(requireContext().resources, emptyList(), this) 
        binding.recyclerView.adapter = artsAdapter

        // 观察 ViewModel 中的 artItems
        artsViewModel.artItems.observe(viewLifecycleOwner, Observer { items ->
            artsAdapter = ArtsAdapter(requireContext().resources, items, this) // 更新适配器数据
            binding.recyclerView.adapter = artsAdapter
        })
        
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isScrollingDown = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
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
    
    override fun onItemClick(link: String) {
        openLink(link)
    }
    
    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val browsers = listOf("com.android.chrome", "com.chrome.beta", "com.chrome.dev", "com.chrome.canary", "com.android.browser")

        var isBrowserFound = false

        for (browser in browsers) {
            intent.setPackage(browser)
            try {
                startActivity(intent)
                isBrowserFound = true
                break
            } catch (e: Exception) {
                // 捕获异常，继续尝试下一个浏览器
            }
        }

        if (!isBrowserFound) {
            intent.setPackage(null)
            startActivity(intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        androidxConstraintLayout = null
        
    }
}