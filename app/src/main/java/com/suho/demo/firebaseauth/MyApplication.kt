package com.suho.demo.firebaseauth

import android.app.Application
import com.suho.demo.firebaseauth.util.MySharedPreference

class MyApplication : Application() {

    companion object {
        lateinit var prefs: MySharedPreference
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreference(applicationContext)
    }
}