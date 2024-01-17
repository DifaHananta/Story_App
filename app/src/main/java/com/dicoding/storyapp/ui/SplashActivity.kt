package com.dicoding.storyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.local.User
import com.dicoding.storyapp.data.local.UserPreference
import com.dicoding.storyapp.databinding.ActivitySplashBinding
import com.dicoding.storyapp.ui.login.LoginActivity
import com.dicoding.storyapp.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore)))[UserViewModel::class.java]

        viewModel.getUser().observe(this) {
            user = User(
                it.name,
                it.token
            )
        }

        viewModel.getUser().observe(this) {
            if (it.token.isEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", user)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    }
}