package com.dicoding.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.local.User
import com.dicoding.storyapp.data.local.UserPreference
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.ui.ViewModelFactory
import com.dicoding.storyapp.ui.main.MainActivity
import com.dicoding.storyapp.ui.register.RegisterActivity
import com.dicoding.storyapp.utils.ApiCallbackString

class LoginActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]

        viewModel.getUser().observe(this) {
            user = User(
                it.name,
                it.token
            )
        }

        setButtonListener()
        playAnimation()

        binding.registerActivity.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setButtonListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            showLoading(true)

            viewModel.login(email, password, object : ApiCallbackString {
                override fun onResponse(success: Boolean, message: String) {
                    if (success) {
                        Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        val moveToMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                        moveToMainActivity.putExtra(MainActivity.EXTRA_USER, user)
                        startActivity(moveToMainActivity)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.login_failed) + message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.appName, View.TRANSLATION_X, -30f, 350f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.llEmail, View.ALPHA, 1f).setDuration(1000)
        val signup = ObjectAnimator.ofFloat(binding.llPassword, View.ALPHA, 1f).setDuration(1000)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(1000)
        val btnRegister = ObjectAnimator.ofFloat(binding.llBtnRegister, View.ALPHA, 1f).setDuration(1000)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(together, btnLogin, btnRegister)
            start()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    }
}