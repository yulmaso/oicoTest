package com.yulmaso.oicotest.ui

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel: ViewModel() {
    protected var disposables = CompositeDisposable()

    var errorListener: ErrorListener? = null

    val progress = ObservableBoolean(false)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}