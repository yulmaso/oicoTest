package com.yulmaso.oicotest.data

import com.yulmaso.oicotest.utils.Constants.TEST_PATH
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuotesService {

    @GET(TEST_PATH)
    fun getQuotesList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Single<List<Quote>>

    @GET("$TEST_PATH/{id}")
    fun getQuote(@Path("id") id: Int): Single<Quote>
}