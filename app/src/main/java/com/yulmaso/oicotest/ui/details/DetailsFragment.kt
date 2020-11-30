package com.yulmaso.oicotest.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yulmaso.oicotest.databinding.FragmentDetailsBinding
import com.yulmaso.oicotest.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : BaseFragment() {

    private val viewModel: DetailsViewModel by viewModels()

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getQuote(args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDetailsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            detailsBack.setOnClickListener{
                findNavController().popBackStack()
            }
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.quote.observe(viewLifecycleOwner, Observer {
            details_tv.text = it.text
            details_tag_layout.tags = it.tagList
        })

        viewModel.error.observeEvent(viewLifecycleOwner, Observer {
            showMessage(it)
        })
    }
}