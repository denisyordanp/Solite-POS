package com.socialite.solite_pos.view.main.opening

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.R
import com.socialite.solite_pos.databinding.ActivityLoginBinding
import com.socialite.solite_pos.utils.preference.UserPref
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.view.viewModel.UserViewModel
import com.socialite.solite_pos.vo.Status

class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loginButton: LoginButton
    private lateinit var userPref: UserPref
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setUpLogin()
    }

    private fun setUpLogin() {
        userViewModel = UserViewModel.getMainViewModel(this)
        loginButton = LoginButton(_binding.btnLogin)
        userPref = UserPref(this)
        auth = Firebase.auth

        loginButton.setOnClick { submitLogin() }
    }

    private fun submitLogin() {
        val email = _binding.edtLoginEmail.text.toString().trim()
        val pass = _binding.edtLoginPassword.text.toString().trim()

        if (validate(email, pass))
            login(email, pass)
    }

    private fun validate(email: String?, password: String?): Boolean {
        return when {
            email.isNullOrEmpty() -> {
                _binding.edtLoginEmail.error = "Kolom tidak boleh kosong"
                false
            }
            isNotValidEmail(email) -> {
                _binding.edtLoginEmail.error = "Mohon masukan email dengan benar"
                false
            }
            password.isNullOrEmpty() -> {
                _binding.edtLoginPassword.error = "Kolom tidak boleh kosong"
                false
            }
            else -> true
        }
    }

    private fun isNotValidEmail(email: String): Boolean {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun login(email: String, password: String) {
        loginButton.setLoadingButtonOn()
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null)
                            getFromDatabase(user.uid)
                        else
                            showWrongInputMessage("Server Error")
                    } else {
                        showWrongInputMessage("Email atau Password anda tidak ditemukan.")
                    }
                }
    }

    private fun getFromDatabase(userId: String) {
        userViewModel.getUser(userId).observe(this) {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        userPref.userAuthority = it.data.authority
                        toMain()
                    } else {
                        showWrongInputMessage("Database Error")
                    }
                }
                Status.ERROR -> {
                    showWrongInputMessage("Database Error")
                }
            }
        }
    }

    private fun showWrongInputMessage(message: String) {
        loginButton.setLoadingButtonOff()
        auth.signOut()
        MessageBottom(supportFragmentManager)
            .setMessageImage(ResourcesCompat.getDrawable(resources, R.drawable.ic_alert_message, null))
                .setMessage(message)
                .setPositiveListener(getString(android.R.string.ok), null)
                .show()
    }

    class LoginButton(private val view: MaterialCardView) {

        private var textView: TextView = view.findViewById(R.id.tv_btn_login)
        private var progress: ProgressBar = view.findViewById(R.id.pb_btn_login)

        private lateinit var onClick: () -> Unit

        fun setOnClick(onClick: () -> Unit) {
            this.onClick = onClick
            view.setOnClickListener {
                onClick.invoke()
            }
        }

        fun setLoadingButtonOn() {
            textView.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE
            view.setOnClickListener { }
        }

        fun setLoadingButtonOff() {
            textView.visibility = View.VISIBLE
            progress.visibility = View.INVISIBLE
            setOnClick(onClick)
        }
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}