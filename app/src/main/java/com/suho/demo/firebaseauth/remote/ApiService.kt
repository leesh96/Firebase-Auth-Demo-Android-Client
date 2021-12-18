package com.suho.demo.firebaseauth.remote

import com.suho.demo.firebaseauth.remote.dto.MemberResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/member/signin")
    suspend fun signIn(): Response<MemberResponse>

    @GET("api/member/login")
    suspend fun login(): Response<MemberResponse>

    @DELETE("api/member/withdraw")
    suspend fun withdraw(): Response<Void>
}