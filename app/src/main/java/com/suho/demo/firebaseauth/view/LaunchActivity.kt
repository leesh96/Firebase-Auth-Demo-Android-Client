package com.suho.demo.firebaseauth.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suho.demo.firebaseauth.MyApplication
import com.suho.demo.firebaseauth.viewmodel.LaunchViewModel

class LaunchActivity : AppCompatActivity() {

    private val viewModel: LaunchViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        viewModel.member.observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result.token ?: ""
                    MyApplication.prefs.setString(TOKEN_KEY, idToken)
                    viewModel.login()
                } else {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "LAUNCH ACTIVITY"
        private const val TOKEN_KEY = "userToken"
    }
}