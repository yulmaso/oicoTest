package com.yulmaso.oicotest.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

/**
 *  Created by yulmaso
 *  Date: 30.11.20
 */
@BindingAdapter("bindVisibilityOrGone")
fun bindVisibility(view: View, visibility: MutableLiveData<Boolean>) {
    visibility.observeForever {
        when (it) {
            true -> view.visibility = View.VISIBLE
            else -> view.visibility = View.GONE
        }
    }
}