package com.yulmaso.oicotest.data

import com.google.gson.GsonBuilder
import com.yulmaso.oicotest.utils.Constants
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(
    private val service: QuotesService
) {

    fun getQuotesList(limit: Int, offset: Int) = service.getQuotesList(limit, offset)
    fun getQuote(id: Int) = service.getQuote(id)

    companion object {
        private var INSTANCE: Repository? = null

        @Synchronized
        fun getInstance(): Repository {
             return INSTANCE ?: Repository(
                    provideRetrofit().create(QuotesService::class.java)
                ).also { INSTANCE = it }
        }

        private fun provideRetrofit(): Retrofit {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build()
        }
    }
}