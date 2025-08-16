package app.ui.vote.ui.atek

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.compile.R
import app.compile.databinding.AtekMenuBinding
import androidx.core.content.ContextCompat
import android.content.res.Resources
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.graphics.Color

class ItemAdapter(
    private val resources: Resources,
    private val itemList: List<Item>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: AtekMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.nameTextView.text = item.name
            binding.linkTextView.text = item.link
            
            setClickableLink(binding.nameTextView, item.name)
            setClickableLink(binding.linkTextView, item.link)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = AtekMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = itemList.size
    
    private fun setClickableLink(textView: TextView, text: String) {
        val regex = "(http://[\\S]+|https://[\\S]+)".toRegex()
        val matcher = regex.find(text)

        if (matcher != null) {
            val start = matcher.range.first
            val end = matcher.range.last + 1 // end 是闭区间，所以要加 1

            // 设置可点击的文本
            textView.text = SpannableString(text).apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        listener.onItemClick(matcher.value) // 点击时打开链接
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = true // 添加下划线
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                // 设置颜色
                val color = resources.getColor(R.color.link_text, null)
                setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            textView.movementMethod = LinkMovementMethod.getInstance() // 使文本可点击
        } else {
            // 如果没有匹配的链接，直接设置文本
            textView.text = SpannableString(text).apply {
                setSpan(ForegroundColorSpan(Color.BLACK), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}