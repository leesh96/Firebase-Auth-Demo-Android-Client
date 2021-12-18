package com.suho.demo.firebaseauth.repository

import com.suho.demo.firebaseauth.remote.RemoteDataSource

object MemberRepository {

    private val apiService = RemoteDataSource.apiService

    suspend fun signIn() = apiService.signIn()
    suspend fun login() = apiService.login()
    suspend fun withdraw() = apiService.withdraw()
}