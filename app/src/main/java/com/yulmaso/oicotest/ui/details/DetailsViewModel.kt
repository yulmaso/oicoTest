package com.yulmaso.oicotest.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yulmaso.oicotest.data.Quote
import com.yulmaso.oicotest.data.Repository
import com.yulmaso.oicotest.ui.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsViewModel: BaseViewModel() {

    private val repository = Repository.getInstance()

    private val quoteMLive = MutableLiveData<Quote>()
    val quote: LiveData<Quote> = quoteMLive

    fun getQuote(id: Int) {
        progress.set(true)
        disposables.add(
            repository.getQuote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate{ progress.set(false) }
                .subscribe({
                    quoteMLive.postValue(it)
                }, {
                    errorListener?.showError(it.localizedMessage)
                })
        )
    }
}