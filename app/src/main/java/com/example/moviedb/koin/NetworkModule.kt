package com.example.moviedb.koin

import android.os.SystemClock
import android.util.Log
import com.example.moviedb.BuildConfig
import com.example.moviedb.service.network.ApiService
import io.reactivex.schedulers.Schedulers
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { createOkHttpClient() }
    factory { apiService<ApiService>(get(), BuildConfig.BASE_URL) }
}

fun createOkHttpClient() : OkHttpClient {
    val httpInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
        Log.d("Movie", it)
    })

    val dispatcher = Dispatcher()
    dispatcher.maxRequests = 1

    val interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            SystemClock.sleep(1000)
            return chain.proceed(chain.request())
        }
    }

    httpInterceptor.level = HttpLoggingInterceptor.Level.BASIC

    return OkHttpClient.Builder().addInterceptor(httpInterceptor)
        .addNetworkInterceptor(interceptor)
        .dispatcher(dispatcher)
        .build()
}

inline fun <reified T> apiService(okHttpClient: OkHttpClient, url: String) : T{
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    return retrofit.create(T::class.java)
}