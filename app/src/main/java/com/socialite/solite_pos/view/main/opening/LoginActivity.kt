package com.socialite.solite_pos.view.main.opening

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.socialite.solite_pos.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.btnLgGoogle.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}