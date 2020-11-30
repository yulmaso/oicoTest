package com.yulmaso.oicotest.ui.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yulmaso.oicotest.R
import com.yulmaso.oicotest.data.Quote
import com.yulmaso.oicotest.data.Repository
import com.yulmaso.oicotest.ui.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsViewModel(app: Application): BaseViewModel(app) {

    private val repository = Repository.getInstance()

    private val quoteMLive = MutableLiveData<Quote>()
    val quote: LiveData<Quote> = quoteMLive

    fun getQuote(id: Int) {
        showProgress()
        disposables.add(
            repository.getQuote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { hideProgress() }
                .subscribe({
                    quoteMLive.postValue(it)
                }, {
                    error.postValue(getApplication<Application>().getString(R.string.network_error))
                })
        )
    }
}