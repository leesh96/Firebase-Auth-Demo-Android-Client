package com.suho.demo.firebaseauth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suho.demo.firebaseauth.MyApplication
import com.suho.demo.firebaseauth.repository.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {

    private val _withdrawCallback: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val withdrawCallback: LiveData<Boolean>
        get() = _withdrawCallback

    fun signOut(callback: () -> Unit) {
        Firebase.auth.signOut()
        MyApplication.prefs.clear()
        callback()
    }

    fun withdraw() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                MemberRepository.withdraw().let { response ->
                    if (response.isSuccessful) {
                        _withdrawCallback.postValue(true)
                    } else {
                        // error handling
                        _withdrawCallback.postValue(false)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
                _withdrawCallback.postValue(false)
            }
        }
    }

    companion object {
        private const val TAG = "MAIN VIEW MODEL"
    }
}