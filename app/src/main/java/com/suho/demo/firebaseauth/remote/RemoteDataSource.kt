package com.suho.demo.firebaseauth.remote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

    // api 요청마다 토큰 검증을 위해 인터셉터 생성
    private val mHttpClient = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Chain): Response {
                // 요청 헤더에 파이어베이스 ID 토큰 넣기
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer ${MyApplication.prefs.getString(TOKEN_KEY)}")
                    .build()
                // return chain.proceed(newRequest)
                val response = chain.proceed(newRequest)

                // 401 UNAUTHORIZED 에러 응답 시 파이어베이스 ID 토큰 재발급
                if (response.code == 401) {
                    Firebase.auth.currentUser?.let { user ->
                        user.getIdToken(true)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    MyApplication.prefs.setString(TOKEN_KEY, it.result.token ?: "")
                                }
                            }
                    }
                }

                return response
            }
        }).build()

    private val mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(mHttpClient)
        .addConverterFactory(GsonConverterFactory.create(mGson))
        .build()

    val apiService = mRetrofit.create(ApiService::class.java)
}