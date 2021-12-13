package com.suho.demo.firebaseauth.remote

import com.google.gson.GsonBuilder
import com.suho.demo.firebaseauth.MyApplication
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteDataSource {

    private const val BASE_URL = "http://182.31.219.155:1130/"
    private const val TOKEN_KEY = "userToken"

    private val mGson = GsonBuilder().setLenient().create()

    private val mHttpClient = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Chain): Response {
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer ${MyApplication.prefs.getString(TOKEN_KEY)}").build()
                return chain.proceed(newRequest)
            }
        }).build()

    private val mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(mHttpClient)
        .addConverterFactory(GsonConverterFactory.create(mGson))
        .build()

    val apiService = mRetrofit.create(ApiService::class.java)
}