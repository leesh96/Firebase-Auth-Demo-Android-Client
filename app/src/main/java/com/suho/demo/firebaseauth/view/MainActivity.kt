package com.suho.demo.firebaseauth.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suho.demo.firebaseauth.MyApplication
import com.suho.demo.firebaseauth.databinding.ActivityMainBinding
import com.suho.demo.firebaseauth.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.view = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnLogout.setOnClickListener {
            viewModel.signOut { signOutCallback() }
        }

        viewModel.withdrawCallback.observe(this) {
            if (it) {
                auth.currentUser?.let { user ->
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            MyApplication.prefs.clear()
                            signOutCallback()
                        } else {
                            signOutCallback()
                        }
                    }
                }
            }
        }
    }

    private fun signOutCallback() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}