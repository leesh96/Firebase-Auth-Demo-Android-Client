package com.suho.demo.firebaseauth.remote

import com.suho.demo.firebaseauth.remote.dto.MemberResponse
import retrofit2.Response
import retrofit2.http.POST

interface ApiService {

    @POST("api/member/signin")
    suspend fun signIn(): Response<MemberResponse>
}