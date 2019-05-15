package com.lovelessgeek.housemanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lovelessgeek.housemanager.R.layout
import com.lovelessgeek.housemanager.R.string
import com.lovelessgeek.housemanager.base.BaseActivity
import com.lovelessgeek.housemanager.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    companion object {
        private const val RC_SIGN_IN = 0x0000
    }

    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_google_login.setOnClickListener {
            startActivityForResult(
                mGoogleSignInClient.signInIntent,
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                } catch (exception: ApiException) {
                    Log.w(logTag, "Google sign in failed..", exception)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        progress_bar.visibility = ProgressBar.VISIBLE
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(logTag, "Succeed to sign in!")
                    startMainActivity()
                } else {
                    Log.w(logTag, "Failed to sign in..")
                    Snackbar.make(root_view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
                progress_bar.visibility = ProgressBar.INVISIBLE
            }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
