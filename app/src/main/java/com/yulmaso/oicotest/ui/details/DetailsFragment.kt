package com.yulmaso.oicotest.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yulmaso.oicotest.R
import com.yulmaso.oicotest.databinding.FragmentDetailsBinding
import com.yulmaso.oicotest.ui.ErrorListener
import com.yulmaso.oicotest.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(), ErrorListener {

    private val viewModel: DetailsViewModel by viewModels()

    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = arguments?.get("id") as Int

        viewModel.errorListener = this
        viewModel.getQuote(id!!)
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
    }

    override fun showError(message: String) {
        Snackbar.make(requireActivity().root, "Error: $message", Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.snackbar_retry)) {
                viewModel.getQuote(id!!)
            }.show()
    }
}