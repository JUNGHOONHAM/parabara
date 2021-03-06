package com.hampson.parabara.data.api

import android.content.Context
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.recruit-test.parabara.kr"
const val TOKEN = "MlQct4pW2tGw3BTew6G3gbkw"

const val FIRST_PAGE = 1
const val POST_SIZE = 5

object DBClient {
    fun getClient(context: Context): DBInterface {
        val requestInterceptor = Interceptor { chain ->
            val url : HttpUrl = chain.request()
                .url()
                .newBuilder()
                .build()

            val request : Request = chain.request()
                .newBuilder()
                .url(url)
                .addHeader("x-token", TOKEN)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient : OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DBInterface::class.java)
    }
}