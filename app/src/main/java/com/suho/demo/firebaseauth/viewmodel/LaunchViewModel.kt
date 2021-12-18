package com.suho.demo.firebaseauth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suho.demo.firebaseauth.model.Member
import com.suho.demo.firebaseauth.repository.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class LaunchViewModel : ViewModel() {

    private val _member: MutableLiveData<Member> by lazy {
        MutableLiveData<Member>()
    }
    val member: LiveData<Member>
        get() = _member

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                MemberRepository.login().let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d(TAG, "$it")

                            _member.postValue(Member(it.id, it.name, it.email))
                        }
                    } else {
                        // error handling
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
            }
        }
    }

    companion object {
        private const val TAG = "LAUNCH VIEW MODEL"
    }
}