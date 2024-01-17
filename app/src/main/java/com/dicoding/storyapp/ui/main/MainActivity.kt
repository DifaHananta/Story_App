package com.dicoding.storyapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.local.User
import com.dicoding.storyapp.data.local.UserPreference
import com.dicoding.storyapp.data.paging.StoryRepository
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.retrofit.ApiConfig
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.ui.adapter.ListStoryAdapter
import com.dicoding.storyapp.ui.Maps.MapsActivity
import com.dicoding.storyapp.ui.UserViewModel
import com.dicoding.storyapp.ui.ViewModelFactory
import com.dicoding.storyapp.ui.adapter.LoadingStateAdapter
import com.dicoding.storyapp.ui.detail.DetailStoryActivity
import com.dicoding.storyapp.ui.login.LoginActivity
import com.dicoding.storyapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: ListStoryAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[UserViewModel::class.java]

        val apiService = ApiConfig.getApiService()
        val storyDatabase = StoryDatabase.getDatabase(applicationContext)
        val storyRepository = StoryRepository(dataStore, apiService, storyDatabase)
        val viewModelFactory = MainViewModelFactory(storyRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        user = intent.getParcelableExtra<User>("user") ?: User("", "")

        adapter = ListStoryAdapter()

        adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.EXTRA_NAME, data.name)
                    it.putExtra(DetailStoryActivity.EXTRA_IMAGE, data.photoUrl)
                    it.putExtra(DetailStoryActivity.EXTRA_DESC, data.description)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
        }

        userViewModel.getUser().observe(this) {
            user = User(
                it.name,
                it.token
            )
            getListStory()
        }

    }

    private fun getListStory() {
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.itemStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_story -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        }
        when (item.itemId) {
            R.id.menu_logout -> {
                showLoading(true)
                userViewModel.logout()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                Toast.makeText(this@MainActivity, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        when(item.itemId) {
            R.id.menu_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USER = "user"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}