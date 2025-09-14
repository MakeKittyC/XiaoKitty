package app.ui.vote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import app.compile.databinding.FragmentVoteBinding
import app.compile.R
import app.compile.BuildConfig
import app.ui.vote.ui.save.SaveFragment
import app.ui.vote.ui.atek.AtekFragment
import app.ui.vote.ui.uppk.UppkFragment
import app.ui.vote.ui.arts.ArtsFragment
import app.ui.vote.ui.aapt.AaptFragment
import app.ui.vote.ui.star.StarFragment
import app.ui.vote.ui.upar.UparFragment
import app.ui.vote.ViewPagerAdapter

public class VoteFragment : Fragment() {

companion object {
    init {
        System.loadLibrary("${BuildConfig.CPP_NAME}")
    }
    
}
    private var _binding: FragmentVoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val voteViewModel =
            ViewModelProvider(this).get(VoteViewModel::class.java)
        _binding = FragmentVoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(binding.viewPager)
        setupTabLayout()
    }
    
    private fun setupViewPager(viewPager: ViewPager2) {
      val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFragment(AtekFragment(), "Link")
        adapter.addFragment(ArtsFragment(), "GitHub")
        adapter.addFragment(SaveFragment(), "Shell")
        adapter.addFragment(StarFragment(), "Top1")
        adapter.addFragment(UparFragment(), "Top2")
        adapter.addFragment(UppkFragment(), "Top3")
        adapter.addFragment(AaptFragment(), "Top4")
        viewPager.adapter = adapter
    }
    
    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = (binding.viewPager.adapter as ViewPagerAdapter).getFragmentTitle(position)
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}