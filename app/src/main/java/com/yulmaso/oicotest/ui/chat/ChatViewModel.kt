package com.yulmaso.oicotest.ui.chat

import android.app.Application
import android.util.Log
import com.yulmaso.oicotest.R
import com.yulmaso.oicotest.data.Quote
import com.yulmaso.oicotest.data.Repository
import com.yulmaso.oicotest.ui.BaseViewModel
import com.yulmaso.oicotest.utils.Constants
import com.yulmaso.oicotest.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers

class ChatViewModel(app: Application) : BaseViewModel(app) {

    private val repository = Repository.getInstance()

    private val paginator = PublishProcessor.create<Int>().also { subscribeForQuotes(it) }

    val listDataChanged = SingleLiveEvent<Nothing>()

    val newQuotes = SingleLiveEvent<List<Quote>>()

    val fullList: MutableList<Quote> = ArrayList()

    var loading = false

    // хранит последний оффсет
    private var maxOffset: Int = 0

    // запрещает загрузку элементов, если все цитаты уже выгружены
    private var reachedEnd: Boolean = false

    fun refreshData() {
        maxOffset = 0
        reachedEnd = false

        paginator.onNext(Constants.DEFAULT_QUOTES_COUNT)
    }

    fun getMoreQuotes(count: Int) {
        paginator.onNext(count)
    }

    private fun subscribeForQuotes(paginator: PublishProcessor<Int>) {
        disposables.add(paginator
            .filter { !reachedEnd }
            .doOnNext {
                showProgress()
                loading = true
            }
            .concatMapSingle { count ->
                repository.getQuotesList(count, maxOffset)
                    .subscribeOn(Schedulers.io())
                    .doOnError { error.postValue(getApplication<Application>().getString(R.string.network_error)) }
                    .onErrorReturn { ArrayList() }
                    .doAfterTerminate { hideProgress() }
                    .map { data -> Pair(count, data) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pair ->
                // Обнуление списка при рефреше
                if (maxOffset == 0) {
                    fullList.clear()
                    listDataChanged.call()
                }

                if (pair.second.size < pair.first - 1) {
                    reachedEnd = true
                }
                newQuotes.postValue(pair.second)

                maxOffset += pair.second.size
                loading = false
            })
    }
}