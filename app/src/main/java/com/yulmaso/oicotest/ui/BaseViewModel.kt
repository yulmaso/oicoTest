package com.yulmaso.oicotest.ui

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yulmaso.oicotest.utils.SingleLiveEvent
import com.yulmaso.oicotest.utils.get
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel(app: Application): AndroidViewModel(app) {
    protected var disposables = CompositeDisposable()

    val error = SingleLiveEvent<String>()

    val progressBar = MutableLiveData(true)

    val swipeRefreshProgress = ObservableBoolean(false)

    protected fun showProgress() {
        if (!progressBar.get()) swipeRefreshProgress.set(true)
    }

    protected fun hideProgress() {
        if (progressBar.get()) progressBar.postValue(false)
        swipeRefreshProgress.set(false)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}