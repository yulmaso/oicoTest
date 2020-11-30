package com.yulmaso.oicotest.ui

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 *  Created by yulmaso
 *  Date: 30.11.20
 */
open class BaseFragment: Fragment() {

    private var snackbar: Snackbar? = null

    protected fun showMessage(text: String) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }
}