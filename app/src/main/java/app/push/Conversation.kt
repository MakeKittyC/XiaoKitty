package app.push

import android.content.Context

class Conversation(private val context: Context) {
    fun getConversationId(): Int {
        val sharedPreferences = context.getSharedPreferences("new_message", Context.MODE_PRIVATE)
        // 从 SharedPreferences 读取 conversationId，设置默认值为 3
        return sharedPreferences.getInt("conversationId", 3)
    }
}