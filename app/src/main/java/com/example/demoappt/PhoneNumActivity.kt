package com.example.demoappt

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.demoappt.User.SharedPrefNames
import com.example.demoappt.databinding.ActivityPhoneNumBinding

class PhoneNumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPhoneNumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListener()
    }

    private fun setClickListener() {
        binding.ivNext.setOnClickListener {
            if (isButtonEnabled) {
                saveData()
                activityTriversalWithAnimation(OtpScreen::class.java)
            } else {
                binding.etPhoneNumber.error = "Enter valid Phone number"
            }
        }

        setTextChangeListener()
    }

    // Function to save data in shared preferences
    private fun saveData() {
        val name = SharedPrefNames() // Assuming you have a class called `SharedPrefNames`
        val pref = getSharedPreferences(name.SHARED_PREF_NAME, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(name.phoneNumber, binding.etPhoneNumber.text.toString().trim())
        editor.apply()
    }

    private var isButtonEnabled: Boolean = false

    // This function monitors the EditText for changes
    private fun setTextChangeListener() {
        binding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phoneNumber = s.toString().trim()
                if (phoneNumber.length != 10) {
                    isButtonEnabled = false
                    binding.ivNext.setImageResource(R.drawable.phone_next_white)
                } else {
                    isButtonEnabled = true
                    binding.ivNext.setImageResource(R.drawable.phone_next_black)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
    }

    // This function handles moving between activities with animation
    private fun activityTriversalWithAnimation(classname: Class<*>) {
        val intent = Intent(this, classname)
        startActivity(intent)
    }
}