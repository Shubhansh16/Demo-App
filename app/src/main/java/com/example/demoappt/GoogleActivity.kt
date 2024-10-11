@file:Suppress("DEPRECATION")

package com.example.demoappt

import android.app.Activity
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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

    private lateinit var binding: ActivityGoogleBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this, gso)



        settingClickListener()
        googleClickListener()
    }

    /*private fun googleSignIn(){
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->

        if (result.resultCode== Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manageResults(task)
        }
    }

    private fun manageResults(task: Task<GoogleSignInAccount>){
        if (task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if (account!= null){
                updateUI(account)
            }
        }
        else {
            Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                val intent = Intent(this, PhoneNumActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    private fun settingClickListener(){
        binding.num.setOnClickListener{
            val intent = Intent(this@GoogleActivity, PhoneNumActivity::class.java)
            startActivity(intent)
        }
    }
    private fun googleClickListener(){
        binding.btnGoogle.setOnClickListener{
            val intent = Intent(this@GoogleActivity, PhoneNumActivity::class.java)
            startActivity(intent)
        }
    }
}