package com.yulmaso.oicotest.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yulmaso.oicotest.data.Quote
import com.yulmaso.oicotest.databinding.ItemChatBinding
class ChatAdapter(
    private val chatListener: ChatListener,
    private val items: MutableList<Quote>
): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    interface ChatListener {
        fun onItemClick(item: Quote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatViewHolder(ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) =
        holder.bind(items[position])

    fun addItems(items: List<Quote>) {
        val oldSize = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(oldSize, items.size)
    }

    inner class ChatViewHolder(
        private val binding: ItemChatBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Quote){
            binding.quote = item
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            chatListener.onItemClick(items[layoutPosition])
        }
    }
}