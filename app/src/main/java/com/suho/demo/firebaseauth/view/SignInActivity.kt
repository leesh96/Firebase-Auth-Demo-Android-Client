package com.suho.demo.firebaseauth.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suho.demo.firebaseauth.MyApplication
import com.suho.demo.firebaseauth.R
import com.suho.demo.firebaseauth.databinding.ActivitySignInBinding
import com.suho.demo.firebaseauth.viewmodel.SignInViewModel

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOption: GoogleSignInOptions
    private val googleSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "Firebase Auth with Google: ${account.id}")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
            }
        }
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.view = this
        setContentView(binding.root)
        init()
        setObserver()
    }

    private fun init() {
        googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        firebaseAuth = Firebase.auth

        binding.btnGoogleSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun setObserver() {
        viewModel.member.observe(this) {
            Log.d(TAG, "$it")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun signIn() {
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInResult.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(googleIdToken: String) {
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "SignIn with Credential: success")

                    val currentUser = FirebaseAuth.getInstance().currentUser

                    currentUser?.let {
                        it.getIdToken(true)
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    val firebaseIdToken = task.result.token ?: ""
                                    Log.d(TAG, "Firebase ID Token: $firebaseIdToken")
                                    MyApplication.prefs.setString(TOKEN_KEY, firebaseIdToken)
                                    viewModel.signIn()
                                } else {
                                    Log.e(TAG, "Get Firebase ID Token: fail")
                                }
                            }
                    }
                } else {
                    Log.e(TAG, "SignIn with Credential: fail")
                }
            }
    }

    companion object {
        private const val TAG = "SIGN IN ACTIVITY"
        private const val TOKEN_KEY = "userToken"
    }
}