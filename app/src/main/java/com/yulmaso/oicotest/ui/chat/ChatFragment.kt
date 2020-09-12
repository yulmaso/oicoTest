package com.yulmaso.oicotest.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yulmaso.oicotest.R
import com.yulmaso.oicotest.data.Quote
import com.yulmaso.oicotest.databinding.FragmentChatBinding
import com.yulmaso.oicotest.ui.ErrorListener
import com.yulmaso.oicotest.utils.Constants.LIMIT
import kotlinx.android.synthetic.main.activity_main.*

class ChatFragment : Fragment(), ChatAdapter.ChatListener, ErrorListener {

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ChatAdapter(this)
        viewModel.errorListener = this
        viewModel.getQuotes(LIMIT, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChatBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            chatRv.adapter = adapter

            chatRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = (chatRv.layoutManager as LinearLayoutManager).itemCount
                    val visibleItemCount = (chatRv.layoutManager as LinearLayoutManager).childCount
                    val firstVisibleItem = (chatRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (totalItemCount - visibleItemCount <= firstVisibleItem) {
                        viewModel.getQuotes(4, true)
                    }
                }
            })
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            adapter.setItems(viewModel.fullList)
        }

        viewModel.newQuotes.observeEvent(viewLifecycleOwner, Observer {
            adapter.addItems(it)
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.maxOffset = 0
    }

    override fun onItemClick(item: Quote) {
        val bundle = bundleOf(Pair("id", item.id))
        findNavController().navigate(R.id.action_chatFragment_to_detailsFragment, bundle)
    }


    override fun showError(message: String) {
        Snackbar.make(requireActivity().root, "Error: $message", Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.snackbar_retry)) {
                viewModel.getQuotes(LIMIT, false)
            }.show()
    }
}