package com.suho.demo.firebaseauth.remote.dto

import com.google.gson.annotations.SerializedName

data class MemberResponse(

    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String
)
