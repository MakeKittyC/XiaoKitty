package app.ui.vote.ui.uppk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.compile.databinding.FragmentUppkBinding
import app.compile.R
import androidx.core.widget.NestedScrollView

public class UppkFragment : Fragment() {

companion object {
    init {
        System.loadLibrary("NonNull")
    }
    
}
    private var _binding: FragmentUppkBinding? = null
    private val binding get() = _binding!!
    
    private var androidxNestedScrollView: NestedScrollView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val uppkViewModel =
            ViewModelProvider(this).get(UppkViewModel::class.java)
        _binding = FragmentUppkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        androidxNestedScrollView = binding.androidxNestedScrollView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        androidxNestedScrollView = null
        _binding = null
    }
}