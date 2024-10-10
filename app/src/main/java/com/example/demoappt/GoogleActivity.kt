@file:Suppress("DEPRECATION")

package com.example.demoappt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.demoappt.databinding.ActivityGoogleBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.android.gms.auth.api.signin.GoogleSignIn as GoogleSignIn


class GoogleActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var binding: ActivityGoogleBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this, gso)

        auth= FirebaseAuth.getInstance()


        settingClickListener()

        binding.btnGoogle.setOnClickListener {
            signGoogle()
        }
    }

    private fun signGoogle(){
      val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    //if successfull authenticate
                    val account  = task.getResult(ApiException::class.java)!!
                    Log.d("GoogleAcitvity", "FirebaseAuthGoogle"+account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e:ApiException){
                    Log.w("GoogleAcitvity", "Google sign in failed",e)
                }
            }
            else {
                Log.w("GoogleAcitvity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task->
                if (task.isSuccessful){
                    Log.d("GoogleAcitvity", "firebaseAuthWithGoogle:success")
                    val user = auth.currentUser
                    val intent = Intent(this, DiscoverActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Log.w("GoogleAcitvity", "firebaseAuthWithGoogle: failed",task.exception)
                }
            }
    }

    private fun settingClickListener(){
        binding.num.setOnClickListener{
            val intent = Intent(this@GoogleActivity, PhoneNumActivity::class.java)
            startActivity(intent)
        }
    }
}