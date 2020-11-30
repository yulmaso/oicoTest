package com.yulmaso.oicotest.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yulmaso.oicotest.R
import com.yulmaso.oicotest.data.Quote
import com.yulmaso.oicotest.databinding.FragmentChatBinding
import com.yulmaso.oicotest.ui.BaseFragment
import com.yulmaso.oicotest.utils.Constants.DEFAULT_QUOTES_COUNT

class ChatFragment : BaseFragment(), ChatAdapter.ChatListener{

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ChatAdapter(this, viewModel.fullList)
        if (viewModel.fullList.isEmpty()) viewModel.refreshData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChatBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            chatRv.adapter = adapter
            val lm = chatRv.layoutManager as LinearLayoutManager

            chatRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    
                    val totalItemCount = lm.itemCount
                    val visibleItemCount = lm.childCount
                    val firstVisibleItem = lm.findFirstVisibleItemPosition()

                    if (!viewModel.loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
                        viewModel.getMoreQuotes(4)
                    }
                }
            })
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.newQuotes.observeEvent(viewLifecycleOwner, Observer {
            adapter.addItems(it)
        })

        viewModel.listDataChanged.observeEvent(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })

        viewModel.error.observeEvent(viewLifecycleOwner, Observer {
            showMessage(it)
        })
    }

    override fun onItemClick(item: Quote) {
        val bundle = bundleOf(Pair("id", item.id))
        findNavController().navigate(R.id.action_chatFragment_to_detailsFragment, bundle)
    }
}