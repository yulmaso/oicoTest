package com.yulmaso.oicotest.ui.chat

import com.yulmaso.oicotest.data.Quote
import com.yulmaso.oicotest.data.Repository
import com.yulmaso.oicotest.ui.BaseViewModel
import com.yulmaso.oicotest.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ChatViewModel : BaseViewModel() {

    private val repository = Repository.getInstance()

    val newQuotes = SingleLiveEvent<List<Quote>>()

    private val fullMList: MutableList<Quote> = ArrayList()
    val fullList: List<Quote> = fullMList

    // хранит последний оффсет для того, чтобы onScrollListener не смог сделать один и тот же запрос несколько раз
    var maxOffset: Int = 0
    // запрещает загрузку элементов, если все цитаты уже выгружены
    private var reachedEnd: Boolean = false

    fun getQuotes(number: Int, fromScrollListener: Boolean) {
        val offset = fullList.size + 1
        if ((!fromScrollListener || offset > maxOffset) && !reachedEnd) {
            progress.set(true)
            maxOffset = offset
            disposables.add(
                repository.getQuotesList(number, offset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnTerminate { progress.set(false) }
                    .subscribe({ resultList ->
                        if (resultList.size < number) reachedEnd = true

                        fullMList.addAll(resultList)

                        newQuotes.postValue(resultList)
                    }, {
                        errorListener?.showError(it.localizedMessage)
                    })
            )
        }
    }
}